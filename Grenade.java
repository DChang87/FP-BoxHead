public class Grenade{
	final int dmg = 1000,dmgrange=50; //damage and the range of damage that the grenade could do
	public double posX,posY; //position of the grenade
	public int counter,explodeCount;
	//counter = how much time the grenade has before explosion
	//explodeCount = how long the explosion sprite must blit for
	public BoxHead BH;
	public Grenade(int x,int y,BoxHead bh){
		posX=x;
		posY=y;
		counter=100;
		bh=BH;
	}
	
	//getter and setter methods
	public int getX(){return (int)posX;}
	public int getY(){return (int)posY;}
	public int getdmg(){return dmg;}
	public void setX(int n){posX=n;}
	public void setY(int n){posY = n;}
	public int getCounter(){return counter;}
	public int getdmgrange(){return  dmgrange;}
	
	//count down methods
	public void countDown(){
		//count down the grenade counter
		//at the end of the grenade counter, set the explodeCount to full 
		//since it is time that the grenade explode and blit the explosion sprite
		counter--;
		if (counter==0){
			explodeCount=10;
		}
	}
	public void explodeCountDown(){explodeCount--;} //count down the explode counter
	
}
