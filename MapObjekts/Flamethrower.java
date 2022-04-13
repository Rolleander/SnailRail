import java.awt.Graphics;

public class Flamethrower extends MapObjekt {

	private int flame = 0;
	private int wait = 0;
	private int animation = 0;

	public Flamethrower(int dreh) {
		drehung = dreh;
		stand = true;
		climb = true;

	}

	public void paint(Graphics g, int x, int y) {
		int bild = animation % 2;
		if (drehung > 1) {// oben
			g.drawImage(Main.fire[bild], pos[0] - x + 480, pos[1] - y + 280 - flame, 40, flame, null);
		} else {// unten
			g.drawImage(Main.fire[bild], pos[0] - x + 480, pos[1] - y + 320 + flame, 40, -flame, null);
		}
		animation++;
		if (animation > 10) {
			animation = 0;
		}
		switch (drehung) {
		case 0:
			g.drawImage(Main.mapBilder[26], pos[0] - x + 480, pos[1] - y + 280, null);
			break;
		case 1:
			g.drawImage(Main.mapBilder[26], pos[0] - x + 520, pos[1] - y + 280, -40, 40, null);
			break;
		case 2:
			g.drawImage(Main.mapBilder[26], pos[0] - x + 480, pos[1] - y + 320, 40, -40, null);
			break;
		case 3:
			g.drawImage(Main.mapBilder[26], pos[0] - x + 520, pos[1] - y + 320, -40, -40, null);
			break;
		}

		if (wait < 500) {
			if (flame < 120) {
				flame += 8;
			} else {
				wait++;
			}
		} else {
			if (flame > 0) {
				flame -= 8;
			} else {
				wait++;
				if (wait > 700) {
					wait = 0;
				}
			}
		}

		if (flame > 20) {
			checkeAction(x, y);
		}
	}

	private void checkeAction(int x, int y) {
		if (x + 10 >= pos[0] && x - 10 <= pos[0] + 40) {
			if (drehung > 1) {
				if (y - 5 >= pos[1] - flame && y + 5 <= pos[1] + 40) {
					Main.schaden(false);
				}
			} else {
				if (y - 5 >= pos[1] + 40 && y + 5 <= pos[1] + 40 + flame) {
					Main.schaden(false);
				}
			}

		}
	}

}
