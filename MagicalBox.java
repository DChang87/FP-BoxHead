public class MagicalBox {
	//leo was against the idea of naming the boxes "MagicalBox" instead of "SuppliesBox"
	//but since these boxes are magical and allow the character to stay alive
	//it was named MagicalBox
	private int ITEM; //the item that is passed back
	private int X,Y; //position of the box
	private final int ITEMCOUNT=10, bsx = 28, bsy = 28; //the number of items in the box and the dimensions of the box
	private int counter=200; //counter for how long the box lasts before disappearing
	private String[] names = new String[ITEMCOUNT]; //names of all the items in the box
	public int[] allItems = new int[ITEMCOUNT]; //the number of item in the box with its correlation to the weapon number
	//this is needed since pistol is not an item in the box so there is one less item count than weapon count
	BoxHead BH;
	public MagicalBox(int x, int y,BoxHead bh){
		X=x;
		Y=y;
		BH=bh;
		allItems[0]=0;//health
		names[0]="Health";
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
	}
	public void countDown(){counter--;} 
	public int generateItem(){
		ITEM=allItems[(int) (Math.random()*(BH.magicalBoxAllowance-1))]; //randomly generate an item
		//BH.magicalBoxAllowance is passed in as the number of the weapon, which accounts for pistol=1
		//pistol is not accounted here so 1 must be taken away
		BH.game.setBoxString(names[ITEM]);

		if (ITEM==0){
			//if the item is health then let the character have it
			return ITEM;
		}
		else{
			//if it is not then randomly generate again since we
			//need to increase more health or character dies too early
			int [] rand = {ITEM,0,ITEM};
			int n = (int)(Math.random()*3);
			if (rand[n]==0){
				return 0;
				//since pistol is at 1 not 0, health=0 is not effected so there is no need to add 1 back on
			}
			else{
				return rand[n]+1;
				//since 1 was taken away earlier, it is added back on when the value is returning
			}
			
		}
		
	}
	
	//getter and setters
	public int getX(){return X;}
	public int getY(){return Y;}
	public void setX(int x){X = x;}
	public void setY(int y){Y = y;} 
	public int getbsx(){return bsx;} 
	public int getbsy(){return bsy;} 
	public int getCounter(){return counter;}
}
