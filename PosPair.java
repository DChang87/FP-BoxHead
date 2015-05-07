public class PosPair{
    private final int X,Y,ANGLE;

    public PosPair(int x,int y,int deg)
    {
        X=x;
        Y=y;
        ANGLE = deg;
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
    
}
