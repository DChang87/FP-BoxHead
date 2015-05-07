import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;


public class GamePanel extends JPanel implements KeyListener{
	private BoxHead BH;
	public GamePanel(BoxHead bh){
		BH=bh;
	}

	public void keyPressed(KeyEvent evt){
	}

	public void keyReleased(KeyEvent evt){
	}

	public void keyTyped(KeyEvent evt){
	}
	public int calculateHealth(){
		//full health is at 100
		return (int)(BH.mc.getHealth()/100*20);
	}
	public void paintComponent(Graphics g){
		Font Sfont = new Font("Calisto MT", Font.BOLD, 30);
		g.setFont(Sfont);
		g.drawString(BH.mc.getName(), BH.mc.getX()-5, BH.mc.getY()-10); //maybe do the string formatting with this later if we have time
		//drawing the health bar
		//draw the inside first
		//then draw the outline of the whole thing
		//figure out the colouring of the bar ugh
		g.drawRect(BH.mc.getX()-5,BH.mc.getY()-3,calculateHealth(),5);
		g.drawRect(BH.mc.getX()-5, BH.mc.getY()-3, 20, 5);
	}
}
