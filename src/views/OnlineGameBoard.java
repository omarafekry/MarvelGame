package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import model.abilities.Ability;
import model.abilities.AreaOfEffect;
import model.abilities.CrowdControlAbility;
import model.abilities.DamagingAbility;
import model.abilities.HealingAbility;
import model.effects.EffectType;
import model.world.Champion;
import model.world.Cover;
import model.world.Damageable;
import model.world.Direction;
import engine.Game;
import exceptions.AbilityUseException;
import exceptions.ChampionDisarmedException;
import exceptions.GameActionException;
import exceptions.InvalidTargetException;
import exceptions.NotEnoughResourcesException;
import exceptions.UnallowedMovementException;

public class OnlineGameBoard extends JPanel{

	Game g;
	JButton[][] b;
	CurrentChampPanel ccp;
	Color enemyColor = Color.red;
	Color allyColor = Color.green;
	OnlineGamePanel gamePanel;
	
	public OnlineGameBoard(OnlineGamePanel gamePanel){
		super();
		this.gamePanel = gamePanel;
		g = gamePanel.getGame();
		b = new JButton[5][5];
		ccp = gamePanel.getChampPanel();
		this.setLayout(new GridLayout(5,5));
		updateBoard();
		
	}
	public JButton[][] getB() {
		return b;
	}
	public void updateBoard(){
		this.removeAll();
		for(int i = 4; i >= 0; i--) {
			for(int j = 0; j < 5; j++) {
				
				b[i][j] = new JButton();
				JButton but = b[i][j];
				//but.add(new JLabel(new ImageIcon("Images/Board.jpg")));
				but.setFocusable(false);
				
				if (g.getBoard()[i][j] instanceof Cover) {
					but.setText("<html><center>Cover<br>Current HP: " +( (Cover)g.getBoard()[i][j]).getCurrentHP()+"</center></html>");
				}
				else if (g.getBoard()[i][j] instanceof Champion){
					but.setText(((Champion)g.getBoard()[i][j]).getName());
					
					but.setLayout(new BorderLayout());
					but.add(new BarPanel(Color.green, ((Champion)g.getBoard()[i][j]).getCurrentHP(), ((Champion)g.getBoard()[i][j]).getMaxHP()), BorderLayout.PAGE_END);
					but.add(new JLabel (new ImageIcon("Images/" + ((Champion)g.getBoard()[i][j]).getName().toLowerCase() + ".png")), BorderLayout.PAGE_START);
					if (sameTeam((Champion)g.getBoard()[i][j]))
						but.setBorder(BorderFactory.createLineBorder(Color.GREEN, 4));
					else
						but.setBorder(BorderFactory.createLineBorder(Color.RED, 4));
					
				}
				this.add(but);
				
			}
		}
		b[g.getCurrentChampion().getLocation().x][g.getCurrentChampion().getLocation().y].setBackground(Color.blue);
		this.updateUI();
		if (g.checkGameOver() != null){
			int input = JOptionPane.showOptionDialog(this.getRootPane(), g.checkGameOver().getName() + " has won! GGWP", "Congrats!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, new ImageIcon("assets/thanos dance hehe.gif"), new String[] {"New Game", "Quit"}, "Quit");
			JFrame frame = getFrame(this);
			if (input == 0){
				frame.remove(this.getParent().getParent());
				frame.add(new MainPanel());
				frame.repaint();
				frame.revalidate();
			}
			else {
				frame.dispose();
				
			}
			
		}
	}
	private JFrame getFrame(Container cont){
		if (cont instanceof JFrame)
			return (JFrame) cont;
		else
			return getFrame(cont.getParent());
	}
	
	public void move(){
		this.updateBoard();
		Champion c = g.getCurrentChampion();
		int y = c.getLocation().x;
		int x = c.getLocation().y;
		
		if (x - 1 >= 0 && g.getBoard()[y][x - 1] == null)
			b[y][x - 1].setBackground(Color.blue);
		
		if (x + 1 < 5 && g.getBoard()[y][x + 1] == null)
			b[y][x + 1].setBackground(Color.blue);
		
		if (y + 1 < 5 && g.getBoard()[y + 1][x] == null)
			b[y + 1][x].setBackground(Color.blue);
		
		if (y - 1 >= 0 && g.getBoard()[y - 1][x] == null)
			b[y - 1][x].setBackground(Color.blue);
		
		if (x - 1 >= 0)
		b[y][x - 1].addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent sender) {
				if (((OnlineGameBoard)((JButton)(sender.getSource())).getParent()) != null){
					try {
						g.move(Direction.LEFT);
					} catch (UnallowedMovementException | NotEnoughResourcesException e) {
						if (found())
							MessageBox.showError(e.getMessage());
						return;
					}
					
					System.out.println("Moved Left");
					if (found())
						gamePanel.getClient().send(buttonToString((JButton)(sender.getSource())));
					((OnlineGameBoard)((JButton)(sender.getSource())).getParent()).updateBoard();
					ccp.update();
				}
			}
		});
		if (x + 1 < 5)
		b[y][x + 1].addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent sender) {
				if (((OnlineGameBoard)((JButton)(sender.getSource())).getParent()) != null){
					try {
						g.move(Direction.RIGHT);
					} catch (UnallowedMovementException | NotEnoughResourcesException e) {
						if (found())
							MessageBox.showError(e.getMessage());
						return;
					}
					//System.out.println(((JButton)sender.getSource()).getParent().getClass().getCanonicalName());
					
					System.out.println("Moved Right");
					if (found())
						gamePanel.getClient().send(buttonToString((JButton)(sender.getSource())));
					((OnlineGameBoard)((JButton)(sender.getSource())).getParent()).updateBoard();
					ccp.update();
				}
			}
		});
		if (y + 1 < 5)
		b[y + 1][x].addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent sender) {
				if (((OnlineGameBoard)((JButton)(sender.getSource())).getParent()) != null){
					try {
						g.move(Direction.UP);
					} catch (UnallowedMovementException | NotEnoughResourcesException e) {
						if (found())
							MessageBox.showError(e.getMessage());
						return;
					}
		
					System.out.println("Moved Up");
					if (found())
						gamePanel.getClient().send(buttonToString((JButton)(sender.getSource())));
					((OnlineGameBoard)((JButton)(sender.getSource())).getParent()).updateBoard();
					ccp.update();
				}
			}
		});
		if (y - 1 >= 0)
		b[y - 1][x].addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent sender) {
				if (((OnlineGameBoard)((JButton)(sender.getSource())).getParent()) != null){
					try {
						g.move(Direction.DOWN);
					} catch (UnallowedMovementException | NotEnoughResourcesException e) {
						if (found())
							MessageBox.showError(e.getMessage());
						return;
					}
					//System.out.println(((OnlineGameBoard)((JButton)(sender.getSource())).getParent()));
					
					System.out.println("Moved Down");
					if (found())
						gamePanel.getClient().send(buttonToString((JButton)(sender.getSource())));
					((OnlineGameBoard)((JButton)(sender.getSource())).getParent()).updateBoard();
					ccp.update();
				}
			}
		});
		
	}
	
	private boolean sameTeam(Champion b){
		if ((g.getFirstPlayer().getTeam().contains(g.getCurrentChampion()) && g.getFirstPlayer().getTeam().contains(b)) || (g.getSecondPlayer().getTeam().contains(g.getCurrentChampion()) && g.getSecondPlayer().getTeam().contains(b)))
			return true;
		return false;
	}
	
	public void attack(){
		updateBoard();
		Champion c = g.getCurrentChampion();
		int y = c.getLocation().x;
		int x = c.getLocation().y;
		int up = 0, down = 0, left = 0, right = 0;
		
		for (int i = 1; i <= c.getAttackRange(); i++){
			
			if (left == 0 && x - i >= 0 && g.getBoard()[y][x - i] != null){
				int curx = x-i;
				int cury = y;
				Damageable d = (Damageable)g.getBoard()[cury][curx];
				if (d instanceof Champion && sameTeam((Champion)d))
					b[cury][curx].setBackground(allyColor);
				else
					b[cury][curx].setBackground(enemyColor);
				
				left = i;
				b[y][x - i].addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent sender) {
						if (((OnlineGameBoard)((JButton)(sender.getSource())).getParent()) != null){
							try {
								g.attack(Direction.LEFT);
							} catch (NotEnoughResourcesException | ChampionDisarmedException | InvalidTargetException e) {
								if (found())
									MessageBox.showError(e.getMessage());
							}
							
							System.out.println("Attacked Left");
							if (found())
								gamePanel.getClient().send(buttonToString((JButton)(sender.getSource())));
							((OnlineGameBoard)((JButton)(sender.getSource())).getParent()).updateBoard();
							ccp.update();
						}
					}
				});
			}
			
			if (right == 0 && x + i < 5 && g.getBoard()[y][x + i] != null){
				int curx = x+i;
				int cury = y;
				Damageable d = (Damageable)g.getBoard()[cury][curx];
				if (d instanceof Champion && sameTeam((Champion)d))
					b[cury][curx].setBackground(allyColor);
				else
					b[cury][curx].setBackground(enemyColor);
				right = i;
				b[y][x + i].addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent sender) {
						if (((OnlineGameBoard)((JButton)(sender.getSource())).getParent()) != null){
							try {
								g.attack(Direction.RIGHT);
							} catch (NotEnoughResourcesException | ChampionDisarmedException | InvalidTargetException e) {
								if (found())
									MessageBox.showError(e.getMessage());
							}
							
							System.out.println("Attacked Right");
							if (found())
								gamePanel.getClient().send(buttonToString((JButton)(sender.getSource())));
							((OnlineGameBoard)((JButton)(sender.getSource())).getParent()).updateBoard();
							ccp.update();
						}
					}
				});
			}
			
			if (up == 0 && y + i < 5 && g.getBoard()[y + i][x] != null){
				int curx = x;
				int cury = y + i;
				Damageable d = (Damageable)g.getBoard()[cury][curx];
				if (d instanceof Champion && sameTeam((Champion)d))
					b[cury][curx].setBackground(allyColor);
				else
					b[cury][curx].setBackground(enemyColor);
				up = i;
				b[y + i][x].addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent sender) {
						if (((OnlineGameBoard)((JButton)(sender.getSource())).getParent()) != null){
							try {
								g.attack(Direction.UP);
							} catch (NotEnoughResourcesException | ChampionDisarmedException | InvalidTargetException e) {
								if (found())
									MessageBox.showError(e.getMessage());
							}
							
							System.out.println("Attacked Up");
							if (found())
								gamePanel.getClient().send(buttonToString((JButton)(sender.getSource())));
							((OnlineGameBoard)((JButton)(sender.getSource())).getParent()).updateBoard();
							ccp.update();
						}
					}
				});
			}
			
			if (down == 0 && y - i >= 0 && g.getBoard()[y - i][x] != null){
				int curx = x;
				int cury = y-i;
				Damageable d = (Damageable)g.getBoard()[cury][curx];
				if (d instanceof Champion && sameTeam((Champion)d))
					b[cury][curx].setBackground(allyColor);
				else
					b[cury][curx].setBackground(enemyColor);
				down = i;
				b[y - i][x].addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent sender) {
						if (((OnlineGameBoard)((JButton)(sender.getSource())).getParent()) != null){
							try {
								g.attack(Direction.DOWN);
							} catch (NotEnoughResourcesException | ChampionDisarmedException | InvalidTargetException e) {
								if (found())
									MessageBox.showError(e.getMessage());
							}
							
							System.out.println("Attacked Down");
							if (found())
								gamePanel.getClient().send(buttonToString((JButton)(sender.getSource())));
							((OnlineGameBoard)((JButton)(sender.getSource())).getParent()).updateBoard();
							ccp.update();
						}
					}
				});
			}
			
			
		}
		for (int i = 1; i <= c.getAttackRange(); i++){
			if (left == 0 && x - i >= 0)
			b[y][x - i].addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent sender) {
					if (((OnlineGameBoard)((JButton)(sender.getSource())).getParent()) != null){
						try {
							g.attack(Direction.LEFT);
						} catch (NotEnoughResourcesException | ChampionDisarmedException | InvalidTargetException e) {
							if (found())
								MessageBox.showError(e.getMessage());
						}
						
						System.out.println("Attacked Left");
						if (found())
							gamePanel.getClient().send(buttonToString((JButton)(sender.getSource())));
						((OnlineGameBoard)((JButton)(sender.getSource())).getParent()).updateBoard();
						ccp.update();
					}
				}
			});
			if (right == 0 && x + i < 5)
			b[y][x + i].addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent sender) {
					if (((OnlineGameBoard)((JButton)(sender.getSource())).getParent()) != null){
						try {
							g.attack(Direction.RIGHT);
						} catch (NotEnoughResourcesException | ChampionDisarmedException | InvalidTargetException e) {
							if (found())
								MessageBox.showError(e.getMessage());
						}
						
						System.out.println("Attacked Right");
						if (found())
							gamePanel.getClient().send(buttonToString((JButton)(sender.getSource())));
						((OnlineGameBoard)((JButton)(sender.getSource())).getParent()).updateBoard();
						ccp.update();
					}
				}
			});
			if (up == 0 && y + i < 5)
			b[y + i][x].addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent sender) {
					if (((OnlineGameBoard)((JButton)(sender.getSource())).getParent()) != null){
						try {
							g.attack(Direction.UP);
						} catch (NotEnoughResourcesException | ChampionDisarmedException | InvalidTargetException e) {
							if (found())
								MessageBox.showError(e.getMessage());
						}
						
						System.out.println("Attacked Up");
						if (found())
							gamePanel.getClient().send(buttonToString((JButton)(sender.getSource())));
						((OnlineGameBoard)((JButton)(sender.getSource())).getParent()).updateBoard();
						ccp.update();
					}
				}
			});
			if (down == 0 && y - i >= 0)
			b[y - i][x].addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent sender) {
				if (((OnlineGameBoard)((JButton)(sender.getSource())).getParent()) != null){
					try {
						g.attack(Direction.DOWN);
					} catch (NotEnoughResourcesException | ChampionDisarmedException | InvalidTargetException e) {
						if (found())
							MessageBox.showError(e.getMessage());
					}
					
					System.out.println("Attacked Down");
					if (found())
						gamePanel.getClient().send(buttonToString((JButton)(sender.getSource())));
					((OnlineGameBoard)((JButton)(sender.getSource())).getParent()).updateBoard();
					ccp.update();
				}
			}
		});
		}
	}
	
	public void cast(final Ability a, final OnlineControlsPanel cp){
		updateBoard();
		Champion curr = g.getCurrentChampion();
		int x = curr.getLocation().y;
		int y = curr.getLocation().x;	
		ArrayList<Champion> team;
		boolean Allys;
		
		if (g.getFirstPlayer().getTeam().contains(curr))
			team = g.getFirstPlayer().getTeam();
		else
			team = g.getSecondPlayer().getTeam();
		
		AreaOfEffect area = a.getCastArea();
		if (area == AreaOfEffect.SINGLETARGET){
			for(int i = 0; i < 5; i++){
				for(int j = 0; j < 5; j++){
					final int i1 = i, j1 = j;
					if (g.getBoard()[i][j] instanceof Damageable && g.ManhattanDistance(curr.getLocation(), ((Damageable)g.getBoard()[i][j]).getLocation()) <= a.getCastRange()){
						int curx = j;
						int cury = i;
						Damageable d = (Damageable)g.getBoard()[cury][curx];
						if (d instanceof Champion && sameTeam((Champion)d))
							b[cury][curx].setBackground(allyColor);
						else
							b[cury][curx].setBackground(enemyColor);
					}
					b[i][j].addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e) {
							try{
								g.castAbility(a, i1, j1);
							}catch(GameActionException | CloneNotSupportedException ex){
								if (found())
									MessageBox.showError(ex.getMessage());
							}
							System.out.println("Cast SINGLETARGET");
							if (found())
								gamePanel.getClient().send(buttonToString((JButton)(e.getSource())));
							updateBoard();
							ccp.update();
							cp.resetList();
						}
					});	
				}
			}
			return;
		}
		else if (area == AreaOfEffect.DIRECTIONAL){
			for (int i = 1; i <= a.getCastRange(); i++){
				if (curr.getLocation().x + i < 5){
					b[curr.getLocation().x + i][curr.getLocation().y].setBackground(Color.cyan);
					b[curr.getLocation().x + i][curr.getLocation().y].addActionListener(new ActionListener(){
	
						public void actionPerformed(ActionEvent e) {
							try {
								g.castAbility(a, Direction.UP);
							} catch (NotEnoughResourcesException
									| InvalidTargetException | AbilityUseException
									| CloneNotSupportedException e1) {
								if (found())
									MessageBox.showError(e1.getMessage());
							}
							
							System.out.println("Cast DIRECTIONAL UP");
							if (found())
								gamePanel.getClient().send(buttonToString((JButton)(e.getSource())));
							updateBoard();
							ccp.update();
							cp.resetList();
						}
						
					});
				}
				if (curr.getLocation().x - i >= 0){
					b[curr.getLocation().x - i][curr.getLocation().y].setBackground(Color.cyan);
					b[curr.getLocation().x - i][curr.getLocation().y].addActionListener(new ActionListener(){
	
						public void actionPerformed(ActionEvent e) {
							try {
								g.castAbility(a, Direction.DOWN);
							} catch (NotEnoughResourcesException
									| InvalidTargetException | AbilityUseException
									| CloneNotSupportedException e1) {
								if (found())
									MessageBox.showError(e1.getMessage());
							}
							
							System.out.println("Cast DIRECTIONAL DOWN");
							if (found())
								gamePanel.getClient().send(buttonToString((JButton)(e.getSource())));
							updateBoard();
							ccp.update();
							cp.resetList();
						}
						
					});
				}
				if (curr.getLocation().y + i < 5){
					b[curr.getLocation().x][curr.getLocation().y + i].setBackground(Color.cyan);
					b[curr.getLocation().x][curr.getLocation().y + i].addActionListener(new ActionListener(){
	
						public void actionPerformed(ActionEvent e) {
							try {
								g.castAbility(a, Direction.RIGHT);
							} catch (NotEnoughResourcesException
									| InvalidTargetException | AbilityUseException
									| CloneNotSupportedException e1) {
								if (found())
									MessageBox.showError(e1.getMessage());
							}
							
							System.out.println("Cast DIRECTIONAL RIGHT");
							if (found())
								gamePanel.getClient().send(buttonToString((JButton)(e.getSource())));
							updateBoard();
							ccp.update();
							cp.resetList();
						}
						
					});
				}
				if (curr.getLocation().y - i >= 0){
					b[curr.getLocation().x][curr.getLocation().y - i].setBackground(Color.cyan);
					b[curr.getLocation().x][curr.getLocation().y - i].addActionListener(new ActionListener(){
	
						public void actionPerformed(ActionEvent e) {
							try {
								g.castAbility(a, Direction.LEFT);
							} catch (NotEnoughResourcesException
									| InvalidTargetException | AbilityUseException
									| CloneNotSupportedException e1) {
								if (found())
									MessageBox.showError(e1.getMessage());
							}
							
							System.out.println("Cast DIRECTIONAL LEFT");
							if (found())
								gamePanel.getClient().send(buttonToString((JButton)(e.getSource())));
							updateBoard();
							ccp.update();
							cp.resetList();
						}
						
					});
				}
			}
		}
		
		ArrayList<Damageable> targets = new ArrayList<Damageable>();
		if (a instanceof DamagingAbility && (area == AreaOfEffect.SURROUND || area == AreaOfEffect.TEAMTARGET))
			targets = g.getTargets(area, a.getCastRange(), true, true);
		
		else if (a instanceof HealingAbility && (area == AreaOfEffect.SURROUND || area == AreaOfEffect.TEAMTARGET || area == AreaOfEffect.SELFTARGET))
			targets = g.getTargets(area, a.getCastRange(), false, false);
		
		else if (a instanceof CrowdControlAbility && ((CrowdControlAbility)a).getEffect().getType() == EffectType.DEBUFF && (area == AreaOfEffect.SURROUND || area == AreaOfEffect.TEAMTARGET || area == AreaOfEffect.SELFTARGET))
			targets = g.getTargets(area, a.getCastRange(), true, false);
		
		else if (a instanceof CrowdControlAbility && ((CrowdControlAbility)a).getEffect().getType() == EffectType.BUFF && (area == AreaOfEffect.SURROUND || area == AreaOfEffect.TEAMTARGET || area == AreaOfEffect.SELFTARGET))
			targets = g.getTargets(area, a.getCastRange(), false, false);
		

		
		for(Damageable d : targets){
			int curx = d.getLocation().y;
			int cury = d.getLocation().x;
			if (d instanceof Champion && sameTeam((Champion)d))
				b[cury][curx].setBackground(allyColor);
			else
				b[cury][curx].setBackground(enemyColor);
		}
	}
	
	public void enable(){
		for(JButton[] buttArray : b){
			for(JButton butt : buttArray){
				butt.setEnabled(true);
			}
		}
	}
	public void disable(){
		for(JButton[] buttArray : b){
			for(JButton butt : buttArray){
				butt.setEnabled(false);
			}
		}
	}
	private String buttonToString(JButton butt){
		for(int i = 0; i < b.length; i++)
			for(int j = 0; j < b[i].length; j++)
				if (butt.equals(b[i][j]))
					return "" + i + "," + j;
		return "null";
	}
	private boolean found(){
		for(Champion champ : gamePanel.getClient().getPlayer().getTeam())
			if (champ.getName().equals(g.getCurrentChampion().getName()))
				return true;
		return false;
	}
	
	public void click(int i, int j) {
		b[i][j].setEnabled(true);
		b[i][j].doClick();
		gamePanel.checkCurrent();
	}
}
