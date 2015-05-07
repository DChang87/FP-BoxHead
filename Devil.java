import java.util.*;
class Devil {
	private int health = 5;
	private int posx,posy,angle; //angle is the angle in which the character is facing
	//-> is 0
	//<- is 180
	private int timeCount=0; //counter to see when to shoot a fireball
	private final int timeCountLim = 20;
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
	public void shoot(ArrayList<PosPair> fireballs){
		//this method is called if timeCounter is at a certain limit by the addTime() method;
		fireballs.add(new PosPair(posx,posy,angle)); 
		//will need another method to move the fireball, probably in the main method
		//will need to adjust the starting position of the fireball so it doesn't start on the devil (instead, a little bit ahead of him)
	}
	public void addTime(ArrayList<PosPair> fireballs){
		//call this method during the main method with a timer to call it at regular intervals
		timeCount++;
		if (timeCount==timeCountLim){
			timeCount=0;
			shoot(fireballs);
		}
	}
}
