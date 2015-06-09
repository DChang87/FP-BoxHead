class Upgrades {
	public int[] allUpgradesNum = new int[30];
	private final int PISTOL = 1, UZI = 2, SHOTGUN = 3, BARREL = 4,
			GRENADE = 5, FAKEWALLS = 6;
	BoxHead BH;

	public Upgrades(BoxHead b) {
		BH = b;
		addUpgrades();
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
		allUpgradesNum[14] = 32; // barrel big bang
		allUpgradesNum[15] = 33; // grenade cluster explode
		allUpgradesNum[16] = 35; // shotgun long shot
		allUpgradesNum[17] = 36; // barrel quad ammo
		allUpgradesNum[18] = 37; // fake walls double ammo
		allUpgradesNum[19] = 39; // uzi quad ammo
		allUpgradesNum[20] = 41; // shotgun quad ammo
		allUpgradesNum[21] = 42; // grenade double ammo
		allUpgradesNum[22] = 43; // shotgun rapid fire
		allUpgradesNum[23] = 44; // barrel bigger bang
		allUpgradesNum[24] = 45; // grenade bigger bang
		allUpgradesNum[25] = 48; // uzi double damage
		allUpgradesNum[26] = 51; // shotgun wider shot
		allUpgradesNum[27] = 52; // grenade quad ammo
		allUpgradesNum[25] = 53; // fake walls quad ammo
	}

	public void getUpgrade(int n) {
		BH.game.fullUpgradeCountDown();
		System.out.println("n"+ n);
		if (n == 3) {
			System.out.println("pistol fast fire");
			// pistol fast fire
		} 
		else if (n == 5) {
			// new weapon UZI
			System.out.println("UZI UZI");
			BH.game.setUpgradeString("New Weapon: UZI");
			BH.mc.addAmmo(UZI);
		} 
		else if (n == 8) {
			// pistol double damage
		} 
		else if (n == 10) {
			// new weapon shotgun
			System.out.println("shotgun SHOTGUN");
			BH.game.setUpgradeString("New Weapon: Shotgun");
			BH.mc.addAmmo(SHOTGUN);
		} 
		else if (n == 13) {
			// uzi rapid fire
		} 
		else if (n == 15) {
			// new weapon barrel
		} 
		else if (n == 17) {
			// uzi double ammo
		} 
		else if (n == 18) {
			// shotgun fast fire
		} 
		else if (n == 20) {
			// new weapon grenade
			BH.game.setUpgradeString("New Weapon: Grenade");
			BH.mc.addAmmo(GRENADE);

		} 
		else if (n == 21) {
			// shot gun double ammo
		} 
		else if (n == 23) {
			// uzi long shot
		} 
		else if (n == 26) {
			// barrel double ammo
		} 
		else if (n == 30) {
			// new weapon fakewall
			BH.game.setUpgradeString("New Weapon: Fake Wall");
			BH.mc.addAmmo(FAKEWALLS);
		} 
		else if (n == 31) {
			// shotgun wide shoot
		} 
		else if (n == 32) {
			// barrel big bang
		} 
		else if (n == 33) {
			// grenade cluster explode
		} 
		else if (n == 35) {
			// shotgun long shot
		} 
		else if (n == 36) {
			// barrel quad ammo
		} 
		else if (n == 37) {
			// fake walls double ammo
		} 
		else if (n == 39) {
			// uzi quad ammo
		} 
		else if (n == 41) {
			// shotgun quad ammo
		} 
		else if (n == 42) {
			// grenade double ammo
		} 
		else if (n == 43) {
			// shotgun rapid fire
		} 
		else if (n == 44) {
			// barrel bigger bang
		} 
		else if (n == 45) {
			// grenade bigger bang
		} 
		else if (n == 48) {
			// uzi double damage
		} 
		else if (n == 51) {
			// shotgun wider shot
		} 
		else if (n == 52) {
			// grenade quad ammo
		} 
		else if (n == 53) {
			// fake walls quad ammo
		}

	}
}
