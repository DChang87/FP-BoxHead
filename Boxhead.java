import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.Timer;
public class BoxHead extends JFrame implements ActionListener{
	Timer fireTimer,myTimer,shootTimer,zombieTimer,enemyGenerationTimer;
	public final int START=0,GAME = 1,PAUSE=2;
	public int state=START;
	public final int OVER=3;
	GameOver go;
	public int score = 0;
	GamePanel game;
	JButton startB;
	//need an arraylist to store these values and assign them as levels go
	MainCharacter mc;
	StartScreen startS;
	SelectionMenu sm;
	public BoxHead(){
		super("BoxHead Zombies");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800,640);
		//setLayout(new BorderLayout());
		setLayout(null);
		shootTimer = new Timer(100,this);
		zombieTimer = new Timer(200,this);
		enemyGenerationTimer = new Timer(20000,this);
		fireTimer = new Timer(100,this); //timer used to increase the timeCount on the fireball for the devil
		myTimer = new Timer(20,this); //myTimer is used to record the time for general movements in the game
		mc = new MainCharacter("damn it leo", 100, 400);		
		game = new GamePanel(this);
		game.setLocation(0,0);
		game.setSize(800,640);
		add(game);
		sm = new SelectionMenu(this);
		sm.setLocation(0,0);
		sm.setSize(800,640);
		add(sm);
		go = new GameOver(this);
		go.setLocation(0,0);
		go.setSize(800,640);
		add(go);
		startB = new JButton("START");
		startB.setLocation(350, 400);
		startB.setSize(100, 50);
		startB.addActionListener(this);
		add(startB);
		startB.setVisible(true);
		
		startS = new StartScreen(this);
		startS.setLocation(0,0);
		startS.setSize(800,640);
		add(startS);
		
		
		setResizable(false);
		setVisible(true);
	}
	public void start(){
		//start the timers
		fireTimer.start();
		shootTimer.start();
		myTimer.start();
		zombieTimer.start();
		enemyGenerationTimer.start();
    }
	public void actionPerformed(ActionEvent evt) {
		Object source = evt.getSource();
		if (state==START){
			
			startB.setVisible(true);
			if (source==startB){
				startB.setVisible(false);
				state=GAME;
	    		game.requestFocus();
			}
			startS.repaint();
		}
		else if (state==GAME){
			if (source==myTimer){
				game.moveZombie();
				game.moveDevil();
				game.moveMC();
				game.moveBullets();
				game.checkBulletCollision();
				game.checkMC();
				game.checkPause();
				game.moveMap();
				game.checkBoxCollision();
				game.updateposition();
				game.switchWeapon();
				game.checkLevelOver();
				game.CountDown();
				game.checkBarricades();
				
			}
			if (source == zombieTimer){	
				game.addZombieCounter();
			}
			if (source==shootTimer){
				game.MCshoot();
			}
			if (source==fireTimer){
				game.moveFireballs();
			}
			if (source==enemyGenerationTimer){
				game.generateEnemy();
			}
			game.repaint();
		}
		else if (state==PAUSE){
			if (source==myTimer){
				sm.checkUnPause();
			}
			sm.repaint();
		}
		else if (state==OVER){
			go.repaint();
		}
	}
	public static void main(String[] args){
		new BoxHead();
	}
}
