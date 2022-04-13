import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

public class Shop {

	private int prizes[] = new int[11];
	private Image button;
	private Image bild[] = new Image[11];

	public Shop() {
		URL filename = getClass().getResource("button.png");
		button = Toolkit.getDefaultToolkit().getImage(filename);

		prizes[0] = 3;
		prizes[1] = 28;
		prizes[2] = 250;
		prizes[3] = 300;
		prizes[4] = 500;
		prizes[5] = 1000;
		prizes[6] = 1500;
		prizes[7] = 900;
		prizes[8] = 700;
		prizes[9] = 1200;
		prizes[10] = 1200;
	}

	private int c = 0;

	public void paint(Graphics g, int mx, int my, String[] names) {
		names[1] = "Slimeball x10";
		names[2] = "Slimeball x100";
		bild[0] = Main.shot[0][0];
		bild[1] = Main.shot[0][1];
		bild[2] = Main.shot[0][2];
		for (int i = 1; i < Main.snailimages.length; i++) {
			bild[i + 2] = Main.snailimages[i][0];
		}
		c++;
		if (c > 49) {
			c = 0;
		}
		g.drawImage(button, 100, 150, 200, 200, null);
		for (int i = 0; i < 11; i++) {
			g.drawImage(button, 370, 100 + i * 40, 450, 35, null);
			g.drawImage(Main.coin[c / 10], 780, 98 + i * 40, null);

			if (mx > 370 && mx < 820) {
				if (my > 100 + i * 40 && my < 100 + i * 40 + 35) {

					g.setColor(new Color(255, 255, 255, 100));
					g.fillRect(370, 100 + i * 40, 450, 35);
					g.setColor(new Color(0, 0, 0));
					g.drawString(names[i], 110, 300);
					g.drawImage(bild[i], 150, 170, 100, 100, null);
					g.drawImage(Main.coin[c / 10], 260, 300, null);
					String s = "" + prizes[i];
					int a = s.length();
					g.drawString(prizes[i] + "", 260 - a * 10, 330);
				}

			}
			if (i > 2) {
				if (Main.equipment.isItemPurchased(i - 2)) {
					g.setColor(new Color(0, 0, 0, 100));
					g.fillRect(370, 100 + i * 40, 450, 35);

				}
			}
			g.setColor(new Color(0, 0, 0));
			g.drawString(names[i], 400, 125 + i * 40);
			String s = "" + prizes[i];
			int a = s.length();
			g.drawString(prizes[i] + "", 780 - a * 10, 125 + i * 40);

		}
	}

	public boolean buy(int mx, int my) {
		boolean b = false;
		for (int i = 0; i < 11; i++) {
			if (mx > 370 && mx < 820) {
				if (my > 100 + i * 40 && my < 100 + i * 40 + 35) {
					int c = Main.profil.getCoins();
					if (c >= prizes[i]) {

						if (i < 3) {
							Main.equipment.buyAmmo((int) Math.pow(10, i));
							Main.profil.setCoins(c - prizes[i]);
						} else {
							if (Main.equipment.isItemPurchased(i - 2) == false) {
								Main.equipment.purchaseItem(i - 2);
								Main.profil.setCoins(c - prizes[i]);
							}
						}
						b = true;
					}
				}
			}
		}
		return b;
	}

}
