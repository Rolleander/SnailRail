import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.CodeSource;

public class Hauptmenu implements MouseListener, MouseMotionListener {

	private Image haupt, button;
	private boolean aktiv, klick = false, options = false, multi = false;
	private int lasti = -1;
	private Font font = new Font("Arial", 25, 25);
	private String[] menu = { "New Game", "Continue", "Play Editor Map", "Multiplayer", "Options", "Exit" };
	private String[] optoin = { "Music: On", "Sound: On", "Clear Save File", "Show Credits", "Back" };

	private boolean menulocked[] = new boolean[menu.length];

	private static LoadandSave saves = new LoadandSave();

	public Hauptmenu() {
		URL filename = getClass().getResource("menu.png");
		haupt = Toolkit.getDefaultToolkit().getImage(filename);
		filename = getClass().getResource("button.png");
		button = Toolkit.getDefaultToolkit().getImage(filename);
		aktiv = true;
		menulocked[1] = !saves.checkSaveFile();
		menulocked[0] = saves.checkSaveFile();
		// menulocked[3]=true;

	}

	public void close() {
		aktiv = false;
	}

	public void open() {
		aktiv = true;
		klick = false;
		menulocked[1] = !saves.checkSaveFile();
		menulocked[0] = saves.checkSaveFile();

	}

	private void click(int nr) {
		Main.sound.playSound(8);
		switch (nr) {
		case 0:
			Profil p = saves.newGame();
			Main.starten(p);
			Main.ownmap = false;
			close();
			break;
		case 1:
			p = saves.loadGame();
			Main.starten(p);
			Main.ownmap = false;
			close();
			break;
		case 2:

			Main.fd.setVisible(true);
			String d = (Main.fd.getDirectory() + Main.fd.getFile());
			File file = new File(d);
			String zeile = "", map = ""; // Lesestrings
			try {
				BufferedReader reader = new BufferedReader(new FileReader(file));// Textdatei mit Name name einlesen
				while ((zeile = reader.readLine()) != null) {
					map = map + zeile;
				}
				Main.profil = saves.newOwnGame();
				Main.equipment.buyAmmo(100);
				Main.loadMap(map);

			} catch (IOException f) {
				System.err.println("Error2 :" + f);
			}

			break;
		case 3:
			multi = true;

			break;

		case 4:
			options = true;
			break;
		case 5:
			System.exit(0);
			break;
		}
		klick = false;

	}

	public void paint(Graphics g) {

		if (aktiv) {

			Main.sound.playTitle();
			g.setFont(font);
			if (options) {
				Background b = new Background();
				b.init(12);
				b.paint(g, 0, 0);
				for (int i = 0; i < optoin.length; i++) {
					Graphics2D g2d = (Graphics2D) g;
					g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
					g.drawImage(button, 450, 220 + i * 60, 500, 50, null);
					g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
					g.setColor(new Color(255, 255, 255));

					if (mx > 450 && mx < 950 && my > 220 + i * 60 && my < 270 + i * 60) {
						g.setColor(new Color(255, 255, 255, 150));
						g.fillRect(450, 220 + i * 60, 500, 50);
						g.setColor(new Color(0, 0, 0));
						if (klick == true) {
							switch (i) {
							case 0:
								Main.sound.switchMusic();
								String s = "";
								if (Main.sound.canPlayMusic()) {
									s = "On";
								} else {
									s = "Off";
								}
								optoin[i] = "Music: " + s;
								break;
							case 1:
								Main.sound.switchSounds();
								s = "";
								if (Main.sound.canPlaySounds()) {
									s = "On";
								} else {
									s = "Off";
								}
								optoin[i] = "Sound: " + s;
								break;
							case 2: // clear
								saves.killFile();
								menulocked[1] = !saves.checkSaveFile();
								menulocked[0] = saves.checkSaveFile();
								break;
							case 3:
								final Frame f = new Frame();
								f.setFocusableWindowState(true);
								f.setTitle("Credits");
								f.setVisible(true);
								f.setAlwaysOnTop(true);
								f.setBounds(500, 250, 200, 200);
								f.setBackground(new Color(200, 200, 200));
								f.setResizable(false);
								f.addWindowListener(new WindowAdapter() {
									public void windowClosing(WindowEvent e) {
										f.setVisible(false);
									}
								});
								FlowLayout fw = new FlowLayout();
								f.setLayout(fw);

								f.add(new Label("Game & Sprites by BRoll"));
								f.add(new Label("Frog Sprite by Bonzai"));
								f.add(new Label("Music by BRoll with Magix Music Maker"));
								f.add(new Label("Thanks to: FlipelyFlip"));
								f.add(new Label("-----------------------"));
								f.add(new Label("Version: 0.8"));
								f.add(new Label("Visit:"));
								f.add(new Label("http://broll.brokensoft.net"));
								break;
							case 4:
								options = false;
								break;
							}
							Main.sound.playSound(8);
							klick = false;
						}
					}

					g.drawString(optoin[i], 580, 255 + i * 60);
				}

			} else {
				g.drawImage(haupt, 0, 0, null);
				boolean b = false;
				for (int i = 0; i < 6; i++) {

					Graphics2D g2d = (Graphics2D) g;
					g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));

					g.drawImage(button, 450, 220 + i * 60, 500, 50, null);
					g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

					g.setColor(new Color(255, 255, 255));
					if (menulocked[i] == false) {
						if (mx > 450 && mx < 950 && my > 220 + i * 60 && my < 270 + i * 60) {
							g.setColor(new Color(255, 255, 255, 150));
							g.fillRect(450, 220 + i * 60, 500, 50);
							g.setColor(new Color(0, 0, 0));
							if (lasti != i) {
								Main.sound.playSound(7);
							}
							lasti = i;
							b = true;
							if (klick == true) {
								klick = false;
								click(i);
							}
						}
					} else {
						g.setColor(new Color(0, 0, 0, 150));
						g.fillRect(450, 220 + i * 60, 500, 50);
						g.setColor(new Color(150, 150, 150));
					}
					g.drawString(menu[i], 500, 255 + i * 60);
				}
				if (b == false) {
					lasti = -1;
				}
			}

		}
		klick = false;
	}

	public boolean network() {
		boolean m = multi;
		if (multi) {
			close();
		}
		multi = false;

		return m;
	}

	int mx, my;

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
	public void mouseReleased(MouseEvent arg0) {
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

	public static String getPfad() {
		String propertiesFilePath = "client.properties";
		File propertiesFile = new File(propertiesFilePath);

		if (!propertiesFile.exists()) {
			try {
				CodeSource codeSource = Hauptmenu.class.getProtectionDomain().getCodeSource();
				File jarFile = new File(codeSource.getLocation().toURI().getPath());
				String jarDir = jarFile.getParentFile().getPath();
				propertiesFile = new File(jarDir + System.getProperty("file.separator"));
			} catch (Exception ex) {
			}
		}

		String s = propertiesFile.getAbsolutePath();

		s = s.substring(0, s.length() - 17);
		return s;
	}

}
