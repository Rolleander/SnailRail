import java.awt.Graphics;

public class Boss {

	public Boss() {

	}

	private int waypoints[] = new int[4];
	private double pos[] = new double[2];
	private double speed[] = new double[2];
	private boolean fire = false;
	private int animation = 0;
	private int firea = 0;
	private int hitz = 0;
	private boolean jump = false;
	private int jw = 0, jw2;
	private int ury = 0;
	private boolean tot = false;

	public void setWaypoints(int x, int x2, int y, int y2) {
		waypoints[0] = x;
		waypoints[1] = x2;
		waypoints[2] = y;
		waypoints[3] = y2;

	}

	public void setPos(int x, int y) {
		pos[0] = x;
		pos[1] = y;
		ury = y;
	}

	public void tot() {
		if (tot == false) {
			speed[0] = 0;
			speed[1] = -3;
			Finish.setOpen(true);
		}
		tot = true;

	}

	public boolean fire() {
		boolean f = fire;
		fire = false;
		return f;
	}

	public void hit() {
		hitz = 20;
		animation = 3;
	}

	public int[] paint(Graphics g, int x, int y) {
		if (tot) {
			g.drawImage(Main.boss[animation], (int) (pos[0] - x + 480), (int) (pos[1] - 40 - y + 280), null);
			pos[0] += speed[0];
			pos[1] += speed[1];
			speed[1] += 0.1;
			animation = 3;
		} else {
			if (hitz > 0) {
				firea = 0;
				hitz--;
				if (hitz == 0) {
					animation = 0;
				}
			} else {
				if (firea > 0) {

					firea--;
					if (firea == 0) {
						if (jump) {
							animation = 2;
						} else {
							animation = 0;
						}
					}
				}
			}
			if (speed[0] > 0) {
				g.drawImage(Main.boss[animation], (int) (pos[0] - x + 480 + 80), (int) (pos[1] - 40 - y + 280), -80, 80,
						null);
			} else {
				g.drawImage(Main.boss[animation], (int) (pos[0] - x + 480), (int) (pos[1] - 40 - y + 280), null);
			}

			if (jump) {
				pos[0] += speed[0];
				animation = 2;
				pos[1] += speed[1];
				jw--;
				if (jw < 1) {
					jump = false;
					animation = 0;
					pos[1] = ury;

				}
				if (jw <= jw2 / 2) {
					speed[1] = 2;
				}
			}
			if ((int) (Math.random() * 150 + 1) == 1 && hitz == 0) {
				fire = true;
				if (jump == false) {
					if (x > pos[0] + 40) {
						speed[0] = 0.1;
					} else {
						speed[0] = -0.1;
					}
				}

				firea = 20;
				if (jump) {
					animation = 2;
				} else {
					animation = 1;
				}
			}
			if ((int) (Math.random() * 100 + 1) == 1 && jump == false) {
				jump = true;
				hitz = 0;
				jw = (int) (Math.random() * 80 + 1) + 10;
				jw2 = jw;
				speed[1] = -2;
				if ((int) (Math.random() * 2 + 1) == 1) {
					if (pos[0] + 2 * jw + 80 < waypoints[1]) {
						speed[0] = 2;
					} else {
						jump = false;

					}
				} else {

					if (pos[0] - 2 * jw > waypoints[0]) {
						speed[0] = -2;
					} else {
						jump = false;

					}

				}

			}
		}
		int[] p = new int[2];
		p[0] = (int) pos[0];
		p[1] = (int) pos[1];
		return p;
	}

}
