public class Barricade {
	//a barricade that stops the enemies from advancing and reaching the MC
	//has a certain health span, once health is at 0, the barricade is destroyed
	final private int sx = 20, sy = 20; //size of the barricade
	final private int rectsx = 34, rectsy = 47;//rects is for MC and enemies size
	private double posX, posY;
	private int Health = 1000;
	Barricade(int x, int y){
		posX = x;
		posY = y;
	}
	
	//////////////getter/setters/////////////////////
	public int getX(){return (int)posX;}
	public int getY(){return (int)posY;}

	//centre position of the barricade
	public int getcx(){return (int)(posX + sx/2);}
	public int getcy(){return (int)(posY + sy/2);}
	
	public void setX(double x){posX = x;}
	public void setY(double y){posY = y;}
	public int getsx(){return sx;}
	public int getsy(){return sy;}
	public int getHealth(){return Health;}
	public void setHealth(int hp){Health = hp;}

	public boolean rectcollision(int x, int y){//collision with characters
		if (posX > x + rectsx || posX + sx < x || posY > y + rectsy || posY + sy < y)
			return false;
		return true;
	}
}
