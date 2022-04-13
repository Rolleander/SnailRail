import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class NetworkMenu implements MouseListener, MouseMotionListener {

	private boolean active = false;
	private Image button;
	private Font font = new Font("Arial", 25, 25);
	private int schnecke = 0, bild = 0, z = 0, spieleranzahl = 2;
	private int spielmodus, leben = 3;
	String[] modus = { "Zeitrennen", "Punkterennen", "Schlacht", "CTF" };
	private String level = "- noch keine gewählt -";
	private int punktezeit;
	int[] moduspunkte = { 3, 50, 10, 5 };
	int[] modusadd = { 1, 10, 5, 5 };
	private boolean teams = false;
	String[] modlimit = { "Minuten", "Punkte", "Kills", "Eroberungen" };

	public NetworkMenu() {

		java.net.URL filename = getClass().getResource("button.png");
		button = Toolkit.getDefaultToolkit().getImage(filename);
		punktezeit = moduspunkte[0];
	}

	public void paint(Graphics g) {
		if (active) {
			z++;
			if (z > 10) {
				bild++;
				z = 0;
				if (bild > 2) {
					bild = 0;
				}
			}
			g.setFont(font);

			Background b = new Background();
			b.init(12);
			b.paint(g, 0, 0);
			g.drawImage(button, 50, 50, 900, 40, null);
			g.setColor(Color.WHITE);
			g.drawString("Spielereinstellungen", 350, 80);
			g.drawImage(button, 50, 150, 900, 40, null);
			g.drawString("Spiel erstellen", 350, 180);
			g.drawImage(button, 50, 500, 900, 40, null);
			g.drawString("Spiel beitreten", 350, 530);

			g.setColor(Color.BLACK);
			g.drawString("Name:", 60, 123);
			g.drawString("Schnecke:", 550, 123);
			g.drawImage(Main.snailimages[schnecke][bild], 750, 100, null);

			if (drawButton(g, "<", 700, 110, 40, 30)) {
				schnecke--;
				if (schnecke < 0) {
					schnecke = 8;
				}
			}
			if (drawButton(g, ">", 800, 110, 40, 30)) {
				schnecke++;
				if (schnecke > 8) {
					schnecke = 0;
				}
			}

			if (drawButton(g, "<", 250, 200, 40, 30)) {
				spieleranzahl--;
				if (spieleranzahl < 2) {
					spieleranzahl = 10;
				}
			}
			if (drawButton(g, ">", 335, 200, 40, 30)) {
				spieleranzahl++;
				if (spieleranzahl > 10) {
					spieleranzahl = 2;
				}
			}
			// leben
			if (drawButton(g, "<", 720, 200, 40, 30)) {
				leben--;
				if (leben < 1) {
					leben = 9;
				}
			}
			if (drawButton(g, ">", 830, 200, 40, 30)) {
				leben++;
				if (leben > 9) {
					leben = 1;
				}
			}

			// spielmodus

			if (drawButton(g, "<", 250, 235, 40, 30)) {
				spielmodus--;
				if (spielmodus < 0) {
					spielmodus = modus.length - 1;

				}
				punktezeit = moduspunkte[spielmodus];
			}
			if (drawButton(g, ">", 500, 235, 40, 30)) {
				spielmodus++;
				if (spielmodus >= modus.length) {
					spielmodus = 0;
				}
				punktezeit = moduspunkte[spielmodus];
			}

			// spiellimit

			if (drawButton(g, "<", 250, 270, 40, 30)) {
				punktezeit -= modusadd[spielmodus];
				if (punktezeit < modusadd[spielmodus]) {
					punktezeit = modusadd[spielmodus];
				}
			}
			if (drawButton(g, ">", 380, 270, 40, 30)) {
				punktezeit += modusadd[spielmodus];

			}

			// teams
			String s;
			if (teams) {
				s = "Frei";
			} else {
				s = "Keine";
			}
			if (drawButton(g, s, 250, 305, 100, 30)) {
				teams = !teams;

			}

			g.setColor(Color.BLACK);
			g.drawString("Adresse:", 60, 575);
			g.drawString("Spieleranzahl:", 60, 225);

			g.drawString("Schneckenleben:", 500, 225);
			g.drawImage(Main.icon, 770, 200, null);
			g.drawString("x" + leben, 800, 225);
			if (("" + spieleranzahl).length() == 1) {
				g.drawString("" + spieleranzahl, 305, 225);
			} else {
				g.drawString("" + spieleranzahl, 295, 225);
			}

			g.drawString("Spielmodus:", 60, 260);
			FontMetrics fm = g.getFontMetrics(font);
			g.drawString(modus[spielmodus], 395 - fm.stringWidth(modus[spielmodus]) / 2, 260);

			g.drawString("Spiellimit:", 60, 295);
			g.drawString("" + punktezeit, 305, 295);
			g.drawString("" + modlimit[spielmodus], 425, 295);
			g.drawString("Map: " + level, 270, 485);
			g.drawString("Teamwahl:", 60, 330);

			if (drawButton(g, "Spiel erstellen", 750, 460, 200, 30)) {

			}

			if (drawButton(g, "Map auswählen", 60, 460, 200, 30)) {

			}

			if (drawButton(g, "Spiel beitreten", 570, 550, 220, 30)) {

			}

			if (drawButton(g, "Zurück", 800, 550, 150, 30)) {
				close();
				Main.hmenu.open();
			}

		}
	}

	public boolean drawButton(Graphics g, String name, int x, int y, int w, int b) {
		boolean ok = false;
		Graphics2D g2d = (Graphics2D) g;
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));

		g.drawImage(button, x, y, w, b, null);
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

		g.setColor(new Color(255, 255, 255));

		if (mx > x && mx < x + w && my > y && my < y + b) {
			g.setColor(new Color(255, 255, 255, 150));
			g.fillRect(x, y, w, b);
			g.setColor(new Color(0, 0, 0));
			if (klick) {
				ok = true;
				klick = false;
				Main.sound.playSound(8);
			}
		}

		g.drawString(name, x + 10, y + b - 5);
		return ok;
	}

	public void close() {
		active = false;
		Main.name.setVisible(false);
		Main.adresse.setVisible(false);

	}

	public void open() {
		active = true;
		Main.name.setVisible(true);
		Main.adresse.setVisible(true);
		Main.name.setFont(font);
		Main.name.setForeground(new Color(100, 100, 230));
		Main.adresse.setFont(font);

		Main.name.setBounds(200, 100, 300, 30);
		Main.adresse.setBounds(200, 550, 300, 30);
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	private int mx, my;

	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		mx = e.getX();
		my = e.getY();
		klick = false;
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

	private boolean klick = false;

	public void mouseReleased(MouseEvent arg0) {
		klick = true;
	}

}
