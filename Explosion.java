public class Explosion {
	// a big BOOM
	//massive damage for every object on screen
	//INCLUDING the player
	//Be careful what you do with the barrels
	final private int sx = 100, sy = 100; //size of the barrel
	final private int rectsx = 34, rectsy = 47; //the size of the characters on the screen
	final private int dmg = 300, range = 100; //damage amount and range of the damage 
	private double posX, posY; //position of barrel
	private int ctime = 0;//time the explosion pic lasts
	Explosion(int x, int y){
		posX = x;
		posY = y;
	}
	//getter and setter
	public void setX(double x){posX = x;}
	public void setY(double y){posY = y;}
	public int getsx(){return sx;}
	public int getsy(){return sy;}
	public int getdmg(){return dmg;}
	public int getX(){return (int)posX;}
	public int getY(){return (int)posY;}
	public int getctime(){return ctime;}
	public int getrange(){return range;}
	
	public void incrementtime(){ctime++;}//the timer for how long the explosion can stay on the screen
	
}
