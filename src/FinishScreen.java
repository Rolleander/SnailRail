import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

public class FinishScreen {

	private Image button;
	private Font font = new Font("Arial", 30, 30);
	private boolean aktiv = false;
	private Font font2 = new Font("Arial", 25, 25);

	public FinishScreen() {
		URL filename = getClass().getResource("button.png");
		button = Toolkit.getDefaultToolkit().getImage(filename);
		aktiv = false;
	}

	private int level, zeit, score;

	public void open(int lvl, boolean cleared) {
		zeit = (int) System.currentTimeMillis();
		aktiv = true;
		level = lvl;
		if (cleared) {
			Main.sound.playSound(4);
			color[0] = (int) (Math.random() * 100 + 1) + 155;
			color[1] = (int) (Math.random() * 100 + 1) + 155;
			color[2] = (int) (Math.random() * 100 + 1) + 155;
			score = Main.player.getCoins();
			int coins = Main.profil.getCoins();
			coins += score;
			Main.profil.setCoins(coins);
			score = score * 10;
			int szeit = Main.player.getStartZeit();
			int aktzeit = (int) System.currentTimeMillis();
			score = score - ((aktzeit - szeit) / 1000);
			score = score + 20 * Main.player.getKills();

			mapscore = Main.gmenu.getMapScore(lvl - 1);
			if (score >= mapscore) {
				sterne = 3;
			} else if (score >= mapscore / 1.3) {
				sterne = 2;
			} else {
				sterne = 1;
			}

		} else {
			Main.sound.playSound(9);
			dunkel = 0;
		}
		if (!Main.ownmap) {
			Main.gmenu.saveScore(lvl - 1, score, cleared);
		}
		clear = cleared;
	}

	private boolean clear;

	private int mapscore, sterne, dunkel;

	private int color[] = new int[3];

	public void paint(Graphics g) {
		if (aktiv) {
			if (clear) {
				g.setColor(new Color(color[0], color[1], color[2], 150));
				g.fillRect(0, 0, 1000, 600);
				int r = (int) (Math.random() * 3 + 1) - 1;
				color[r] += (int) (Math.random() * 10 + 1) - 5;
				if (color[r] < 0) {
					color[r] = 0;
				}
				if (color[r] > 255) {
					color[r] = 255;
				}

				for (int i = 0; i < 550; i = i + 50) {
					g.setColor(new Color((int) (Math.random() * 255 + 1), (int) (Math.random() * 255 + 1),
							(int) (Math.random() * 255 + 1), 50));
					g.fillOval(500 - i, 300 - i, i * 2, i * 2);
				}

				g.drawImage(button, 350, 270, 300, 90, null);
				g.setFont(font);
				if (level < 26) {
					g.setColor(new Color(0, 0, 0));
					g.drawString("Level " + level + " cleared!", 372, 312);
					g.setColor(new Color(255, 255, 255));
					g.drawString("Level " + level + " cleared!", 370, 310);
				} else {
					g.setColor(new Color(0, 0, 0));
					g.drawString("Level cleared!", 372, 312);
					g.setColor(new Color(255, 255, 255));
					g.drawString("Level cleared!", 370, 310);

				}
				g.setFont(font2);
				g.setColor(new Color(255, 200, 0));
				g.drawString("Score: " + score, 370, 345);
				for (int i = 0; i < 3; i++) {
					if (i < sterne) {
						g.drawImage(Main.stars[1], 500 + i * 40, 320, null);
					} else {
						g.drawImage(Main.stars[0], 500 + i * 40, 320, null);
					}
				}
			} else {

				g.setColor(new Color(0, 0, 0, dunkel));
				g.fillRect(0, 0, 1000, 600);
				dunkel += 2;
				if (dunkel > 255) {
					dunkel = 255;
				}
				g.drawImage(button, 350, 270, 300, 70, null);
				g.setFont(font);
				g.setColor(new Color(0, 0, 0));
				g.drawString("Game Over!", 372, 312);
			}

			if ((int) System.currentTimeMillis() / 1000 - 4 > zeit / 1000) {
				Main.stopLevel();
				Main.sound.playTitle();
				aktiv = false;
			}
		}
	}

}
