import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.Timer;
public class BoxHead extends JFrame implements ActionListener{
	Timer fireTimer,myTimer;
	public final int START=0,GAME = 1;
	public int state=GAME;
	GamePanel game;
	private ArrayList<Zombie> allZombies = new ArrayList<Zombie>(); //this stores all of the zombies that are currently in the game
	private ArrayList<Devil> allDevils = new ArrayList<Devil>(); //this stores all of the devils that are currently running around in the game
	public ArrayList<PosPair> fireballs = new ArrayList<PosPair>(); //this stores all of the fireballs that are currently in the game
	//make an arraylist of active bullets that save the info about the bullet including the type of gun
	public ArrayList<PosPair> activeBullets = new ArrayList<PosPair>(); //private?
	public ArrayList<Image> bulletSprites=new ArrayList<Image>();
	MainCharacter mc;
	StartScreen startS;
	public BoxHead(){
		super("BoxHead Zombies");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800,640);
		//activeBullets.add(new PosPair(100,100,0,100));
		fireballs.add(new PosPair(100,100,0,100));
		setLayout(new BorderLayout());
		game = new GamePanel(this);
		fireTimer = new Timer(100,this); //timer used to increase the timeCount on the fireball for the devil
		myTimer = new Timer(20,this); //myTimer is used to record the time for general movements in the game
		mc = new MainCharacter("damn it leo", 200, 200);
		startS = new StartScreen(this);
		startS.setLocation(0,0);
		startS.setSize(800,640);
		add(startS);
		game.setLocation(0,0);
		game.setSize(800,640);
		add(game);
		game.setFocusable(true);
		game.setVisible(true);
		setResizable(false);
		setVisible(true);
		game.addNotify();
		start();
	}
	public void start(){
		//start the timers
		fireTimer.start();
		myTimer.start();
    }
	public void actionPerformed(ActionEvent evt) {
		Object source = evt.getSource();
		if (state==START){
			startS.repaint();
		}
		else if (state==GAME){
			if (source==myTimer){
				//move character
				//move zombies
				game.moveMC();
				game.MCshoot();
				game.checkBulletCollision();
				game.moveBullets();
			}
			if (source==fireTimer){
				game.moveFireballs();
				//check if there are devil
				//if there are, add on to the timer counter for each devil
			}
			game.repaint();
		}
	}
	public static void main(String[] args){
		new BoxHead();
	}
}
