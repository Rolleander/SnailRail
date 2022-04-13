import java.awt.Graphics;

public class Player extends Objekt {

	private int bildid;
	private int speed = 2;
	private int sprungmax = 40;
	private boolean jump = false;
	private boolean jumpdarf = true, halten = false, fly = false;
	private int sprungh = 0;
	private boolean rechts = true;
	private int animation = 0;
	private int fallh = 0;
	private int haltrichtung = 0;
	private boolean tot = false;
	private int leben;
	private int invinc = 0;
	private int maph;
	private int coins = 0;
	private int startzeit, kills;
	private int direction = 0;

	public Player() {
		aktiv = false;
	}

	public void kill() {
		leben = 0;
		tot = true;
		jump = false;
		sprungh = 30;
		fallh = 0;
		Main.failLevel();
	}

	public void schaden(int map) {
		maph = map * 40;
		if (invinc <= 0) {
			leben--;
			if (leben < 1) {
				tot = true;
				sprungh = 0;
				fallh = 0;
				jump = false;
			} else {
				invinc = 100;
			}
			Main.sound.playSound(3);
			jumpdarf = true;
			halten = false;

		}
	}

	public void gainKill(int id) {
		kills += id;
	}

	public int getKills() {
		return kills;
	}

	public void init(int x, int y) {
		pos[0] = x;
		pos[1] = y;
		kills = 0;
		aktiv = true;
		leben = 3;
		tot = false;
		sprungh = 0;
		fallh = 0;
		bildid = 0;
		coins = 0;
		invinc = 0;
		startzeit = (int) System.currentTimeMillis();
	}

	public void collectCoin() {
		coins++;
	}

	public int getCoins() {

		return coins;
	}

	public int getStartZeit() {
		return startzeit;
	}

	public int getLeben() {
		return leben;
	}

	public void fly(int r) {
		jumpdarf = true;
		sprungh = 0;
		fallh = 0;
		bildid = 0;
		animation = 0;
		haltrichtung = 0;
		halten = false;
		fly = true;
		if (r == 0) {
			pos[1] -= 2;
		} else {
			pos[1] += 2;
		}
	}

	private boolean fsound = false;

	public void testeFallen(boolean[][] blocks, boolean[][] climb) {
		if (invinc > 0) {
			invinc--;
		}
		if (tot == false) {

			if (halten == false) {
				if (fly) {
					jumpdarf = true;
					sprungh = 0;
					fallh = 0;
					fly = false;
				} else {
					if (jump == false) {

						jumpdarf = false;
						if (bildid == 3) {
							bildid = 0;
						}
						if (blocks[1][2]) {
							if (pos[1] - 20 < (pos[1] / 40) * 40) {
								pos[1] += speed + fallh / 10;
								fallh++;
								fsound = true;
							} else {
								jumpdarf = true;// Bodenberührung
								pos[1] = (pos[1] / 40) * 40 + 20;
								fallh = 0;
								if (fsound) {
									fsound = false;
									Main.sound.playSound(11);
								}

							}
						} else {
							pos[1] += speed + fallh / 10;
							fallh++;
						}
					} else {// Springen
						bildid = 3;
						if (blocks[1][0]) {
							if (pos[1] - 10 > (pos[1] / 40) * 40) {
								jumpen();

							} else {
								jump = false;
							}
						} else {
							jumpen();
						}

					}
				}
			} else {
				jumpdarf = true;
				sprungh = 0;
				fallh = 0;

				if (haltrichtung == 1) {// Decke
					if (climb[1][0] == false) {
						halten = false;

					}
				} else if (haltrichtung == 2) {// Decke
					if (climb[0][1] == false) {
						halten = false;

					}
				} else if (haltrichtung == 3) {// Decke
					if (climb[2][1] == false) {
						halten = false;

					}
				}

			}
		} else {
			halten = false;
			bildid = 4;

			if (sprungh < 30) {
				pos[1] = pos[1] - (3 + sprungh / 15);
				sprungh++;
				pos[0]++;
			} else {
				if (pos[1] - 100 > maph) {
					bildid = 9;
					Main.failLevel();
				} else {
					fallh++;
					pos[1] = pos[1] + (2 + fallh / 15);
					pos[0]++;
				}
			}
		}
	}

	private void jumpen() {
		sprungh++;
		pos[1] = pos[1] - (speed + (sprungmax - sprungh) / 10);
		if (sprungh >= sprungmax) {
			jump = false;
			sprungh = 0;
		}
	}

	private void festhalten(boolean[][] blocks) {
		if (halten == false) {
			// Wo festhalten?
			if (blocks[1][0]) {// Decke
				if (pos[1] - 40 < (pos[1] / 40) * 40) {
					halten = true;
					pos[1] = pos[1] / 40 * 40 + 20;
					haltrichtung = 1;
					bildid = 0;
				}

			} else if (blocks[0][1]) {// linkerrand
				int abstand = pos[0] % 40;
				if (pos[0] + abstand + 10 > (pos[0] / 40) * 40) {
					halten = true;
					pos[0] = (pos[0] / 40) * 40 + 20;
					haltrichtung = 2;
					bildid = 0;
				}
			} else if (blocks[2][1]) {// linkerrand
				int abstand = pos[0] % 40;
				if (pos[0] + abstand - 80 < (pos[0] / 40) * 40) {
					halten = true;
					pos[0] = (pos[0] / 40) * 40 + 20;
					haltrichtung = 3;
					bildid = 0;
				}
			}
		} else {
			halten = false;
		}
	}

