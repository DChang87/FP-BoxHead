
class MagicalBox {
	int ITEM;
	final int HEALTH=0,PISTOL=1,UZI=2,PISTOLP=11,SHOTGUN=3,UZIP=21,BARREL = 4,UZIPP=22,GRENADE= 5, FAKEWALLS=6; 
	//PistolP = Pistol plus, double damage, 1.1
	//UZIP = UZI rapid fire, 2.1
	//UZIPP = UZI double ammo, 2.2
	int count=0;
	int X,Y;
	public MagicalBox(int x, int y){
		generateItem();
		X=x;
		Y=y;
	}
	public void generateItem(){
		ITEM=count;
	}
	public int getX(){
		return X;
	}
	public int getY(){
		return Y;
	}
	//generate the item AFTER the box has been collected
}
