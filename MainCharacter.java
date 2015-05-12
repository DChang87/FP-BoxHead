import java.util.*;
class MainCharacter {
	private int health;
	private String name;
	private int posx,posy;
	private final int NUMOFWEAPONS = 5;
	private int[] weapons = new int[NUMOFWEAPONS];
	public MainCharacter(String n){
		name = n;
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
	public void setHealth(int h){
		health=h;
	}
	public int getHealth(){
		return health;
	}
	public String getName(){
		return name;
	}
	public void setName(String n){
		name = n;
	}
	public int getLength(){
		return 70;
	}
	public int getWidth(){
		return 30;
	}
}
