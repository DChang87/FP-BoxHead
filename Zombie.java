import java.util.*;
class Zombie {
	private int Health = 2000;
	private int posx,posy, sx = 30, sy = 70;
	public Zombie(int x, int y){
		posx = x;
		posy = y;
	}
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
	public int getHealth(){
		return Health;
	}
	public void setHealth(int hp){
		Health = hp;
	}
	public boolean getCollide(int x, int y){
		return (posx >= x && x <= posx+sx && posy >= y && y <= posy+sy);
	}
	public int getsx(){
		return sx;
	}
	public int getsy(){
		return sy;
	}
}
