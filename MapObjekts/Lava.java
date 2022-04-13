import java.awt.Graphics;

public class Lava extends MapObjekt {

	private boolean fireing = false;
	private int animation = 0;

	public Lava(int dreh) {
		aktiv = true;
		if (dreh > 1) {
			fireing = true;
		}

	}

	private void checkeAction(int x, int y) {
		if (x + 10 >= pos[0] && x - 10 <= pos[0] + 40) {
			if (y - 5 >= pos[1] && y + 5 <= pos[1] + 40) {
				Main.sound.playSound(5);
				aktiv = false;
				Main.schaden(true);
			}
		}
	}

	public void paint(Graphics g, int x, int y) {
		if (aktiv) {

			checkeAction(x, y);
			animation++;
			if (animation > 29) {
				animation = 0;
			}
			g.drawImage(Main.lava[animation / 10], pos[0] - x + 480, pos[1] - y + 280, null);

		}
	}
}
