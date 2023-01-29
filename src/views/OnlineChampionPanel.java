package views;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.*;

import network.Client;
import network.GameServer;
import model.abilities.*;
import model.world.*;
import engine.*;

public class OnlineChampionPanel extends JPanel implements Serializable{
	Game g;
	int playerTurn;
	Player player, otherPlayer;
	OnlineChampionPanel thisPanel;
	JButton add, leader1, leader2;
	Client client;
	JList allChampions, p1List, p2List;
	
	@SuppressWarnings("static-access")
	public OnlineChampionPanel(final Client client){
		super();
		
		
		thisPanel = this;
		this.client = client;
		
		player = client.getPlayer();
		otherPlayer = client.getOtherPlayer();
		
				
		playerTurn = 1;
		
		JPanel selectionPanel = new JPanel();
		JPanel p1Panel = new JPanel();
		JPanel p2Panel = new JPanel();
		
		this.setLayout(new GridBagLayout());
	
		
		selectionPanel.setLayout(new GridBagLayout());
		p1Panel.setLayout(new GridBagLayout());
		p2Panel.setLayout(new GridBagLayout());
		//this.setBackground(Color.pink);
		
		Font dFont = new Font("Dialog", Font.PLAIN, 24);
		
		g = client.getGame();
		
		p1List = new JList();
		p2List = new JList();
		final JList p1abList = new JList();
		final JList p2abList = new JList();
		final JScrollPane s1 = new JScrollPane(p1List);
		final JScrollPane s2 = new JScrollPane(p2List);
		final JScrollPane s1ab = new JScrollPane(p1abList);
		final JScrollPane s2ab = new JScrollPane(p2abList);
		final JTextArea p1text = new JTextArea("Type: "
				+ "\nHealth: "
				+ "\nMana: "
				+ "\nAttack Damage: "
				+ "\nAttack Range: "
				+ "\nAction Points: "
				+ "\nSpeed: ");
		final JTextArea p2text = new JTextArea("Type: "
				+ "\nHealth: "
				+ "\nMana: "
				+ "\nAttack Damage: "
				+ "\nAttack Range: "
				+ "\nAction Points: "
				+ "\nSpeed: ");
		final JTextArea p1abtext = new JTextArea("Type: "
				+ "\nMana Cost: " 
				+ "\nCooldown: "
				+ "\nCast Range: "
				+ "\nCast Area: "
				+ "\nAction Points: ");
		final JTextArea p2abtext = new JTextArea("Type: "
				+ "\nMana Cost: " 
				+ "\nCooldown: "
				+ "\nCast Range: "
				+ "\nCast Area: "
				+ "\nAction Points: ");
		leader1 = new JButton("<html><center>Set As Leader<br>Current Leader: None</center></html>");
		leader2 = new JButton("<html><center>Set As Leader<br>Current Leader: None</center></html>");
		
		
		final DefaultListModel allChampionsModel = new DefaultListModel();
		for(Champion c : g.getAvailableChampions())
			allChampionsModel.addElement(c);
		allChampions = new JList(allChampionsModel);
		final JList abilities = new JList();
		add = new JButton("Add Champion");
		final JTextArea att = new JTextArea("Type: "
				+ "\nHealth: "
				+ "\nMana: "
				+ "\nAttack Damage: "
				+ "\nAttack Range: "
				+ "\nAction Points: "
				+ "\nSpeed: ");
		final JTextArea abilityAtt = new JTextArea("Type: "
				+ "\nMana Cost: " 
				+ "\nCooldown: "
				+ "\nCast Range: "
				+ "\nCast Area: "
				+ "\nAction Points: ");
		
		abilities.setFont(dFont);
		abilities.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		abilities.setLayoutOrientation(JList.VERTICAL);
		
		
		JScrollPane aScroller = new JScrollPane(abilities);
		aScroller.setHorizontalScrollBar(null);
		aScroller.setPreferredSize(new Dimension(300,115));
		aScroller.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		
		allChampions.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		allChampions.setFont(dFont);
		allChampions.setLayoutOrientation(JList.VERTICAL);
		JScrollPane cScroller = new JScrollPane(allChampions);
		//cScroller.setMinimumSize(new Dimension(200, 400));
		
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(10,10,10,10);
		LabeledPanel avChampPanel = new LabeledPanel("Available Champions", cScroller);
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 2;
		c.fill = GridBagConstraints.VERTICAL;
		//c.weighty = 1;
		//c.weightx = 1;
		c.anchor = GridBagConstraints.CENTER;
		selectionPanel.add(avChampPanel, c);
		
		final JLabel img = new JLabel();
		img.setPreferredSize(new Dimension(250,250));
		img.setMaximumSize(new Dimension(250,250));
		img.setHorizontalAlignment(JLabel.CENTER);
		img.setBorder(BorderFactory.createLineBorder(Color.black, 4));
		c.fill = GridBagConstraints.NONE;
		c.gridheight = 1;
		c.gridy = 2;
		selectionPanel.add(img, c);
		
		att.setFont(dFont);
		att.setEditable(false);
		att.setHighlighter(null);
		att.setBackground(this.getBackground());
		att.setPreferredSize(new Dimension(300,240));
		att.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		
		LabeledPanel attPanel = new LabeledPanel("Attributes", att);
		Border black = BorderFactory.createLineBorder(Color.BLACK,3);
		attPanel.setBorder(black);
		//attPanel.setBackground(new Color(255,255,255));
		//c.weighty = 0.5;
		//c.fill = GridBagConstraints.BOTH;
		c.gridx = 1;
		c.gridy = 0;
		c.gridheight = 1;
		//c.fill = GridBagConstraints.BOTH;
		selectionPanel.add(attPanel, c);
		
		//c.fill = GridBagConstraints.NONE;
		c.gridy = 1;
		c.weighty = 0;
		LabeledPanel avAbPanel = new LabeledPanel("Available Abilities", aScroller);
		avAbPanel.setBorder(black);
		//avAbPanel.setMaximumSize(new Dimension(300, 150));
		selectionPanel.add(avAbPanel, c);
		//this.setBackground(new Color(0,0,0));
		c.weighty = 1;
		allChampions.addListSelectionListener(new ListSelectionListener(){
		
			
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting() && allChampions.getSelectedIndex() != -1){
					Champion c = (Champion)allChampions.getSelectedValue();
					//System.out.println(allChampions.getSelectedValue());
					abilities.setListData(c.getAbilities().toArray());
					att.setText("Type: " + c.getClass().getSimpleName() + 
							"\nHealth: " + c.getMaxHP()
							+ "\nMana: " + c.getMana()
							+ "\nAttack Damage: " + c.getAttackDamage()
							+ "\nAttack Range: " + c.getAttackRange()
							+ "\nAction Points: " + c.getMaxActionPointsPerTurn()
							+ "\nSpeed: " + c.getSpeed());
					abilityAtt.setText("Type: "
							+ "\nMana Cost: " 
							+ "\nCooldown: "
							+ "\nCast Range: "
							+ "\nCast Area: "
							+ "\nAction Points: ");
					//img.removeAll();
					img.setIcon(new ImageIcon("Images/" + c.getName().toLowerCase() + ".png"));
				}
				
			}
			
		});
		
		 
		abilityAtt.setFont(dFont);
		abilityAtt.setHighlighter(null);
		abilityAtt.setEditable(false);
		abilityAtt.setPreferredSize(new Dimension(300,265));
		abilityAtt.setBackground(this.getBackground());
		abilityAtt.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		c.gridy = 2;
		LabeledPanel abAtPanel = new LabeledPanel("Ability Attributes", abilityAtt);
		abAtPanel.setBorder(black);
		selectionPanel.add(abAtPanel, c);
		
		abilities.addListSelectionListener(new ListSelectionListener(){

			public void valueChanged(ListSelectionEvent e) {
				Ability a = (Ability)abilities.getSelectedValue();
				if (a instanceof DamagingAbility){
					abilityAtt.setText("Type: Damaging"
							+ "\nMana Cost: " + a.getManaCost() 
							+ "\nCooldown: " + a.getBaseCooldown()
							+ "\nCast Range: " + a.getCastRange()
							+ "\nCast Area: " + a.getCastArea().name()
							+ "\nAction Points: " + a.getRequiredActionPoints()
							+ "\nDamage Amount: " + ((DamagingAbility)a).getDamageAmount());
				}
				else if (a instanceof HealingAbility){
					abilityAtt.setText("Type: Healing"
							+ "\nMana Cost: " + a.getManaCost() 
							+ "\nCooldown: " + a.getBaseCooldown()
							+ "\nCast Range: " + a.getCastRange()
							+ "\nCast Area: " + a.getCastArea().name()
							+ "\nAction Points: " + a.getRequiredActionPoints()
							+ "\nHeal Amount: " + ((HealingAbility)a).getHealAmount());
				}
				else if (a instanceof CrowdControlAbility){
					abilityAtt.setText("Type: Crowd Control"
							+ "\nMana Cost: " + a.getManaCost() 
							+ "\nCooldown: " + a.getBaseCooldown()
							+ "\nCast Range: " + a.getCastRange()
							+ "\nCast Area: " + a.getCastArea().name()
							+ "\nAction Points: " + a.getRequiredActionPoints()
							+ "\nEffect: " + ((CrowdControlAbility)a).getEffect().getName()
							+ "\nDuration: " + ((CrowdControlAbility)a).getEffect().getDuration());
				}
				
			}
			
		});
		
		add.setPreferredSize(new Dimension(200, 70));
		add.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				
				if (add.getText().equals("Start Game")){
					
					client.setGame(new Game(player, otherPlayer));
					
					client.send(client.getGame());
					System.out.println("Sent game to other player");
					
					JFrame f = (JFrame)((JButton)e.getSource()).getParent().getParent().getParent().getParent().getParent().getParent().getParent();
					f.remove(thisPanel);
					f.add(new OnlineGamePanel(client));
					f.revalidate();
					f.repaint();
										
					
					return;
				}
				
				if (allChampions.getSelectedValue() == null){
					MessageBox.showError("Please pick a champion to add");
					return;
				}
				
				
				
				if (client.getPlayerNumber() == playerTurn){
					
					Champion selectedChampion = (Champion)allChampions.getSelectedValue();
					//client.getPlayer().getTeam().add(selectedChampion);
					player.getTeam().add(selectedChampion);
					System.out.println("your team size: " + player.getTeam().size());
					g.getAvailableChampions().remove((Champion)allChampions.getSelectedValue());
					allChampions.setListData(g.getAvailableChampions().toArray());
					p1List.setListData(player.getTeam().toArray());
					
					sendChampion(selectedChampion);
				}
				
				att.setText("Type: "
						+ "\nHealth: "
						+ "\nMana: "
						+ "\nAttack Damage: "
						+ "\nAttack Range: "
						+ "\nAction Points: "
						+ "\nSpeed: ");
				abilityAtt.setText("Type: "
						+ "\nMana Cost: " 
						+ "\nCooldown: "
						+ "\nCast Range: "
						+ "\nCast Area: "
						+ "\nAction Points: ");
				abilities.setListData(new Object[0]);
				int turns = 0;
				turns += g.getFirstPlayer().getTeam().size();
				turns += g.getSecondPlayer().getTeam().size();
				
				if (turns == 6){
					add.setText("Start Game");
					return;
				}
				img.setIcon(null);
			}
			
		});
		
		
		JPanel butPanel = new JPanel();
		butPanel.setLayout(new GridBagLayout());
		c.fill = GridBagConstraints.NONE;
		c.ipadx = 0;
		c.ipady = 0;
		c.weighty = 0;
		c.insets = new Insets(0,0,20,0);
		c.gridx = 0;
		c.gridwidth = 2;
		butPanel.add(add, c);
		
		c.gridy = 4;
		selectionPanel.add(butPanel, c);
		GridBagConstraints con = new GridBagConstraints();
		con.fill = GridBagConstraints.BOTH;
		con.insets = new Insets(0,0,0,0);
		con.gridx = 1;
		con.ipady = 0;
		con.weightx = 1;
		con.weighty = 1;
		this.add(selectionPanel, con);
		
		
		
		GridBagConstraints co = new GridBagConstraints();
		co.insets = new Insets(0,0,0,0);
		co.weighty = 1;
		co.gridy = 0;
		c.ipadx = 0;
		c.ipady = 0;
		p1List.setFont(dFont);
		p2List.setFont(dFont);
		s1.setPreferredSize(new Dimension(300,105));
		s2.setPreferredSize(new Dimension(300,105));
		p1Panel.add(new LabeledPanel(player.getName() + "'s Team", s1), co);
		p2Panel.add(new LabeledPanel(otherPlayer.getName()  + "'s Team", s2), co);
		
		co.gridy = 1;
		p1text.setPreferredSize(new Dimension(300,225));
		p2text.setPreferredSize(new Dimension(300,225));
		p1text.setFont(dFont);
		p2text.setFont(dFont);
		p1text.setHighlighter(null);
		p1text.setEditable(false);
		p2text.setHighlighter(null);
		p2text.setEditable(false);
		p1Panel.add(new LabeledPanel("Champion Attributes", p1text), co);
		p2Panel.add(new LabeledPanel("Champion Attributes", p2text), co);
		
		co.gridy = 2;
		leader1.setPreferredSize(new Dimension(250,50));
		leader2.setPreferredSize(new Dimension(250,50));
		leader1.setHorizontalAlignment(SwingConstants.CENTER);
		leader2.setHorizontalAlignment(SwingConstants.CENTER);
		p1Panel.add(leader1, co);
		p2Panel.add(leader2, co);
		
		co.gridy = 3;
		//co.fill = GridBagConstraints.NONE;
		p1abList.setFont(dFont);
		p2abList.setFont(dFont);
		s1ab.setPreferredSize(new Dimension(300,105));
		s2ab.setPreferredSize(new Dimension(300,105));
		p1Panel.add(new LabeledPanel("Champion Abilities", s1ab), co);
		p2Panel.add(new LabeledPanel("Champion Abilities", s2ab), co);
		
		co.gridy = 4;
		p1abtext.setPreferredSize(new Dimension(300,260));
		p2abtext.setPreferredSize(new Dimension(300,260));
		p1abtext.setFont(dFont);
		p2abtext.setFont(dFont);
		p1abtext.setHighlighter(null);
		p1abtext.setEditable(false);
		p2abtext.setHighlighter(null);
		p2abtext.setEditable(false);
		p1Panel.add(new LabeledPanel("Ability Attributes", p1abtext), co);
		p2Panel.add(new LabeledPanel("Ability Attributes", p2abtext), co);
		
		leader1.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				if ((Champion)p1List.getSelectedValue() != null){
					//client.getPlayer().setLeader((Champion)p1List.getSelectedValue());
					player.setLeader((Champion)p1List.getSelectedValue());
					leader1.setText("Current Leader: " + (Champion)p1List.getSelectedValue());
					leader1.setEnabled(false);
					sendChampion((Champion)p1List.getSelectedValue());
					if (otherPlayer.getLeader() != null){
						leader1.setEnabled(false);
						add.setText("Waiting for " + otherPlayer.getName());
						
						new Thread(new Runnable(){
							@Override
							public void run() {
								receiveStart();						
							}
							
						}).start();
					}
				}
			}
			
		});
		p1List.addListSelectionListener(new ListSelectionListener(){
		
			
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting() && p1List.getSelectedIndex() != -1){
					Champion c = (Champion)p1List.getSelectedValue();
					//System.out.println(allChampions.getSelectedValue());
					p1abList.setListData(c.getAbilities().toArray());
					p1text.setText("Type: " + c.getClass().getSimpleName() + 
							"\nHealth: " + c.getMaxHP()
							+ "\nMana: " + c.getMana()
							+ "\nAttack Damage: " + c.getAttackDamage()
							+ "\nAttack Range: " + c.getAttackRange()
							+ "\nAction Points: " + c.getMaxActionPointsPerTurn()
							+ "\nSpeed: " + c.getSpeed());
					p1abtext.setText("Type: "
							+ "\nMana Cost: " 
							+ "\nCooldown: "
							+ "\nCast Range: "
							+ "\nCast Area: "
							+ "\nAction Points: ");
				}
				
			}
			
		});
		p2List.addListSelectionListener(new ListSelectionListener(){
		
			
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting() && p2List.getSelectedIndex() != -1){
					Champion c = (Champion)p2List.getSelectedValue();
					//System.out.println(allChampions.getSelectedValue());
					p2abList.setListData(c.getAbilities().toArray());
					p2text.setText("Type: " + c.getClass().getSimpleName() + 
							"\nHealth: " + c.getMaxHP()
							+ "\nMana: " + c.getMana()
							+ "\nAttack Damage: " + c.getAttackDamage()
							+ "\nAttack Range: " + c.getAttackRange()
							+ "\nAction Points: " + c.getMaxActionPointsPerTurn()
							+ "\nSpeed: " + c.getSpeed());
					p2abtext.setText("Type: "
							+ "\nMana Cost: " 
							+ "\nCooldown: "
							+ "\nCast Range: "
							+ "\nCast Area: "
							+ "\nAction Points: ");
				}
				
			}
			
		});
		p1abList.addListSelectionListener(new ListSelectionListener(){

			public void valueChanged(ListSelectionEvent e) {
				Ability a = (Ability)p1abList.getSelectedValue();
				if (a instanceof DamagingAbility){
					p1abtext.setText("Type: Damaging"
							+ "\nMana Cost: " + a.getManaCost() 
							+ "\nCooldown: " + a.getBaseCooldown()
							+ "\nCast Range: " + a.getCastRange()
							+ "\nCast Area: " + a.getCastArea().name()
							+ "\nAction Points: " + a.getRequiredActionPoints()
							+ "\nDamage Amount: " + ((DamagingAbility)a).getDamageAmount());
				}
				else if (a instanceof HealingAbility){
					p1abtext.setText("Type: Healing"
							+ "\nMana Cost: " + a.getManaCost() 
							+ "\nCooldown: " + a.getBaseCooldown()
							+ "\nCast Range: " + a.getCastRange()
							+ "\nCast Area: " + a.getCastArea().name()
							+ "\nAction Points: " + a.getRequiredActionPoints()
							+ "\nHeal Amount: " + ((HealingAbility)a).getHealAmount());
				}
				else if (a instanceof CrowdControlAbility){
					p1abtext.setText("Type: Crowd Control"
							+ "\nMana Cost: " + a.getManaCost() 
							+ "\nCooldown: " + a.getBaseCooldown()
							+ "\nCast Range: " + a.getCastRange()
							+ "\nCast Area: " + a.getCastArea().name()
							+ "\nAction Points: " + a.getRequiredActionPoints()
							+ "\nEffect: " + ((CrowdControlAbility)a).getEffect().getName()
							+ "\nDuration: " + ((CrowdControlAbility)a).getEffect().getDuration());
				}
				
			}
			
		});
		p2abList.addListSelectionListener(new ListSelectionListener(){

			public void valueChanged(ListSelectionEvent e) {
				Ability a = (Ability)p2abList.getSelectedValue();
				if (a instanceof DamagingAbility){
					p2abtext.setText("Type: Damaging"
							+ "\nMana Cost: " + a.getManaCost() 
							+ "\nCooldown: " + a.getBaseCooldown()
							+ "\nCast Range: " + a.getCastRange()
							+ "\nCast Area: " + a.getCastArea().name()
							+ "\nAction Points: " + a.getRequiredActionPoints()
							+ "\nDamage Amount: " + ((DamagingAbility)a).getDamageAmount());
				}
				else if (a instanceof HealingAbility){
					p2abtext.setText("Type: Healing"
							+ "\nMana Cost: " + a.getManaCost() 
							+ "\nCooldown: " + a.getBaseCooldown()
							+ "\nCast Range: " + a.getCastRange()
							+ "\nCast Area: " + a.getCastArea().name()
							+ "\nAction Points: " + a.getRequiredActionPoints()
							+ "\nHeal Amount: " + ((HealingAbility)a).getHealAmount());
				}
				else if (a instanceof CrowdControlAbility){
					p2abtext.setText("Type: Crowd Control"
							+ "\nMana Cost: " + a.getManaCost() 
							+ "\nCooldown: " + a.getBaseCooldown()
							+ "\nCast Range: " + a.getCastRange()
							+ "\nCast Area: " + a.getCastArea().name()
							+ "\nAction Points: " + a.getRequiredActionPoints()
							+ "\nEffect: " + ((CrowdControlAbility)a).getEffect().getName()
							+ "\nDuration: " + ((CrowdControlAbility)a).getEffect().getDuration());
				}
				
			}
			
		});
		
		con.gridx = 0;
		
		this.add(p1Panel, con);
		con.gridx = 2;
		
		this.add(p2Panel, con);
		
		leader1.setEnabled(false);
		leader2.setEnabled(false);
		
		if (player.equals(g.getSecondPlayer())){
			add.setEnabled(false);
			add.setText(otherPlayer.getName() + "'s Turn");
			
			new Thread(new Runnable(){
				public void run() {
					receiveChampion();
				}
			}).start();
		}
	}
	
	
	private void sendChampion(Champion champion){
		if (otherPlayer.getLeader() == null){
			add.setText(otherPlayer.getName() + "'s turn");
			add.setEnabled(false);
		}
		thisPanel.repaint();
		thisPanel.revalidate();
		
		if (playerTurn == 1)
			playerTurn = 2;
		else
			playerTurn = 1;
		client.sendChampion(champion);
		System.out.println("Sent champion: " + champion.getName());
		
		if (player.getLeader() == null || otherPlayer.getLeader() == null)
			new Thread(new Runnable(){
				public void run() {
					receiveChampion();
				}
			}).start();
		
	}
	
	@SuppressWarnings({ "static-access", "unchecked" })
	private void receiveChampion(){
		Champion otherChampion = client.receiveChampion();
		System.out.println("Received champion: " + otherChampion.getName());
		g = client.getGame();
		
		player = client.getPlayer();
		otherPlayer = client.getOtherPlayer();
		
		p2List.setListData(otherPlayer.getTeam().toArray());
		
		for(int i = 0; i < g.getAvailableChampions().size(); i++){
			for(int j = 0; j < otherPlayer.getTeam().size(); j++){
				if (g.getAvailableChampions().get(i).getName().equals(otherPlayer.getTeam().get(j).getName())){
					g.getAvailableChampions().remove(i);
					break;
				}
			}
		}
		allChampions.setListData(g.getAvailableChampions().toArray());
		System.out.println("other team size: " + otherPlayer.getTeam().size());
		
		if (player.getTeam().size() == 3 && otherPlayer.getTeam().size() == 3){
		
			leader1.setEnabled(true);
			add.setText("Choose a leader");
			
			if (otherPlayer.getLeader() != null)
				leader2.setText("Current Leader: " + otherPlayer.getLeader().getName());
	
			
			
			if (player.getLeader() != null){ //first chose a leader, received from second
				leader1.setEnabled(false);
				leader2.setText("Current Leader: " + otherPlayer.getLeader().getName());
				add.setText("Start Game");
				add.setEnabled(true);
			}
			
			
		}
		else{
			add.setEnabled(true);
			add.setText("Add Champion");
			thisPanel.repaint();
			thisPanel.revalidate();
		}
		
		
		thisPanel.repaint();
		thisPanel.revalidate();
		if (playerTurn == 1)
			playerTurn = 2;
		else
			playerTurn = 1;
			
	}
	
	private void receiveStart(){
		Game game = (Game)client.receive();
		System.out.println("Received game from host");
		client.setGame(game);
		
		JFrame f = (JFrame)(thisPanel.getParent().getParent().getParent().getParent());
		f.remove(thisPanel);
		f.add(new OnlineGamePanel(client));
		f.repaint();
		f.revalidate();
	}
	
	
	public static void main(String[] args){
		
		JFrame f = new JFrame();
		f.add(new OnlinePlayerPanel());
		//f.setExtendedState(JFrame.MAXIMIZED_BOTH);
		f.setSize(800,500);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLocationRelativeTo(null);
		//f.setMinimumSize(Toolkit.getDefaultToolkit().getScreenSize());
		//f.setUndecorated(true);
		f.setVisible(true);
		
	}
}
