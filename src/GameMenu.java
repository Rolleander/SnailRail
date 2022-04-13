import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.net.URL;

public class GameMenu implements MouseListener, MouseMotionListener {

	private boolean aktiv;
	private int mx, my;
	private boolean klick = false;
	private Coin c = new Coin();

	private Font font = new Font("Arial", 20, 20);
	private boolean stagelocked[] = new boolean[5];
	private Image button, locked, live;
	private String[] but = { "Return to Mainmenu", "Achievements", "Shop", "Equipment", "Stage Selection" };

	private LoadandSave saves = new LoadandSave();
	private String items[] = { "Slimeball", "Slimeball x10", "Simeball x100", "Toxic Costume", "Soccer Costume",
			"Ghost Costume", "Robot Costume", "Magma Costume", "Pokeball Costume", "White Costume", "Black Costume", "",
			"" };

	private static int mapscores[] = new int[26];
	private Shop shop = new Shop();

	public GameMenu() {
		aktiv = false;
		c.setPos(950, 20);
		URL filename = getClass().getResource("button.png");
		button = Toolkit.getDefaultToolkit().getImage(filename);
		filename = getClass().getResource("locked.png");
		locked = Toolkit.getDefaultToolkit().getImage(filename);

		klick = false;

		for (int i = 0; i < 25; i++) {
			Map m = new Map();

			m.ladeMap(i + 1);

			mapscores[i] = m.getMaxPoints();
		}

	}

	public static void setOwnMapScore(int score) {
		mapscores[25] = score;
	}

	public int getMapScore(int nr) {
		return mapscores[nr];
	}

	public void saveScore(int lvl, int score, boolean clear) {
		if (clear) {
			if (score > Main.profil.getScoe(lvl)) {

				Main.profil.setScore(lvl, score);

			}
		} // Speichern
		saves.saveProfil(Main.profil);
	}

	int animation = 0, bildid = 0, bzaehler = 0;

	private void ladeLevel(int nr) {
		Main.vorbereitenLevel(nr);
		close();
	}

	int auswahl = 0;

	public void paintCoins(Graphics g) {
		// Geld zeichnen
		g.setFont(font);
		g.drawImage(button, 860, 30, 200, 40, null);
		c.paint(g, 570, 270);
		g.setColor(new Color(255, 255, 255));
		g.drawString("" + Main.player.getCoins(), 895, 58);
		int leben = Main.player.getLeben();
		for (int i = 0; i < leben; i++) {
			g.drawImage(Main.icon, 5 + i * 30, 27, null);
		}
		int ammo = Main.equipment.getAmmo();

		g.drawImage(button, 890, 65, 200, 30, null);
		g.drawImage(Main.shot[0][0], 885, 60, null);
		g.drawString("" + ammo, 920, 87);
	}

	private int submenu = 0;

