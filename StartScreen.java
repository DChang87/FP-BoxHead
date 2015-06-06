import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
public class StartScreen extends JPanel{
	private static int mouseX,mouseY;
	private Image background = new ImageIcon("tempbackground.jpg").getImage();
	private boolean down=false;
	BoxHead BH;
	private int counter=50;
	public StartScreen(BoxHead b){
		//load the images for the start button
		BH=b;
		setSize(800,640);
	}
    //public void addNotify() {
    	//super.addNotify();
    	//requestFocus();
    //}
    public void paintComponent(Graphics g){
    	g.drawImage(background,0,0,this);
    }
	    
}
