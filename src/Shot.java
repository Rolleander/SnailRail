import java.awt.Graphics;

public class Shot {

	private double pos[] = new double[2];
	private double speed[] = new double[2];
	private boolean aktiv, hit = true, high = false;
	private int treffer = 0;
	private int absender;
	private boolean direct = false; // 100% target
	private int design;

	public Shot(int abs, int des) {
		absender = abs;
		design = des;
	}

	public int getAbsender() {
		return absender;
	}

	public void init(double speedx, double speedy, boolean hig) {
		speed[0] = speedx * 5;
		speed[1] = speedy * 5;
		if (hig) {

			speed[1] -= 3.5;
		}

		high = hig;

		aktiv = true;
		hit = true;
	}

	public int[] getPos() {
		int[] intpos = new int[2];
		intpos[0] = (int) pos[0] - 500;
		intpos[1] = (int) pos[1] - 300;
		return intpos;
	}

	public void setPos(int x, int y) {
		pos[0] = x;
		pos[1] = y;
	}

	public boolean getAktiv() {
		return aktiv;
	}

	public void hit() {
		treffer = 1;
		hit = false;
	}

	public boolean canHit() {
		return hit;
	}

	public void setDirect(boolean b) {
		direct = b;
	}

	public void paint(Graphics g, int sx, int sy) {
		if (aktiv) {
			if (treffer == 0) {
				pos[0] += speed[0];
				pos[1] += speed[1];

				if (direct == false) {
					if (high) {
						speed[1] += 0.1;
					} else {
						speed[1] += 0.05;
					}
				}
				g.drawImage(Main.shot[design][0], (int) pos[0] - sx - 20, (int) pos[1] - sy - 20, null);
			} else {

				treffer++;
				g.drawImage(Main.shot[design][treffer / 7], (int) pos[0] - sx - 20, (int) pos[1] - sy - 20, null);
				if (treffer > 33) {
					aktiv = false;
					treffer = 0;
				}

			}
		}
		if (time > 0) {
			time--;

		} else {
			bounce = true;
		}
	}

	private boolean bounce = true;

	public boolean canBounce() {
		return bounce;
	}

	private int time = 0;

	public void setBounceBlock(int t) {
		time = t;
		bounce = false;
	}

	public int[] getNextPos() {
		int[] intpos = new int[2];
		intpos[0] = (int) ((int) pos[0] + speed[0] + -500);
		intpos[1] = (int) ((int) pos[1] + speed[1] + 0.1 - 300);

		return intpos;
	}
}
