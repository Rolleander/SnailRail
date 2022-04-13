import java.awt.Graphics;

public class Finish extends MapObjekt {

	public static boolean open = true;

	public Finish(int id, int dreh) {
		image = id;
		aktiv = true;
		drehung = dreh;
		climb = false;
		stand = false;
	}

	private void checkeAction(int x, int y) {
		if (x - 10 >= pos[0] && x + 10 <= pos[0] + 40) {
			if (y + 10 >= pos[1] && y - 10 <= pos[1] + 40) {
				if (open) {
					aktiv = false;
					Main.clearLevel();
				}
			}
		}
	}

	public static void setOpen(boolean b) {
		open = b;
	}

	public void paint(Graphics g, int x, int y) {
		if (aktiv) {
			if (aktiv && open) {

				checkeAction(x, y);
				switch (drehung) {
				case 0:
					g.drawImage(Main.mapBilder[image], pos[0] - x + 480, pos[1] - y + 280, null);
					break;
				case 1:
					g.drawImage(Main.mapBilder[image], pos[0] - x + 520, pos[1] - y + 280, -40, 40, null);
					break;
				case 2:
					g.drawImage(Main.mapBilder[image], pos[0] - x + 480, pos[1] - y + 320, 40, -40, null);
					break;
				case 3:
					g.drawImage(Main.mapBilder[image], pos[0] - x + 520, pos[1] - y + 320, -40, -40, null);
					break;
				}
			}
		}

	}

}
