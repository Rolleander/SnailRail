import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class HiddenBlock extends MapObjekt {

	private float hidden;

	public HiddenBlock(int im) {
		image = im;
		aktiv = true;
		hidden = 1;
	}

	private int out = 0;

	public void paint(Graphics g, int x, int y) {
		hidden -= 0.005;
		if (hidden < 0) {
			hidden = 0;
			out++;
			if (out > 100) {
				hidden = 1;
				out = 0;
			}
			climb = false;
			stand = false;
		} else {
			if (image == 8) {
				climb = false;
				stand = true;
			} else {
				climb = true;
				stand = true;
			}
		}
		Graphics2D g2d = (Graphics2D) g;
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, hidden));
		g2d.drawImage(Main.mapBilder[image], pos[0] - x + 480, pos[1] - y + 280, null);
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
	}

}
