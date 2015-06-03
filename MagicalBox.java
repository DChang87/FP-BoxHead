
class MagicalBox {
	int ITEM;
	 
	//PistolP = Pistol plus, double damage, 1.1
	//UZIP = UZI rapid fire, 2.1
	//UZIPP = UZI double ammo, 2.2
	int allowance=1;//allowance for the current state (as the user kills more enemies, the allowance increases and increases the diversity
	//of the items in the box
	int X,Y;
	public MagicalBox(int x, int y){
		X=x;
		Y=y;
	}
	public int generateItem(){
		ITEM=(int) (Math.random()*allowance);
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
