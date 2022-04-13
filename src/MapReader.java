import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MapReader {

	public MapReader() {
		readPathBlocks();
	}

	private int maph, mapw;
	private int start[] = new int[2];
	private boolean[][][] blocks;
	private MapObjekt[] mapobjekte = new MapObjekt[999];

	private boolean pathblock[][] = new boolean[100][2];// 0: fest 1:festhalten?

	public MapObjekt[] getMapObjekte() {
		int anzahl = 0;
		for (int i = 0; i < mapobjekte.length; i++) {
			if (mapobjekte[i] != null) {
				anzahl++;
			} else {
				break;
			}
		}
		MapObjekt objekte[] = new MapObjekt[anzahl];
		for (int i = 0; i < anzahl; i++) {
			objekte[i] = mapobjekte[i];
		}
		return objekte;
	}

	public int[] getDimension() {
		int i[] = { mapw, maph };
		return i;
	}

	public int[] getStartPosition() {
		return start;
	}

	public boolean[][][] getBlocks() {
		return blocks;
	}

	public int[][][] mapLesen(String title, boolean read) {

		int felder[][][] = null;
		try {
			String map = "", zeile;
			if (read) {
				InputStreamReader r = new InputStreamReader(getClass().getClassLoader().getResourceAsStream(title));
				BufferedReader reader = new BufferedReader(r);

				while ((zeile = reader.readLine()) != null) {
					map = map + zeile;
				}
			} else {
				map = title;
			}
			felder = zerschneiden(map);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		return felder;
	}

	private int[][][] zerschneiden(String map) {

		int objektzaehler = 0;
		String[] maps = map.split("\\$");

		mapw = Integer.parseInt(maps[0]);
		maph = Integer.parseInt(maps[1]);

		int fields[][][] = new int[mapw][maph][2];
		blocks = new boolean[mapw][maph][2];

		int x = 0, y = 0;

		for (int i = 2; i < maps.length; i++) {
			String[] info = maps[i].split("\\:");
			fields[x][y][0] = Integer.parseInt(info[0]);// Tile ID
			fields[x][y][1] = Integer.parseInt(info[1]);// Tile Drehung
			if (fields[x][y][0] == 84)// Start
			{
				start[0] = x;
				start[1] = y;
				fields[x][y][0] = 0;

			}
			blocks[x][y][0] = pathBlock(fields[x][y][0]);
			blocks[x][y][1] = climbBlock(fields[x][y][0]);
			MapObjekt m = getMapObjekt(fields[x][y][0], fields[x][y][1]);
			if (m != null) {
				m.setPos(x * 40, y * 40);

				m.setBlockGround(pathBlock(fields[x][y][0]), climbBlock(fields[x][y][0]));
				mapobjekte[objektzaehler] = m;
				objektzaehler++;
				fields[x][y][0] = 0;
				fields[x][y][1] = 0;
			}
			y++;
			if (y >= maph) {
				x++;
				y = 0;
			}
		}
		return fields;
	}

	private MapObjekt getMapObjekt(int id, int dreh) {
		MapObjekt o = null;
		if (id == 74) {
			o = new Coin();
		}
		if (id == 3) {
			o = new Brocken(id);
		}
		if (id == 2) {
			o = new Spikes(id, dreh);
		}
		if (id == 5) {
			o = new Spikes(id, dreh);
		}
		if (id == 53) {
			o = new Spikes(id, dreh);
		}
		if (id == 94) {
			o = new Spikes(id, dreh);
		}
		if (id == 44) {
			o = new Fall(id, dreh);
		}
		if (id == 54) {
			o = new Fall(id, dreh);
		}
		if (id == 63) {
			o = new Finish(id, dreh);
		}
		if (id == 73) {
			o = new Finish(id, dreh);
		}
		if (id == 26) {
			o = new Flamethrower(dreh);
		}
		if (id == 86) {
			o = new KnifeBlock(dreh);
		}
		if (id == 8 || id == 18) {
			o = new HiddenBlock(id);
		}
		if (id == 28) {
			o = new Steam(dreh);
		}
		if (id == 38) {
			o = new BreakStone(dreh);
		}
		if (id == 48) {
			o = new MetalGear();
		}
		if (id == 47) {
			o = new Lava(dreh);
		}
		if (id == 78) {
			o = new Eye();
		}
		if (id == 98) {
			o = new Enemy(10);// Boss
		}
		for (int i = 0; i < 10; i++) {
			if (id == 9 + i * 10) {
				o = new Enemy(i);
				break;
			}
		}
		return o;
	}

	public boolean pathBlock(int id) {
		return pathblock[id][0];
	}

	public boolean climbBlock(int id) {
		return pathblock[id][1];
	}

	private void readPathBlocks() {
		try {

			BufferedReader reader = new BufferedReader(
					new InputStreamReader(getClass().getClassLoader().getResourceAsStream("Tiles.txt")));
			String map = "", zeile;
			while ((zeile = reader.readLine()) != null) {
				map = map + zeile;
			}
			String[] maps = map.split("\\.");
			for (int i = 0; i < maps.length; i++) {
				int id = Integer.parseInt(maps[i]);
				switch (id) { // Begehbar|Festhalten
				case 0:
					pathblock[i][0] = false;
					pathblock[i][1] = false;
					break;// Nein Nein
				case 1:
					pathblock[i][0] = true;
					pathblock[i][1] = false;
					break;// Ja Neun
				case 2:
					pathblock[i][0] = false;
					pathblock[i][1] = true;
					break;// Nein Ja
				case 3:
					pathblock[i][0] = true;
					pathblock[i][1] = true;
					break;// Ja Ja
				}
			}

		} catch (IOException f) {
			System.err.println("Error 2: " + f);
		}
	}

}