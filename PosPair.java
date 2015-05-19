public class PosPair{
    private final int X,Y,ANGLE, Type;
    public PosPair(int x,int y,int deg,int type)
    {
        X=x;
        Y=y;
        ANGLE = deg;
        Type = type;
    }
    public int getX(){
    	return X;
    }
    public int getY(){
    	return Y;
    }
    public int getANGLE(){
    	return ANGLE;
    }
    public int getTYPE(){
    	return Type;
    }
}
