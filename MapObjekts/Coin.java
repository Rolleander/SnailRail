import java.awt.Graphics;

public class Coin extends MapObjekt {

	private int animation = 0;
	private int vanish = 0;

	public Coin() {
		aktiv = true;
	}

	public boolean isCoin() {
		return true;
	}

	public void canVanish() {
		vanish = 1;
	}

	private void checkeAction(int x, int y) {
		if (x + 10 >= pos[0] && x - 10 <= pos[0] + 40) {
			if (y + 10 >= pos[1] && y - 10 <= pos[1] + 40) {
				aktiv = false;
				Main.sound.playSound(6);
				Main.player.collectCoin();
			}
		}
	}

	private int azaehler = 0;

	public void paint(Graphics g, int x, int y) {
		if (aktiv) {
			g.drawImage(Main.coin[animation], pos[0] - x + 480, pos[1] - y + 280, null);
			azaehler++;
			if (azaehler > 10) {
				azaehler = 0;
				animation++;
				if (animation > 4) {
					animation = 0;
				}
			}

			checkeAction(x, y);
		}

		if (vanish > 0) {
			vanish++;
			if (vanish > 1000) {
				aktiv = false;
			}

		}

	}

}
