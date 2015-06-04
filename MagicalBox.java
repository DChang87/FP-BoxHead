
class MagicalBox {
	int ITEM;
	 
	//PistolP = Pistol plus, double damage, 1.1
	//UZIP = UZI rapid fire, 2.1
	//UZIPP = UZI double ammo, 2.2
	int allowance=2;//allowance for the current state (as the user kills more enemies, the allowance increases and increases the diversity
	//of the items in the box
	int X,Y;
	
	
	//change this
	final int ITEMCOUNT=10;
	//stop
	
	
	public MagicalBox(int x, int y){
		X=x;
		Y=y;
	}
	public int generateItem(){
		int[] allItems = new int[ITEMCOUNT];
		allItems[0]=0;//health
		allItems[1]=2;//UZI
		//no need for pistol since pistol is unlimited
		allItems[2]=3; //shotgun

		
		
		
		//add this
		allItems[3]=4; //barrels
		allItems[4]=5;//grenade
		allItems[5]=6;//fake walls
		//stop
		
		
		
		ITEM=allItems[(int) (Math.random()*allowance)];
		return ITEM;
	}
	public int getX(){
		return X;
	}
	public int getY(){
		return Y;
	}
	//generate the item AFTER the box has been collected
}
