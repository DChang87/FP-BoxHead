import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.Timer;
public class BoxHead extends JFrame implements ActionListener{
	Timer fireTimer,myTimer,spritesTimer,enemyGenerationTimer,SentryTimer;
	public final int START=0,GAME = 1,PAUSE=2,OVER=3,HS=4; //different states/screens in the game
	public int state=START; //game starts with the startScreen
	public int score = 0;  
	
	GameOver go;
	GamePanel game;
	HighScore hs;
	JButton startB;
	MainCharacter mc;
	StartScreen startS;
	SelectionMenu sm;
	Upgrades ug;
	
	public int magicalBoxAllowance=1; //the number of items in the magicalBoxes. 
	//for now it is only "Health" (increase in health), player gets more as more weapons are obtained
	
	public BoxHead(){
		super("BoxHead Zombies");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800,640);
		setLayout(null); 

		//set timers
		spritesTimer = new Timer(200,this);
		enemyGenerationTimer = new Timer(2000,this);
		SentryTimer = new Timer(500,this);
		fireTimer = new Timer(100,this); //timer used to increase the timeCount on the fireball for the devil
		myTimer = new Timer(20,this); //myTimer is used to record the time for general movements in the game
		
		//load the classes
		mc = new MainCharacter(100, 400);
		ug = new Upgrades(this);
	
		hs = new HighScore(this);
		hs.setLocation(0,0);
		hs.setSize(800, 640);
		add(hs);
		
		go = new GameOver(this);
		go.setLocation(0,0);
		go.setSize(800,640);
		add(go);
		
		game = new GamePanel(this);
		game.setLocation(0,0);
		game.setSize(800,640);
		add(game);
		
		sm = new SelectionMenu(this);
		sm.setLocation(0,0);
		sm.setSize(800,640);
		add(sm);
		
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
		myTimer.start();
		spritesTimer.start();
		enemyGenerationTimer.start();
		SentryTimer.start();
    }
	public void actionPerformed(ActionEvent evt) {
		Object source = evt.getSource();
		if (state==START){
			startS.repaint();
		}
		else if (state==GAME){
			if (source==myTimer){
				game.moveZombie();
				game.moveDevil();
				game.moveMC();
				game.moveBullets();
				
				game.checkBulletCollision();
				game.checkBulletDistance();
				game.checkBoxCollision();
				
				game.checkMC();//checks if the MC is in contact with the enemies
				game.checkPause(); //checks if the game is paused
				game.moveMap(); //moves map as the character walks
				game.updateposition(); //updates the position of all the items in the game since the map moves
				game.switchWeapon();
				game.checkLevelOver(); //checks if the level is over
				game.CountDown(); //counts down the timer to keep track of consecutive kills
				game.checkObjects(); //checks the health and details of different objects on the map				
			}
			if (source == spritesTimer){
				//these methods add on to the counter for blitting the enemy sprites
				game.addZombieCounter();
				game.addDevilCounter();
			}
			if (source==fireTimer){
				game.moveFireballs();
			}
			if (source==enemyGenerationTimer){
				game.generateEnemy();
			}
			if (source==SentryTimer){
				game.shootSentry(); //this shoots the automatic sentry gun
			}
			game.MCshoot();
			game.repaint();
			game.countdownGrenade(); //counts down the counter on the Grenade before explosion
			game.PostGrenadeExplosion(); //counts down for the grenade after explosion for sprite blitting
			game.UpgradeCountDown();  //counts down the counter to blit the string for upgrading
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
		else if (state==HS){
			hs.repaint();
		}
	}
	public static void main(String[] args){
		new BoxHead();
	}
}
