public class PosPair{
	//The place where fireballs, bullets and very dangerous weapons are stored
    private final int ANGLE, Type, dmg = 15;
    private double X,Y, origX, origY;
    //				x, 		y,	degree bullet/fireball will travel
    public PosPair(int x,int y,int deg,int type)//type is type of bullet or fireball
    {
        X=x;
        Y=y;
        origX = x;
        origY = y;
        ANGLE = deg;
        Type = type;
    }
    public int getX(){
    	return (int)X;
    }
    public int getY(){
    	return (int)Y;
    }
    public int getdmg(){
    	return dmg;
    }
    public int getType(){
    	return Type;
    }
    public int getorigX(){
    	return (int)origX;
    }
    public int getorigY(){
    	return (int)origY;
    }
    public double getDX(){
    	return X;
    }
    public double getDY(){
    	return Y;
    }
    public int getANGLE(){
    	return ANGLE;
    }
    public int getTYPE(){
    	return Type;
    }
    public void setPos(double x, double y){
    	X = x;
    	Y = y;
    }
}
