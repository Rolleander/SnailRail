import java.awt.Color;
import java.awt.Graphics;

public class Steam extends MapObjekt {

	public Steam(int dreh) {
		drehung = dreh;
	}

	public void paint(Graphics g, int x, int y) {
		switch (drehung) {
		case 0:
			g.drawImage(Main.mapBilder[28], pos[0] - x + 480, pos[1] - y + 280, null);
			break;
		case 1:
			g.drawImage(Main.mapBilder[28], pos[0] - x + 520, pos[1] - y + 280, -40, 40, null);
			break;
		case 2:
			g.drawImage(Main.mapBilder[28], pos[0] - x + 480, pos[1] - y + 320, 40, -40, null);
			break;
		case 3:
			g.drawImage(Main.mapBilder[28], pos[0] - x + 520, pos[1] - y + 320, -40, -40, null);
			break;
		}
		if (drehung < 2) {
			for (int i = 0; i < 10; i++) {
				g.setColor(new Color(100, 100, (int) (Math.random() * 100 + 1) + 155, (int) (Math.random() * 50 + 1)));
				g.fillRect(pos[0] - x + 480 + i * 4, pos[1] - y + 280 - 120, 4, 120);
			}
		} else {
			for (int i = 0; i < 10; i++) {
				g.setColor(new Color(100, 100, (int) (Math.random() * 100 + 1) + 155, (int) (Math.random() * 50 + 1)));
				g.fillRect(pos[0] - x + 480 + i * 4, pos[1] - y + 280 + 40, 4, 120);
			}
		}
		if (x > pos[0] && x < pos[0] + 40) {// in strahl x-bereich
			if (drehung < 2) {// nach oben strahl
				if (y + 120 > pos[1] && y < pos[1]) {
					Main.player.fly(0);
				}
			} else {// nach unten strahl
				if (y < pos[1] + 120 && y > pos[1]) {
					Main.player.fly(1);
				}
			}

		}

	}

}
