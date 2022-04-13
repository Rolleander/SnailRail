import java.awt.Graphics;

public class BreakStone extends MapObjekt {

	private int destroy;
	private boolean fall = false;

	public BreakStone(int dreh) {
		drehung = dreh;
		aktiv = true;
		destroy = 0;
	}

	private void checkeAction(int x, int y) {
		if (x + 10 >= pos[0] && x - 10 <= pos[0] + 40) {
			if (y + 35 >= pos[1] && y - 45 <= pos[1]) {
				fall = true;

			}
		}
	}

	public void paint(Graphics g, int x, int y) {
		if (stand) {
			if (fall) {
				destroy++;
				if (destroy > 100) {
					stand = false;

				}
			} else {
				checkeAction(x, y);
			}
			switch (drehung) {
			case 0:
				g.drawImage(Main.mapBilder[38], pos[0] - x + 480, pos[1] - y + 280, null);
				break;
			case 1:
				g.drawImage(Main.mapBilder[38], pos[0] - x + 520, pos[1] - y + 280, -40, 40, null);
				break;
			case 2:
				g.drawImage(Main.mapBilder[38], pos[0] - x + 480, pos[1] - y + 320, 40, -40, null);
				break;
			case 3:
				g.drawImage(Main.mapBilder[38], pos[0] - x + 520, pos[1] - y + 320, -40, -40, null);
				break;
			}
		}
	}

}
