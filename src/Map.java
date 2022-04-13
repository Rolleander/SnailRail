import java.awt.Graphics;

public class Map {

	private int tiles[][][];
	private int start[] = new int[2];
	private int dimension[] = new int[2];
	private boolean block[][][];
	private MapObjekt[] objekte;
	private MapObjekt[] coins = new MapObjekt[50];
	private static Shot[] shots = new Shot[30];
	private MapReader reader = new MapReader();
	private Background back = new Background();

	public Map() {
		for (int i = 0; i < coins.length; i++) {
			coins[i] = new MapObjekt();
		}
		for (int i = 0; i < shots.length; i++) {
			shots[i] = new Shot(0, 0);
		}
	}

	public void drawObjetcs(Graphics g, int x, int y) {
		for (int i = 0; i < objekte.length; i++) {
			objekte[i].paint(g, x, y);
			int coin = objekte[i].getCoins();
			if (coin > 0) {
				int cx = objekte[i].getPos()[0];
				int cy = objekte[i].getPos()[1];
				for (int h = 0; h < coins.length; h++) {
					if (coins[h].getAktiv() == false && coin > 0) {
						coins[h] = new Coin();
						coins[h].setPos(cx + (int) (Math.random() * 80 + 1) - 40, cy);
						coin--;
					}
				}
			}
			// schuss treffer (von player)
			for (int h = 0; h < shots.length; h++)// schüsse
			{
				if (shots[h].getAktiv() && shots[h].canHit() && shots[h].getAbsender() == 0) {
					if (objekte[i].hit(shots[h].getPos()[0], shots[h].getPos()[1])) {
						shots[h].hit();
					}
				}
			}
		}
		for (int i = 0; i < coins.length; i++)// gegner drop items
		{
			coins[i].paint(g, x, y);
		}
		for (int i = 0; i < shots.length; i++)// schüsse
		{
			shots[i].paint(g, x, y);
			// collision with player
			if (shots[i].getAbsender() > 0 && shots[i].getAktiv() && shots[i].canHit()) {
				if (Main.hitPlayer(shots[i], dimension[1])) {
					shots[i].hit();
				}
			}
			if (shots[i].canBounce()) {
				double sx = shots[i].getNextPos()[0];
				double sy = shots[i].getNextPos()[1];
				if (sx % 40 >= 5) {
					sx = sx / 40 + 1;
				} else {
					sx = sx / 40;
				}
				if (sy % 40 >= 5) {
					sy = sy / 40 + 1;
				} else {
					sy = sy / 40;
				}
				if (getTileBlock((int) sx, (int) sy) && shots[i].canHit()) {
					shots[i].hit();
				}
			}
		}
	}

	public void schuss(Shot s) {
		for (int i = 0; i < shots.length; i++)// schüsse
		{
			if (shots[i].getAktiv() == false) {
				shots[i] = s;
				break;
			}
		}
	}

	public static void schuss(int dir, int x, int y, boolean high) {
		for (int i = 0; i < shots.length; i++)// schüsse
		{
			if (shots[i].getAktiv() == false) {
				shots[i] = new Shot(0, 0);
				switch (dir) {
				case 0:
					shots[i].init(1, 0, high);
					break;
				case 1:
					shots[i].init(0, 1, high);
					break;
				case 2:
					shots[i].init(-1, 0, high);
					break;
				case 3:
					shots[i].init(0, -1, high);
					break;
				}
				shots[i].setPos(x, y);
				break;

			}
		}
	}

	public void drawBackground(Graphics g, int x, int y) {
		back.paint(g, x, y);
	}

	private boolean getTileBlock(int x, int y) {
		boolean b;
		if (x > -1 && y > -1 && x < dimension[0] && y < dimension[1]) {
			b = block[x][y][0];
		} else {
			b = true;
		}
		return b;
	}

	public boolean getBlock(int x, int y) {
		boolean b;
		if (x > -1 && y > -1 && x < dimension[0] && y < dimension[1]) {
			b = block[x][y][0];
		} else {
			b = true;
		}
		for (int i = 0; i < objekte.length; i++) {
			if (objekte[i].getAktiv() && objekte[i].isBlock()) {
				int ox = objekte[i].getPos()[0] / 40;
				int oy = objekte[i].getPos()[1] / 40;
				if (ox == x && oy == y) {
					b = objekte[i].getBlock();
					break;
				}
			}
		}
		return b;
	}

