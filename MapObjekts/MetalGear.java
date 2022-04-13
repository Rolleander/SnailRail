import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class MetalGear extends MapObjekt {

	public MetalGear() {
		aktiv = true;
	}

	private int drehung = 0;

	private void checkeAction(int x, int y) {
		if (x + 10 >= pos[0] && x - 10 <= pos[0] + 40) {
			if (y - 5 >= pos[1] && y + 5 <= pos[1] + 40) {
				Main.schaden(false);
			}
		}
	}

	public void paint(Graphics g, int x, int y) {
		if (aktiv) {

			checkeAction(x, y);
			drehung++;
			Graphics2D g2d = (Graphics2D) g;
			AffineTransform affineTransform = new AffineTransform();
			affineTransform.setToTranslation(pos[0] - x + 480, pos[1] - y + 280);
			affineTransform.rotate(Math.toRadians(drehung), 20, 20);
			g2d.drawImage(Main.mapBilder[48], affineTransform, null);
		}
	}

}
