import java.awt.Graphics;

public class Fall extends MapObjekt {

	public Fall(int id, int dreh) {
		image = id;
		aktiv = true;
		drehung = dreh;
	}

	private void checkeAction(int x, int y) {
		if (x + 10 >= pos[0] && x - 10 <= pos[0] + 40) {
			if (y + 5 >= pos[1] && y - 5 <= pos[1] + 40) {
				Main.sound.playSound(5);
				aktiv = false;
				Main.schaden(true);
			}
		}
	}

	public void paint(Graphics g, int x, int y) {
		if (aktiv) {

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
