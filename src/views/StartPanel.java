package views;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import engine.Game;
import engine.Player;

public class StartPanel extends JPanel{
	Image Background;
	Game g;
	
	public StartPanel(Game g){
		this.g=g;
		final Player p1 = g.getFirstPlayer();
		final Player p2 = g.getSecondPlayer();
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		try {
			Background = ImageIO.read(getClass().getResource("/vs.jpg"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		JLabel P1name = new JLabel(g.getFirstPlayer().getName() +"'s team");
		P1name.setFont(new Font("Dialog", Font.BOLD, 30));
		P1name.setForeground(Color.white);
		JLabel P2name = new JLabel(g.getSecondPlayer().getName() +"'s team");
		P2name.setFont(new Font("Dialog", Font.BOLD, 30));
		P2name.setForeground(Color.white);
		
		
		JLabel p1c1 = new JLabel(new ImageIcon("assets/" + g.getFirstPlayer().getTeam().get(0).getName().toLowerCase() + ".png"));
		JLabel p1c2 = new JLabel(new ImageIcon("assets/" + g.getFirstPlayer().getTeam().get(1).getName().toLowerCase() + ".png"));
		JLabel p1c3 = new JLabel(new ImageIcon("assets/" + g.getFirstPlayer().getTeam().get(2).getName().toLowerCase() + ".png"));
		JLabel p2c1 = new JLabel(new ImageIcon("assets/" + g.getSecondPlayer().getTeam().get(0).getName().toLowerCase() + ".png"));
		JLabel p2c2 = new JLabel(new ImageIcon("assets/" + g.getSecondPlayer().getTeam().get(1).getName().toLowerCase() + ".png"));
		JLabel p2c3 = new JLabel(new ImageIcon("assets/" + g.getSecondPlayer().getTeam().get(2).getName().toLowerCase() + ".png"));
		
		
		c.gridx=0;
		c.gridy=0;
		c.weightx=1;
		c.weighty=1;		
		c.insets = new Insets(10,10,10,10);
		this.add(P1name,c);
	
		c.gridx=1;
		this.add(P2name,c);
		
		c.gridy=1;
		c.gridx =0;
		this.add(p1c1,c);
		
		c.gridx =1 ;
		this.add(p2c1,c);
		
		c.gridy=2;
		c.gridx =0;
		this.add(p1c2,c);
		
		c.gridx =1;
		this.add(p2c2,c);
		
		c.gridy=3;
		c.gridx =0;
		this.add(p1c3,c);
		
		c.gridx=1;
		this.add(p2c3,c);
		
		c.gridy=4;
		c.gridx =0;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.PAGE_END;
	    JButton Start = new JButton("Start Game");
	    Start.setBackground(Color.red);
	    Start.setAlignmentX(Component.CENTER_ALIGNMENT);
	    Start.setPreferredSize(new Dimension(200,50));
	    
	    Start.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				StartPanel sp = (StartPanel)((JButton)e.getSource()).getParent();
				JFrame f = (JFrame)((JButton)e.getSource()).getParent().getParent().getParent().getParent().getParent();
				f.remove(sp);
				f.add(new GamePanel(new Game(p1,p2)));
				f.revalidate();
				f.repaint();	
			}
	    });
		
	    this.add(Start,c);
		this.setVisible(true);
		
		
	}
	
	protected void paintComponent(Graphics g) {

	    super.paintComponent(g);
	    	g.drawImage(Background, 0, 0, getWidth(), getHeight(), null);
	}
	
	
	public static void main(String[] args){
		JFrame test = new JFrame();
		test.setSize(400,400);

		
		
		
	}
	
	
}