	public boolean getClimb(int x, int y) {
		boolean b;

		if (x > -1 && y > -1 && x < dimension[0] && y < dimension[1]) {
			b = block[x][y][1];
		} else {
			b = block[0][0][1];
		}
		for (int i = 0; i < objekte.length; i++) {
			if (objekte[i].getAktiv() && objekte[i].isBlock()) {
				int ox = objekte[i].getPos()[0] / 40;
				int oy = objekte[i].getPos()[1] / 40;
				if (ox == x && oy == y) {
					b = objekte[i].getClimb();
					break;
				}
			}
		}
		return b;
	}

	public int getMaxPoints() {
		int points = 0;
		for (int i = 0; i < objekte.length; i++) {
			if (objekte[i].isCoin()) {
				points += 10;
			}
			if (objekte[i].isEnemy() && objekte[i].canDestroy()) {
				points += 20;
			}
		}
		points = (int) ((double) points / (1.15 + (mapnr / 50)));
		return points;
	}

	private int mapnr;

	public void ladeMap(int nr) {
		String map = "map" + nr + ".txt";
		tiles = reader.mapLesen(map, true);
		block = reader.getBlocks();
		dimension = reader.getDimension();
		start = reader.getStartPosition();
		objekte = reader.getMapObjekte();
		back.init(nr - 1);
		createWaypoints();
		Finish.setOpen(true);
		if (nr == 25) {
			Finish.setOpen(false);
		}

		mapnr = nr;
	}

	public void ladeMap(String map) {

		tiles = reader.mapLesen(map, false);
		block = reader.getBlocks();
		dimension = reader.getDimension();
		start = reader.getStartPosition();
		objekte = reader.getMapObjekte();
		back.init(12);
		createWaypoints();
		Finish.setOpen(true);
		GameMenu.setOwnMapScore(getMaxPoints());
		mapnr = 26;
	}

	private void createWaypoints() {// Erstelle horizontale Wegpunkte der Gegner
		for (int i = 0; i < objekte.length; i++) {
			if (objekte[i].isEnemy()) {// Ist MapObjekt ein Gegner?
										// Wenn ja Wegpunkte berechnen:
				int x = objekte[i].getPos()[0] / 40;
				int y = objekte[i].getPos()[1] / 40;
				int way1 = x;
				// von x nach links laufen bis wand kommt
				for (int h = x; h > -1; h--) {
					way1 = h * 40 + 40;
					if (getBlock(h, y)) {// Weg blockiert
						break;
					}
					if (objekte[i].isFlyer() == false && !getBlock(h, y + 1)) {
						break;
					}
					if (h == 0) {
						way1 = h * 40;
					}
				}
				int way2 = x;
				// von x nach rechts laufen bis wand kommt
				for (int h = x; h < dimension[0]; h++) {
					way2 = h * 40 - 40;
					if (getBlock(h, y)) {// Weg blockiert
						break;
					}
					if (objekte[i].isFlyer() == false && !getBlock(h, y + 1)) {
						break;
					}
					if (h == dimension[0]) {
						way2 = h * 40;
					}
				}
				int way3 = y;
				// von y nach oben laufen bis wand kommt
				for (int h = y; h > -1; h--) {
					way3 = h * 40 + 40;
					if (getBlock(x, h)) {
						break;
					}
					if (h == 0) {
						way3 = h * 40;
					}
				}
				int way4 = y;
				// von y nach unten laufen bis wand kommt
				for (int h = y; h < dimension[1]; h++) {
					way4 = h * 40 - 40;
					if (getBlock(x, h)) {

						break;
					}
					if (h == dimension[1]) {
						way4 = h * 40;
					}
				}
				objekte[i].setWaypoints(way1, way2, way3, way4);
			}
		}
	}

	public int getTileID(int x, int y) {
		return tiles[x][y][0];
	}

	public int getTileDrehung(int x, int y) {
		return tiles[x][y][1];
	}

	public int[] getDimension() {
		return dimension;
	}

	public void setTile(int x, int y, int id) {
		tiles[x][y][0] = id;
	}

	public void setTileDrehung(int x, int y, int d) {
		tiles[x][y][1] = d;
	}

	public void setStartPosition(int x, int y) {
		start[0] = x;
		start[1] = y;
	}

	public int[] getStartPos() {
		return start;
	}
}
