import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.Timer;
public class BoxHead extends JFrame implements ActionListener{
	Timer fireTimer,myTimer,shootTimer,zombieTimer,enemyGenerationTimer,SentryTimer;
	public final int START=0,GAME = 1,PAUSE=2,HS=4;
	public int state=START;
	public final int OVER=3;
	GameOver go;
	public int score = 0;
	GamePanel game;
	HighScore hs;
	JButton startB;
	//need an arraylist to store these values and assign them as levels go
	MainCharacter mc;
	StartScreen startS;
	SelectionMenu sm;
	Upgrades ug;
	public int magicalBoxAllowance=1;
	public BoxHead(){
		super("BoxHead Zombies");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800,640);
		//setLayout(new BorderLayout());
		setLayout(null);
		shootTimer = new Timer(100,this);
		zombieTimer = new Timer(200,this);
		enemyGenerationTimer = new Timer(2000,this);
		SentryTimer = new Timer(500,this);
		fireTimer = new Timer(100,this); //timer used to increase the timeCount on the fireball for the devil
		myTimer = new Timer(20,this); //myTimer is used to record the time for general movements in the game
		mc = new MainCharacter("damn it leo", 100, 400);
		ug = new Upgrades(this);
		go = new GameOver(this);
		hs = new HighScore(this);
		hs.setLocation(0,0);
		hs.setSize(800, 640);
		add(hs);
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
		shootTimer.start();
		myTimer.start();
		zombieTimer.start();
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
				game.checkMC();
				game.checkPause();
				game.moveMap();
				game.checkBoxCollision();
				game.updateposition();
				game.switchWeapon();
				game.checkLevelOver();
				game.CountDown();
				game.checkObjects();
				
			}
			if (source == zombieTimer){	
				game.addZombieCounter();
			}
			if (source==shootTimer){
				//game.MCshoot();
			}
			if (source==fireTimer){
				game.moveFireballs();
			}
			if (source==enemyGenerationTimer){
				game.generateEnemy();
			}
			if (source==SentryTimer){
				game.shootSentry();
			}
			game.MCshoot();
			game.repaint();
			game.countdownGrenade();
			game.PostGrenadeExplosion();
			game.UpgradeCountDown();
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
