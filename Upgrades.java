class Upgrades {
	public int[] allUpgradesNum = new int[26];
	private final int PISTOL = 1, UZI = 2, SHOTGUN = 3, BARREL = 4,
			GRENADE = 5, BARRICADE = 6,SENTRY=7;
	BoxHead BH;
	private String[] upgradeStrings = new String[26];
	public Upgrades(BoxHead b) {
		BH = b;
		addUpgrades();
		addUpgradeNames();
	}
	public void addUpgrades() {
		allUpgradesNum[0] = 3; // pistol fast fire
		allUpgradesNum[1] = 5; // new weapon UZI
		allUpgradesNum[2] = 8; // pistol double damage
		allUpgradesNum[3] = 10; // new weapon shotgun
		allUpgradesNum[4] = 13; // uzi rapid fire
		allUpgradesNum[5] = 15; // new weapon barrel
		allUpgradesNum[6] = 17; // uzi double ammo
		allUpgradesNum[7] = 18; // shotgun fast fire
		allUpgradesNum[8] = 20; // new weapon grenade
		allUpgradesNum[9] = 21; // shotgun double ammo
		allUpgradesNum[10] = 23; // uzi long shot
		allUpgradesNum[11] = 26; // barrel double ammo
		allUpgradesNum[12] = 30; // new weapon fake wall
		allUpgradesNum[13] = 31; // shot gun wide shot
		allUpgradesNum[14] = 35; // shotgun long shot
		allUpgradesNum[15] = 36; // barrel quad ammo
		allUpgradesNum[16] = 37; // fake walls double ammo
		allUpgradesNum[17] = 39; // uzi quad ammo
		allUpgradesNum[18] = 41; // shotgun quad ammo
		allUpgradesNum[19] = 42; // grenade double ammo
		allUpgradesNum[20] = 43; // shotgun rapid fire
		allUpgradesNum[21] = 48; // uzi double damage
		allUpgradesNum[22] = 51; // shotgun wider shot
		allUpgradesNum[23] = 52; // grenade quad ammo
		allUpgradesNum[24] = 53; // fake walls quad ammo
		allUpgradesNum[25]=75;
	}
	public void addUpgradeNames(){
		upgradeStrings[0] = "pistol fast fire";
		upgradeStrings[1] = "new weapon UZI";
		upgradeStrings[2] = "pistol double damage";
		upgradeStrings[3] = "new weapon shotgun";
		upgradeStrings[4] = "uzi rapid fire";
		upgradeStrings[5] = "new weapon barrel";
		upgradeStrings[6] = "uzi double ammo";
		upgradeStrings[7] = "shotgun fast fire";
		upgradeStrings[8] = "new weapon grenade";
		upgradeStrings[9] ="shotgun double ammo";
		upgradeStrings[10] ="uzi long shot";
		upgradeStrings[11] = "barrel double ammo";
		upgradeStrings[12] = "new weapon fake wall";
		upgradeStrings[13] = "shot gun wide shot";
		upgradeStrings[14] = "shotgun long shot";
		upgradeStrings[15] = "barrel quad ammo";
		upgradeStrings[16] = "fake walls double ammo";
		upgradeStrings[17] = "uzi quad ammo";
		upgradeStrings[18] = "hotgun quad ammo";
		upgradeStrings[19] = "grenade double ammo";
		upgradeStrings[20] = "shotgun rapid fire";
		upgradeStrings[21] = "uzi double damage";
		upgradeStrings[22] = "shotgun wider shot";
		upgradeStrings[23] = "grenade quad ammo";
		upgradeStrings[24] = "fake walls quad ammo";
		upgradeStrings[25]="new weapon sentry gun";
	}
	public String getUpgradeString(int n){
		return upgradeStrings[n];
	}
	public void getUpgrade(int n) {
		BH.game.fullUpgradeCountDown();
		System.out.println("n"+ n);
		if (n == 3) {
			BH.game.setUpgradeString("Pistol Fast Fire");
			System.out.println("pistol fast fire");
			BH.mc.setConsecutiveShoot(PISTOL);
		} 
		else if (n == 5) {
			// new weapon UZI
			System.out.println("UZI UZI");
			BH.game.setUpgradeString("New Weapon: UZI");
			BH.mc.addAmmo(UZI);
			BH.magicalBoxAllowance=UZI;
		} 
		else if (n == 8) {
			// pistol double damage
			BH.mc.setDmg(PISTOL, 10);
			BH.game.setUpgradeString("Pistol Double Damage");
		} 
		else if (n == 10) {
			// new weapon shotgun
			System.out.println("shotgun SHOTGUN");
			BH.game.setUpgradeString("New Weapon: Shotgun");
			BH.mc.addAmmo(SHOTGUN);
			BH.magicalBoxAllowance=SHOTGUN;
		} 
		else if (n == 13) {
			// uzi rapid fire
			BH.mc.setConsecutiveShoot(UZI);
			BH.game.setUpgradeString("UZI Rapid Fire");
		} 
		else if (n == 15) {
			// new weapon barrel
			BH.game.setUpgradeString("New Weapon: Barrel");
			BH.mc.addAmmo(BARREL);
			BH.magicalBoxAllowance=BARREL;
		} 
		else if (n == 17) {
			// uzi double ammo
			BH.mc.setMaxAmmo(UZI, BH.mc.getMaxAmmo(UZI)*2);
			BH.game.setUpgradeString("UZI Double Ammo");
		} 
		else if (n == 18) {
			// shotgun fast fire
			BH.mc.setConsecutiveShoot(SHOTGUN);
			BH.game.setUpgradeString("Shotgun Fast Fire");
		} 
		else if (n == 20) {
			// new weapon grenadE
			BH.game.setUpgradeString("New Weapon: Grenade");
			BH.mc.addAmmo(GRENADE);
			BH.magicalBoxAllowance=GRENADE;
		} 
		else if (n == 21) {
			// shot gun double ammo
			BH.mc.setMaxAmmo(SHOTGUN, BH.mc.getMaxAmmo(SHOTGUN)*2);
			BH.game.setUpgradeString("Shotgun Double Ammo");
			//NOT DONE
		} 
		else if (n == 23) {
			// uzi long shot
			BH.mc.setDist(UZI, 700);
			BH.game.setUpgradeString("UZI Long Shot");
			//NOT DONE
		} 
		else if (n == 26) {
			// barrel double ammo
			BH.mc.setMaxAmmo(BARREL, BH.mc.getMaxAmmo(BARREL)*2);
			BH.game.setUpgradeString("Barrel Double Ammo");
			
		} 
		else if (n == 30) {
			// new weapon fakewall
			BH.game.setUpgradeString("New Weapon: Barricade");
			BH.mc.addAmmo(BARRICADE);
			BH.magicalBoxAllowance=BARRICADE;
		} 
		else if (n == 31) {
			// shotgun wide shoot
			BH.mc.setSGW(1);
			BH.game.setUpgradeString("Shotgun Wide Shoot");
		} 
		else if (n == 35) {
			// shotgun long shot
			BH.mc.setDist(SHOTGUN, 400);
			BH.game.setUpgradeString("Shotgun Long Shot");
		} 
		else if (n == 36) {
			// barrel quad ammo
			BH.mc.setMaxAmmo(BARREL,BH.mc.getMaxAmmo(BARREL));
			BH.game.setUpgradeString("Barrel Quad Ammo");
		} 
		else if (n == 37) {
			// fake walls double ammo
			BH.mc.setMaxAmmo(BARRICADE,BH.mc.getMaxAmmo(BARRICADE));
			BH.game.setUpgradeString("Barricade Double Ammo");
		} 
		else if (n == 39) {
			// uzi quad ammo
			BH.mc.setMaxAmmo(UZI,BH.mc.getMaxAmmo(UZI)*4);
			BH.game.setUpgradeString("UZI Quad Ammo");
		} 
		else if (n == 41) {
			// shotgun quad ammo
			BH.mc.setMaxAmmo(SHOTGUN,BH.mc.getMaxAmmo(SHOTGUN)*4);
			BH.game.setUpgradeString("Shotgun Quad Ammo");
			
		} 
		else if (n == 42) {
			// grenade double ammo
			BH.mc.setMaxAmmo(GRENADE,BH.mc.getMaxAmmo(GRENADE)*2);
			BH.game.setUpgradeString("Grenade Double Ammo");
			
		} 
		else if (n == 43) {
			// shotgun rapid fire
			BH.mc.setConsecutiveShoot(SHOTGUN);
			BH.game.setUpgradeString("Shotgun Rapid Fire");
		}
		else if (n == 48) {
			// uzi double damage
			BH.mc.setDmg(UZI, 30);
			BH.game.setUpgradeString("UZI Double Damage");
		} 
		else if (n == 51) {
			// shotgun wider shot
			BH.mc.setSGW(2);
			BH.game.setUpgradeString("Shotgun Wider Shot");
		} 
		else if (n == 52) {
			// grenade quad ammo
			BH.mc.setMaxAmmo(GRENADE, BH.mc.getMaxAmmo(GRENADE)*4);
			BH.game.setUpgradeString("Grenade Quad Ammo");
		} 
		else if (n == 53) {
			// fake walls quad ammo
			BH.mc.setMaxAmmo(BARRICADE, BH.mc.getMaxAmmo(BARRICADE)*4);
			BH.game.setUpgradeString("Barricade Quad Ammo");
		}
		else if (n==75){
			BH.mc.addAmmo(SENTRY);
			BH.game.setUpgradeString("New Weapon: Sentry Gun");
			BH.magicalBoxAllowance=SENTRY;
		}
	}
}
