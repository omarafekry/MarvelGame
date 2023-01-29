package views;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.abilities.Ability;
import model.abilities.AreaOfEffect;
import model.abilities.CrowdControlAbility;
import model.abilities.DamagingAbility;
import model.abilities.HealingAbility;
import model.world.Champion;
import network.Client;
import engine.Game;
import exceptions.AbilityUseException;
import exceptions.LeaderAbilityAlreadyUsedException;
import exceptions.LeaderNotCurrentException;
import exceptions.NotEnoughResourcesException;

public class OnlineControlsPanel extends JPanel{
	
	Game g;
	Champion c;
	OnlineGameBoard b;
	JList ab;
	JButton cast, move, end, attack, leader;
	JTextArea att;
	CurrentChampPanel ccp;
	boolean enabled = true;
	Client client;
	ActionListener al;
	ArrayList<JButton> buttons;
	OnlineGamePanel gamePanel;
	
	
	public OnlineControlsPanel(final OnlineGamePanel gamePanel) {
		this.gamePanel = gamePanel;
		this.client = gamePanel.getClient();
		c = gamePanel.getGame().getCurrentChampion();
		g = gamePanel.getGame();
		b = gamePanel.getBoard();
		ccp = gamePanel.getChampPanel();
		
		cast = new JButton("Select An Ability");
		att = new JTextArea(
				"Type: "
				+ "\nMana Cost: " 
				+ "\nCooldown: "
				+ "\nCast Range: "
				+ "\nCast Area: "
				+ "\nAction Points: ");
		JPanel right = new JPanel();
		right.setLayout(new GridBagLayout());
		JPanel left = new JPanel();
		left.setLayout(new GridBagLayout());
		
		Font dFont = new Font("Dialog", Font.PLAIN, 20);
		
		ab = new JList(c.getAbilities().toArray());
		ab.setFont(dFont);
		ab.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ab.setLayoutOrientation(JList.VERTICAL);
		
		JScrollPane aScroller = new JScrollPane(ab);
		aScroller.setHorizontalScrollBar(null);
		aScroller.setPreferredSize(new Dimension(250,100));
		aScroller.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		
		
		GridBagConstraints con = new GridBagConstraints();
		
		con.insets = new Insets(10,25,10,25);
		con.gridx = 0;
		con.gridy = 0;
		right.add(new LabeledPanel("Available Abilities", aScroller), con);
		
		att.setFont(dFont);
		att.setHighlighter(null);
		att.setEditable(false);
		att.setPreferredSize(new Dimension(280,240));
		
		con.gridy = 1;
		right.add(new LabeledPanel("Ability Attributes", att), con);
		
		cast.setFont(dFont);
		cast.setEnabled(false);
		
		con.gridx = 0;
		con.gridy = 2;
		right.add(cast, con);
		
		
		
		move = new JButton("Move");
		
		move.setFont(dFont);
		move.setPreferredSize(new Dimension(100, 50));
		con.gridy = 0;
		left.add(move, con);
		attack = new JButton("Attack");
		
		attack.setFont(dFont);
		attack.setPreferredSize(new Dimension(100, 50));
		con.gridy = 1;
		left.add(attack, con);
		
		leader = new JButton("Use Leader Ability");
		leader.setFont(dFont);
		leader.setPreferredSize(new Dimension(200, 50));
		
		con.gridx = 0;
		con.gridy = 2;
		
		left.add(leader, con);
		
		end = new JButton("End Turn");
		
		end.setFont(dFont);
		end.setPreferredSize(new Dimension(150, 50));
		con.gridy = 3;
		left.add(end, con);
		
		
		con.gridx = 0;
		con.gridy = 0;
		this.add(right, con);
		con.gridx = 1;
		con.gridy = 0;
		
		this.add(left, con);
		
	
		
		
		
		
		buttons = new ArrayList<JButton>();
		buttons.add(move);
		buttons.add(end);
		buttons.add(leader);
		buttons.add(cast);
		buttons.add(attack);
		
		
		for(JButton butt : buttons)
			for(ActionListener actionListener : butt.getActionListeners())
				butt.removeActionListener(actionListener);
		for(ListSelectionListener listener : ab.getListSelectionListeners())
			ab.removeListSelectionListener(listener);
		
		
		attack.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				
				client.send(((JButton)arg0.getSource()).getText());
				System.out.println("Sent " + ((JButton)arg0.getSource()).getText() + " to other player");
				
				attack();
			}
		});
		leader.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				
				client.send(((JButton)arg0.getSource()).getText());
				System.out.println("Sent " + ((JButton)arg0.getSource()).getText() + " to other player");
				
				leader();
			}
		});
		move.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				
				client.send(((JButton)arg0.getSource()).getText());
				System.out.println("Sent " + ((JButton)arg0.getSource()).getText() + " to other player");
				
				move();
			}
		});
		cast.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				
				client.send(((JButton)arg0.getSource()).getText());
				System.out.println("Sent " + ((JButton)arg0.getSource()).getText() + " to other player");
				
				cast();
			}
		});
		end.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				
				client.send(((JButton)arg0.getSource()).getText());
				System.out.println("Sent " + ((JButton)arg0.getSource()).getText() + " to other player");
				
				end();
			}
		});
		ab.addListSelectionListener(new ListSelectionListener(){

			public void valueChanged(ListSelectionEvent e) {
				boolean found = false;
				for(Champion champ : client.getPlayer().getTeam())
					if (champ.getName().equals(g.getCurrentChampion().getName()))
						found = true;
				
				if (!ab.getValueIsAdjusting() && ab.getSelectedIndex() != -1 && found){
					client.send("Ability " + ab.getSelectedIndex());
					System.out.println("Sent Ability " + ab.getSelectedIndex() + " to other player");
					
					select();
				}
			}
			
		});
		
	}
	public void disableButtons(){
		enabled = false;
		for(JButton butt : buttons)
			butt.setEnabled(false);
		ab.setEnabled(false);
	}
	public void enableButtons(){
		boolean oldCast = cast.isEnabled();
		enabled = true;
		for(JButton butt : buttons)
			butt.setEnabled(true);
		ab.setEnabled(true);
		cast.setEnabled(oldCast);
	}
	
	public void click(String text){
		if (text.split(" ")[0].equals("Ability")){
			ab.setSelectedIndex(Integer.parseInt(text.split(" ")[1]));
			select();
		}
		else if (text.equals("End Turn"))
			end();
		else if (text.equals("Attack"))
			attack();
		else if (text.equals("Cast Ability"))
			cast();
		else if (text.equals("Use Leader Ability"))
			leader();
		else if (text.equals("Move"))
			move();
			
	}
	public void attack(){
		b.attack();
		gamePanel.checkCurrent();
		if (ab.getSelectedIndex() == -1)
			cast.setEnabled(false);
	}
	public void leader(){
		try {
			g.useLeaderAbility();
		} catch (LeaderNotCurrentException
				| LeaderAbilityAlreadyUsedException
				| CloneNotSupportedException e) {
			MessageBox.showError(e.getMessage());
		}
		b.updateBoard();
		ccp.update();
		gamePanel.checkCurrent();
		if (ab.getSelectedIndex() == -1)
			cast.setEnabled(false);
	}
	public void cast(){
		try {
			g.castAbility((Ability)ab.getSelectedValue());
		} catch (NotEnoughResourcesException | AbilityUseException
				| CloneNotSupportedException e) {
			if (client.getPlayer().getTeam().contains(g.getCurrentChampion()))
				MessageBox.showError(e.getMessage());
		}
		b.updateBoard();
		resetList();
		ccp.update();
		gamePanel.checkCurrent();
	}
	public void move(){
		b.move();
		gamePanel.checkCurrent();
		if (ab.getSelectedIndex() == -1)
			cast.setEnabled(false);
	}
	public void end(){
		g.endTurn();
		ccp.update();
		b.updateBoard();
		update();
		gamePanel.checkCurrent();
		cast.setEnabled(false);
	}
	public void select(){
		Ability a = (Ability)ab.getSelectedValue();
		OnlineControlsPanel cp = (OnlineControlsPanel)ab.getParent().getParent().getParent().getParent().getParent();
		if (a instanceof DamagingAbility){
			att.setText("Type: Damaging"
					+ "\nMana Cost: " + a.getManaCost()
					+ "\nCurrent Cooldown: " + a.getCurrentCooldown()
					+ "\nBase Cooldown: " + a.getBaseCooldown()
					+ "\nCast Range: " + a.getCastRange()
					+ "\nCast Area: " + a.getCastArea().name()
					+ "\nAction Points: " + a.getRequiredActionPoints()
					+ "\nDamage Amount: " + ((DamagingAbility)a).getDamageAmount());
		}
		else if (a instanceof HealingAbility){
			att.setText("Type: Healing"
					+ "\nMana Cost: " + a.getManaCost() 
					+ "\nCurrent Cooldown: " + a.getCurrentCooldown()
					+ "\nBase Cooldown: " + a.getBaseCooldown()
					+ "\nCast Range: " + a.getCastRange()
					+ "\nCast Area: " + a.getCastArea().name()
					+ "\nAction Points: " + a.getRequiredActionPoints()
					+ "\nHeal Amount: " + ((HealingAbility)a).getHealAmount());
		}
		else if (a instanceof CrowdControlAbility){
			att.setText("Type: Crowd Control"
					+ "\nMana Cost: " + a.getManaCost() 
					+ "\nCurrent Cooldown: " + a.getCurrentCooldown()
					+ "\nBase Cooldown: " + a.getBaseCooldown()
					+ "\nCast Range: " + a.getCastRange()
					+ "\nCast Area: " + a.getCastArea().name()
					+ "\nAction Points: " + a.getRequiredActionPoints()
					+ "\nEffect: " + ((CrowdControlAbility)a).getEffect().getName()
					+ "\nDuration: " + ((CrowdControlAbility)a).getEffect().getDuration());
		}
		
		
		if (a.getCastArea() == AreaOfEffect.SINGLETARGET){
			cast.setText("Select Location On Board");
			cast.setEnabled(false);
		}
		else if (a.getCastArea() == AreaOfEffect.DIRECTIONAL){
			cast.setText("Select Direction On Board");
			cast.setEnabled(false);
		}
		else{
			cast.setText("Cast Ability");
			cast.setEnabled(true);
		}
		
		b.cast(a, cp);
		gamePanel.checkCurrent();
		if (client.getPlayer().getTeam().contains(g.getCurrentChampion()) && !cast.getText().equals("Cast Ability"))
			cast.setEnabled(false);
	}
	
	public void update(){
		resetList();
		c = g.getCurrentChampion();
		ab.setListData(c.getAbilities().toArray());
		att.setText(
				"Type: "
				+ "\nMana Cost: " 
				+ "\nCooldown: "
				+ "\nCast Range: "
				+ "\nCast Area: "
				+ "\nAction Points: ");
		
	}
	public void resetList(){
		cast.setText("Select An Ability");
		cast.setEnabled(false);
		ab.clearSelection();
		att.setText(
				"Type: "
				+ "\nMana Cost: " 
				+ "\nCooldown: "
				+ "\nCast Range: "
				+ "\nCast Area: "
				+ "\nAction Points: ");
	}
}
