public  class Barrel { 
	// An Over-powered barricade that can take sustain any amount of damage, but explodes
	// when hit by a fireball or a bullet
	// A weapon of mass destruction
	
	final int dmg = 100;
	private int Health = 100, sx = 20, sy = 50, rectsx = 34, rectsy = 47, range = 100;
	public double posX, posY;
	Barrel(int x, int y){
		posX = x;
		posY = y;
	}
	public int getX(){
		return (int)posX;
	}
	public int getY(){
		return (int)posY;
	}
	public int getcx(){// center position
		return (int)(posX + sx/2);
	}
	public int getcy(){
		return (int)(posY + sy/2);
	}
	public int getsx(){ //x size - width
		return sx;
	}
	public int getsy(){// y size - height
		return sy;
	}
	public void setX(double x){
		posX = x;
	}
	public void setY(double y){
		posY = y;
	}
	public int getHealth(){
		return Health;
	}
	public void setHealth(int hp){
		Health = hp;
	}
	public int getdmg(){
		return dmg;
	}
	public int getrange(){ //explosion range
		return range;
	}
	public boolean rectcollision(int x, int y){ //whether barrel intersects the MC or Enemy with Top Left
		if (posX > x + rectsx || posX + sx < x || posY > y + rectsy || posY + sy < y)//position at (x,y)
			return false;
		return true;
	}
}