	private int haltentimer = 0;

	public void move(int r, boolean[][] blocks, boolean[][] climb) {
		boolean ani = false;
		if (tot == false) {
			haltentimer++;

			if (halten == false) {
				switch (r) {
				case 0:
					moveLeft(blocks);
					rechts = false;
					ani = true;
					break;
				case 1:
					moveRight(blocks);
					rechts = true;
					ani = true;
					break;
				case 2:
					jump();
					break;
				}
			} else {
				if (haltrichtung < 2) {
					switch (r) {
					case 0:
						moveLeft(blocks);
						rechts = false;
						ani = true;
						break;
					case 1:
						moveRight(blocks);
						rechts = true;
						ani = true;
						break;
					case 2:
						jump();
						break;
					}
				} else {
					switch (r) {
					case 0:
						jump();
						break;
					case 1:
						jump();
						break;
					case 2:
						moveLeft(blocks);
						rechts = false;
						ani = true;
						break;
					case 3:
						moveRight(blocks);
						rechts = true;
						ani = true;
						break;
					}
				}

			}
			switch (r) {
			case 4:
				festhalten(climb);
				break;
			case 6:
				shot(true);
				break;
			case 7:
				shot(false);
				break;
			}
			if (ani) {
				animation++;
				if (animation > 10) {
					animation = 0;
					bildid++;
					if (bildid > 2) {
						bildid = 0;
					}
				}
			}
		}
	}

	private void moveLeft(boolean[][] blocks) {
		if (halten == false || haltrichtung == 1) {
			if (blocks[0][1]) {
				int abstand = pos[0] % 40;
				if (pos[0] + abstand - 30 > (pos[0] / 40) * 40) {
					pos[0] -= speed;
				}
			} else {
				pos[0] -= speed;

			}
		} else {
			if (haltrichtung > 1) {
				if (blocks[1][0]) {
					if (pos[1] - 10 > (pos[1] / 40) * 40) {
						pos[1] -= speed;
					}
				} else {
					pos[1] -= speed;
				}
			}
		}
	}

	private void moveRight(boolean[][] blocks) {
		if (halten == false || haltrichtung == 1) {
			if (blocks[2][1]) {
				int abstand = pos[0] % 40;
				if (pos[0] + abstand - 50 < (pos[0] / 40) * 40) {
					pos[0] += speed;
				}
			} else {
				pos[0] += speed;

			}
		} else {
			if (haltrichtung > 1) {
				if (blocks[1][2]) {
					if (pos[1] - 20 < (pos[1] / 40) * 40) {
						pos[1] += speed;
					}
				} else {
					pos[1] += speed;
				}
			}
		}
	}

	private void jump() {
		if (jumpdarf == true) {
			Main.sound.playSound(0);
			jump = true;
			jumpdarf = false;
			sprungh = 0;
			halten = false;
		}
	}

	public void paint(Graphics g) {

		if (invinc == 0 || invinc % 3 == 0) {

			if (halten == false) {
				if (rechts) {
					g.drawImage(Main.snail[bildid], 460, 260, null);
					direction = 0;
				} else {
					g.drawImage(Main.snail[bildid], 500, 260, -40, 40, null);
					direction = 2;
				}
			} else {
				if (haltrichtung == 0) {
					if (rechts) {
						g.drawImage(Main.snail[bildid], 460, 260, null);
						direction = 0;
					} else {
						g.drawImage(Main.snail[bildid], 500, 260, -40, 40, null);
						direction = 2;
					}
				} else if (haltrichtung == 1) {
					if (rechts) {
						g.drawImage(Main.snail[bildid], 460, 300, 40, -40, null);
						direction = 0;
					} else {
						g.drawImage(Main.snail[bildid], 500, 300, -40, -40, null);
						direction = 2;
					}
				} else if (haltrichtung == 2) {

					if (rechts) {
						g.drawImage(Main.snail[bildid + 5], 460, 260, null);
						direction = 1;
					} else {
						g.drawImage(Main.snail[bildid + 5], 460, 300, 40, -40, null);
						direction = 3;
					}
				} else if (haltrichtung == 3) {
					if (rechts) {
						g.drawImage(Main.snail[bildid + 5], 500, 260, -40, 40, null);
						direction = 1;
					} else {
						g.drawImage(Main.snail[bildid + 5], 500, 300, -40, -40, null);
						direction = 3;
					}
				}
			}
		}
	}

	private void shot(boolean high) {
		if (Main.equipment.canFire()) {
			Map.schuss(direction, pos[0] + 480, pos[1] + 280, high);
			Main.equipment.fire();
			Main.sound.playSound(10);
		}
	}

	public boolean hit(int sx, int sy, int maph) {
		if (sx >= pos[0] - 40 && sx <= pos[0]) {
			if (sy >= pos[1] - 40 && sy <= pos[1]) {
				schaden(maph);

				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

}
