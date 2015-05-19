public class PosPair{
    private final int ANGLE, Type;
    private double X,Y;
    public PosPair(int x,int y,int deg,int type)
    {
        X=x;
        Y=y;
        ANGLE = deg;
        Type = type;
    }
    public int getX(){
    	return (int)X;
    }
    public int getY(){
    	return (int)Y;
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