	public void paint(Graphics g) {
		if (aktiv) {
			Main.sound.playTitle();
			// Hintergrund
			g.setColor(new Color(100, 100, 100));
			g.fillRect(0, 0, 1000, 600);
			for (int i = -2; i < 26; i = i + 3) {
				for (int h = -2; h < 15; h = h + 3) {
					g.drawImage(Main.snail[bildid], i * 40 - animation + 40, h * 40 + animation, -40, 40, null);
				}
			}
			bzaehler++;
			if (bzaehler > 9) {
				bzaehler = 0;
				bildid++;
				if (bildid > 4) {
					bildid = 0;
				}
			}
			animation++;
			if (animation >= 120) {
				animation = 0;
			}
			g.setFont(font);

			g.setColor(new Color(0, 0, 0));

			g.drawImage(button, 30, 45, 920, 53, null);
			g.drawString(but[4 - submenu], 400, 75);

			if (submenu == 0) {// Level
								// Stageüberschrift
				g.drawImage(button, 30, 300, 920, 25, null);
				g.drawString("LEVEL SELECTION", 400, 320);

				// Stageauswahl
				for (int i = 4; i > -1; i--) {

					g.setColor(new Color(0, 0, 0));
					g.drawRect(30 + i * 180, 99, 201, 201);
					g.drawImage(Main.stages[i], 30 + i * 180, 100, null);
					if (mx > 30 + i * 180 && mx < 230 + i * 180 && my > 100 && my < 300) {
						auswahl = i;
					}
					if (stagelocked[i]) {
						g.setColor(new Color(0, 0, 0, 230));
						g.fillRect(30 + i * 180, 99, 201, 201);
						g.drawImage(locked, 90 + i * 180, 150, null);
					}
				}
				if (auswahl > -1 && stagelocked[auswahl] == false) {
					int i = auswahl;
					g.drawImage(Main.stages[i], 30 + i * 180, 100, null);
					g.drawRect(30 + i * 180, 99, 201, 201);
					g.setColor(new Color(255, 255, 255, 70));
					g.fillRect(30 + i * 180, 99, 201, 201);
				}

				// Levelauswahl
				g.drawImage(button, 30, 325, 920, 225, null);
				for (int i = 0; i < 5; i++) {
					g.drawImage(button, 35, 340 + i * 40, 910, 37, null);
					boolean free = Main.profil.getLevelFree(i + auswahl * 5);
					if (mx > 35 && mx < 945 && my > 340 + i * 40 && my < 377 + i * 40) {
						g.setColor(new Color(255, 255, 255, 200));
						g.fillRect(35, 340 + i * 40, 910, 37);
						if (klick && free) {
							ladeLevel(i + auswahl * 5 + 1);
							klick = false;
						}
					}
					g.setColor(new Color(0, 0, 0));
					g.drawString("Level " + (i + auswahl * 5 + 1), 45, 365 + i * 40);
					if (free) {
						int score = Main.profil.getScoe(i + auswahl * 5);
						int maxscore = mapscores[i + auswahl * 5];
						int sterne = 0;
						if (score >= maxscore) {
							sterne = 3;
						} else if (score >= maxscore / 1.3) {
							sterne = 2;
						} else if (score > 0) {
							sterne = 1;
						}
						for (int h = 0; h < 3; h++) {
							if (h < sterne) {
								g.drawImage(Main.stars[1], 800 + h * 40, 342 + i * 40, null);
							} else {
								g.drawImage(Main.stars[0], 800 + h * 40, 342 + i * 40, null);
							}
						}
						g.setColor(new Color(50, 50, 0));
						g.drawString("Highscore: " + score, 500, 365 + i * 40);
						g.setColor(new Color(30, 30, 30));
						g.drawString("/ " + maxscore, 650, 365 + i * 40);
					} else {
						g.setColor(new Color(0, 0, 0, 150));
						g.fillRect(35, 340 + i * 40, 910, 37);
						g.setColor(new Color(255, 255, 255));
						g.drawString("Locked", 345, 365 + i * 40);
						g.drawImage(locked, 200, 342 + i * 40, 30, 30, null);
					}
				}
			} else if (submenu == 1)// equipment
			{
				Main.equipment.paint(g, items);
				if (klick) {

					if (Main.equipment.equip(mx, my)) {
						klick = false;
					}
				}
			} else if (submenu == 2)// shop
			{
				shop.paint(g, mx, my, items);
				if (klick) {

					if (shop.buy(mx, my)) {
						saves.saveProfil(Main.profil);
						klick = false;
					}
				}
			} else if (submenu == 3)// archievments
			{

			}

			// Buttonleiste

			for (int i = 0; i < 5; i++) {
				g.drawImage(button, 805 - i * 200, 540, 190, 50, null);

				if (mx > 805 - i * 200 && mx < 995 - i * 200 && my > 540 && my < 730) {
					g.setColor(new Color(255, 255, 255, 150));
					g.fillRect(805 - i * 200, 540, 190, 50);
					if (klick) {
						Main.sound.playSound(8);
						klick = false;
						if (i > 0) {
							submenu = 4 - i;
						} else {
							submenu = 0;
							Main.hmenu.open();
							close();
						}
					}
				}
				g.setColor(new Color(0, 0, 0));

				g.drawString(but[i], 810 - i * 200, 570);
			}

			klick = false;

			// Geld zeichnen
			g.drawImage(button, 860, 30, 200, 40, null);
			c.paint(g, 570, 270);
			g.setColor(new Color(255, 255, 255));
			g.drawString("" + Main.profil.getCoins(), 895, 58);
		}

	}

	public void close() {
		aktiv = false;
	}

	public void open() {

		klick = false;
		aktiv = true;
		for (int i = 0; i < 5; i++) {
			stagelocked[i] = !Main.profil.getStageFree(i);
		}
		stagelocked[0] = false;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		klick = true;
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		mx = e.getX();
		my = e.getY();
	}

}
