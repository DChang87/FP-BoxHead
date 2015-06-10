public class Grenade{
	final int dmg = 1000,dmgrange=50;
	public double posX,posY;
	public int counter,explodeCount;
	public BoxHead BH;
	public Grenade(int x,int y,BoxHead bh){
		posX=x;
		posY=y;
		counter=100;
		bh=BH;
	}
	public int getX(){
		return (int)posX;
	}
	public int getY(){
		return (int)posY;
	}
	public int getdmg(){
		return dmg;
	}
	public void setX(int n){
		posX=n;
	}
	public void setY(int n){
		posY = n;
	}
	public void countDown(){
		counter--;
		if (counter==0){
			explodeCount=10;
		}
	}
	public void explodeCountDown(){
		explodeCount--;
	}
	public int getCounter(){
		return counter;
	}
	public int getdmgrange(){
		return  dmgrange;
	}
}
