import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
public class SelectionMenu extends JPanel implements KeyListener{
	private static int mouseX,mouseY;
	private Image background = new ImageIcon("selectionBackground.jpg").getImage();
	private boolean down=false;
	BoxHead BH;
	private boolean[] keys;
	private Font font = new Font("Impact", Font.PLAIN, 35);
	public SelectionMenu(BoxHead b){
		//load the images for the start button
		System.out.println("Selection menu");
		keys = new boolean[65535];
		BH=b;
		addKeyListener(this);
		setSize(800,640);
	}
    //public void addNotify() {
    	//super.addNotify();
    	//requestFocus();
    //}
    public void keyTyped(KeyEvent e){
		
	}
    public void keyPressed(KeyEvent e){
    	keys[e.getKeyCode()]=true;
    }
    public void keyReleased(KeyEvent e){
    	keys[e.getKeyCode()]=false;
    }
    public void checkUnPause(){
    	if (keys[KeyEvent.VK_P] || down){
			BH.state=BH.GAME;
			System.out.println("checkunpause");
			BH.game.requestFocus();
			keys[KeyEvent.VK_P]=false;
		}
	}
    public void paintComponent(Graphics g){
    	g.setFont(font);
    	g.drawImage(background,0,0,this);
    	for (int i=0;i<13;i++){
    		if (BH.game.getNextUpgrade()>i){
    			g.setColor(Color.green);
    		}
    		else{
    			g.setColor(Color.red);
    			
    		}
    		g.drawString(BH.ug.getUpgradeString(i), 30, 150+i*30);
    	}
    	for (int i=13;i<26;i++){
    		if (BH.game.getNextUpgrade()>i){
    			g.setColor(Color.green);
    		}
    		else{
    			g.setColor(Color.red);
    		}
    		g.drawString(BH.ug.getUpgradeString(i), 430, 150+(i-13)*30);
    	}
    }
    
	    
}
