import java.awt.Color;
import java.awt.Graphics;

public class Enemy extends MapObjekt {

	// Links - Rechts Weg Begrenzungen
	private int waypoints[] = new int[4];
	private int enemyid;
	private int leben;
	// Bewegungsrichtung: Links
	private int direction = 0;
	private int animation = 0;
	private int frequenz;
	private double speed;
	private boolean damage = false;
	private boolean self = false, tot = false, vertical = false, flyer = false;
	private int getcoins = 0;
	private double pos2[] = new double[2]; // double pos
	private Boss boss;

	public Enemy(int id) {
		enemyid = id;
		aktiv = true;
		image = 0;
		switch (id) {
		case 0: // Schildkröte
			speed = 0.5;
			damage = true;
			vertical = false;
			getcoins = 2;
			leben = 2;
			self = false;
			frequenz = 10;
			break;
		case 1: // Käfer
			speed = 0.3;
			damage = false;
			vertical = false;
			getcoins = 1;
			leben = 1;
			self = true;
			frequenz = 10;
			break;
		case 2: // Spinne
			speed = 0.7;
			damage = true;
			vertical = true;
			getcoins = 3;
			leben = 3;
			self = false;
			frequenz = 7;
			break;
		case 3: // Vogel
			speed = 1;
			damage = true;
			vertical = false;
			getcoins = 4;
			leben = 3;
			self = false;
			flyer = true;
			frequenz = 15;
			break;
		case 4: // Huhn
			speed = 0;
			damage = false;
			vertical = true;
			getcoins = 3;
			leben = 1;
			self = true;
			frequenz = 10;
			break;
		case 5: // Wespe
			speed = 1;
			damage = true;
			vertical = true;
			getcoins = 3;
			leben = 2;
			self = false;
			frequenz = 1;
			break;
		case 6: // Energieball
			speed = 1.5;
			damage = true;
			vertical = true;
			leben = 100000;
			self = false;
			frequenz = 1;
			break;
		case 7: // Zahnrad
			speed = 0.75;
			damage = true;
			vertical = false;
			getcoins = 5;
			leben = 5;
			self = false;
			frequenz = 10;
			break;
		case 8: // kugel horiz
			speed = 0;
			damage = true;
			vertical = false;
			getcoins = 3;
			leben = 5;
			self = false;
			frequenz = 3;
			flyer = true;
			break;
		case 9: // kugel verti
			speed = 0;
			damage = true;
			vertical = true;
			getcoins = 3;
			leben = 5;
			self = false;
			frequenz = 3;
			break;
		case 10:// BOSS
			speed = 2;
			damage = true;
			vertical = false;
			getcoins = 0;
			leben = 20;
			self = false;
			frequenz = 10;
			boss = new Boss();
		}
		destroy = true;
		if (leben > 100) {
			destroy = false;
		}
	}

	public boolean isFlyer() {
		return flyer;
	}

	public void setPos(int x, int y) {
		pos2[0] = x;
		pos2[1] = y;
		pos[0] = (int) pos2[0];
		pos[1] = (int) pos2[1];
		if (boss != null) {
			boss.setPos(x, y);
		}
	}

	public void setWaypoints(int x, int x2, int y, int y2) {
		waypoints[0] = x;
		waypoints[1] = x2;
		waypoints[2] = y;
		waypoints[3] = y2;
		if (boss != null) {
			boss.setWaypoints(x, x2, y, y2);

		}
	}

	public boolean isBlock() {
		return false;
	}

	public boolean isEnemy() {
		return true;
	}

	private void move() {
		if (speed > 0) {
			animation++;
			if (animation > frequenz) {
				animation = 0;
				image++;
				if (image > 1) {
					image = 0;
				}
			}
			if (vertical == false) {
				if (direction == 0) {
					pos2[0] -= speed;
					if (pos2[0] <= waypoints[0]) {
						direction = 1;
					}
				} else {
					pos2[0] += speed;
					if (pos2[0] >= waypoints[1]) {
						direction = 0;
					}
				}
			} else {
				if (direction == 0) {
					pos2[1] -= speed;
					if (pos2[1] <= waypoints[2]) {
						direction = 1;
					}
				} else {
					pos2[1] += speed;
					if (pos2[1] >= waypoints[3]) {
						direction = 0;
					}
				}
			}
		}
		pos[0] = (int) pos2[0];
		pos[1] = (int) pos2[1];
	}

	public void paint(Graphics g, int x, int y) {
		if (aktiv) {
			if (boss == null) {
				if (enemyid == 2 && tot == false) {// Spinnenfaden
					g.setColor(new Color(200, 200, 200));
					g.drawLine(pos[0] - x + 480 + 20, pos[1] - y + 280, pos[0] - x + 480 + 20, waypoints[2] - y + 280);
				}

				if (direction == 0) {// Links
					g.drawImage(Main.enemy[enemyid][image], pos[0] - x + 480, pos[1] - y + 280, null);
				} else {// Rechts
					g.drawImage(Main.enemy[enemyid][image], pos[0] - x + 520, pos[1] - y + 280, -40, 40, null);
				}
				if (tot) {
					pos2[1] += speed;
					speed = speed + 0.1;
					pos[1] = (int) pos2[1];
					if (pos[1] > 10000) {
						aktiv = false;
					}
				} else {
					if (enemyid == 8 || enemyid == 9) {
						if (inRadius(pos[0] + 20, x, pos[1] + 20, y, 100)) {

							speed = 3;
						} else {
							speed = 0;
						}
					}
					move();
					checkeAction(x, y);
				}
			} else {// Boss
				pos = boss.paint(g, x, y);
				checkeActionBoss(x, y);
				if (boss.fire()) {
					schuss(x, y);
				}
				if (tot) {
					boss.tot();
				}
			}
		}
	}

	public void schuss(int x, int y) {
		if (inRadius(pos[0], x, pos[1], y, 300)) {
			Shot s = new Shot(1, 0);
			double xs = getMovement(pos[0] + 20, pos[1] + 20, x, y)[0] / 2;
			double ys = getMovement(pos[0] + 20, pos[1] + 20, x, y)[1] / 2;
			s.init(xs, ys, false);
			s.setPos(pos[0] + 500, pos[1] + 300);
			s.setBounceBlock(4000);
			s.setDirect(true);
			Main.shotEnemy(s);
		}
	}

	public int getCoins() {
		int c = coins;
		coins = 0;
		return c;
	}

	public void schaden() {
		leben--;
		if (leben == 0) {
			Main.sound.playSound(2);
			Main.player.gainKill(enemyid);
			speed = -3;
			coins = getcoins;
			tot = true;

		}
	}

	private void checkeAction(int x, int y) {
		if (x + 10 >= pos[0] && x - 10 <= pos[0] + 40) {
			if (y - 5 >= pos[1] && y + 5 <= pos[1] + 40) {
				if (damage) {
					Main.schaden(false);
				}
				if (self) {
					schaden();
				}
			}
		}
	}

	private void checkeActionBoss(int x, int y) {
		if (x + 20 >= pos[0] && x - 10 <= pos[0] + 70) {
			if (y - 10 >= pos[1] && y + 10 <= pos[1] + 50) {
				if (damage) {
					Main.schaden(false);
				}
				if (self) {
					schaden();
				}
			}
		}
	}

	public boolean hit(int sx, int sy) {

		if (sx >= pos[0] - 20 && sx <= pos[0] + 20) {
			if (sy >= pos[1] - 20 && sy <= pos[1] + 20) {
				schaden();
				if (boss != null) {
					boss.hit();
				}
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}
