public class PosPair{
    private final int ANGLE, Type, dmg = 15;
    private double X,Y, origX, origY;
    private int[] typedist = new int[30];
    
    public PosPair(int x,int y,int deg,int type)
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
