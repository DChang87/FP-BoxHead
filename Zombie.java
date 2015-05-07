import java.util.*;
class Zombie {
	private int health = 5;
	private int posx,posy;
	public int getX(){
		return posx;
	}
	public int getY(){
		return posy;
	}
	public void setX(int x){
		posx = x;
	}
	public void setY(int y){
		posy = y;
	}
	public void inContact(MainCharacter Leo){
		if (Leo.getX()==posx && Leo.getY()==posy){
			Leo.setHealth(Leo.getHealth()-5);
		}
	}
}
