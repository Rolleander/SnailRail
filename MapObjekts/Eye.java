import java.awt.Graphics;
import java.awt.Point;

public class Eye extends MapObjekt {

	public Eye() {
		aktiv = true;
		stand = true;
		climb = true;
	}

	public void paint(Graphics g, int x, int y) {
		if (aktiv) {

			g.drawImage(Main.mapBilder[78], pos[0] - x + 480, pos[1] - y + 280, null);
			if (schussz > 150 && schussz < 190) {
				g.drawImage(Main.shot[1][0], pos[0] - x + 480, pos[1] - y + 280, null);
			}
			checkeAction(x, y);
		}
	}

	private int schussz = 0;

	private void checkeAction(int x, int y) {
		if (inRadius(pos[0] + 20, x, pos[1] + 20, y, 200)) {
			// schiessen
			schussz++;
			if (schussz >= 190) {// schiessen
				schussz = 0;
				Shot s = new Shot(1, 1);
				double xs = getMovement(pos[0] + 20, pos[1] + 20, x, y)[0] / 2;
				double ys = getMovement(pos[0] + 20, pos[1] + 20, x, y)[1] / 2;
				s.init(xs, ys, false);
				s.setPos(pos[0] + 500, pos[1] + 300);
				s.setBounceBlock(4000);
				s.setDirect(true);
				Main.shotEnemy(s);

			}
		} else {
			schussz = 150;
		}
	}

}
