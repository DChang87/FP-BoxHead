class MagicalBox {
	private int ITEM;
	private int X,Y;
	
	
	//change this
	private final int ITEMCOUNT=10, bsx = 28, bsy = 28;
	private int counter=200;
	//stop
	private String[] names = new String[ITEMCOUNT]; 
	BoxHead BH;
	public MagicalBox(int x, int y,BoxHead bh){
		X=x;
		Y=y;
		BH=bh;
	}
	public void countDown(){
		counter--;
	}
	public int getbsx(){
		return bsx;
	}
	public int getbsy(){
		return bsy;
	}
	public int getCounter(){
		return counter;
	}
	public int generateItem(){
		int[] allItems = new int[ITEMCOUNT];
		allItems[0]=0;//health
		names[0]="health";
		allItems[1]=2;//UZI
		names[1]="UZI";
		//no need for pistol since pistol is unlimited
		allItems[2]=3; //shotgun		
		names[2]="Shotgun";
		allItems[3]=4; //barrels
		names[3]="Barrels";
		allItems[4]=5;//grenade
		names[4]="Grenade";
		allItems[5]=6;//fake walls
		names[5]="Barricades";
		allItems[6]=7;//sentry gun
		names[6]="Sentry Gun";
		
		
		
		ITEM=allItems[(int) (Math.random()*(BH.magicalBoxAllowance-1))];
		//n is passed in as the number of the weapon which accounts for pistol
		//pistol is not accounted here so it must be taken away
		BH.game.setBoxString(names[ITEM]);
		return ITEM;
	}
	public int getX(){
		return X;
	}
	public int getY(){
		return Y;
	}
	public void setX(int x){
		X = x;
	}
	public void setY(int y){
		Y = y;
	}
	
	//generate the item AFTER the box has been collected
}
