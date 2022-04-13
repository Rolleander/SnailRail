
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.IOException;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class Main extends Frame implements Runnable {

	// Bilder für DoubleBuffer
	private Image dbImage;
	private Graphics dbg;

	private static Map map = new Map();
	public static Player player = new Player();

	public static Image mapBilder[] = new Image[100];
	static int tilex = 40, tiley = 40;
	private MouseAndKey listener = new MouseAndKey();
	public static Image snail[] = new Image[10];
	public static Image snailimages[][] = new Image[9][10];
	public static Image coin[] = new Image[5];
	public static Image enemy[][] = new Image[10][2];
	public static Image boss[] = new Image[4];
	public static Sound sound = new Sound();
	public static Equipment equipment = new Equipment();
	public static Hauptmenu hmenu = new Hauptmenu();
	public static GameMenu gmenu = new GameMenu();
	private static FinishScreen fmenu = new FinishScreen();
	public static Image icon;
	private int size[] = new int[2];
	private static boolean go = false, move = false;
	public static Image stages[] = new Image[5];
	public static Image stars[] = new Image[2];
	public static Image fire[] = new Image[2];
	public static Image shot[][] = new Image[2][5];
	public static Image lava[] = new Image[3];
	public static Profil profil;
	private static int level;
	private NetworkMenu network = new NetworkMenu();
	static boolean ownmap = false;
	static FileDialog fd;

	public static TextField adresse = new TextField();
	public static TextField name = new TextField();

	public static boolean loadTestMap = false;

	public static void main(String[] args) {
		Main main = new Main();
		main.setEnabled(true);
		main.setResizable(false);
		main.setVisible(true);
		main.setSize(1000, 600);
		main.init();
		int width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		main.setLocation((width - 1000) / 2, (height - 600) / 2 - 50);
		main.setIconImage(icon);
		main.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		main.add(name);
		main.add(adresse);
		name.setVisible(false);
		adresse.setVisible(false);
		main.setLayout(null);
		main.setTitle("Snail Rail");
		fd = new FileDialog(main, "Dateidialog", FileDialog.LOAD);

	}

	public void init() {
		size[0] = 1000;
		size[1] = 600;
		addKeyListener(listener);
		addMouseListener(listener);
		addMouseMotionListener(listener);
		addMouseListener(hmenu);
		addMouseMotionListener(network);
		addMouseListener(network);
		addMouseMotionListener(hmenu);
		addMouseListener(gmenu);
		addMouseMotionListener(gmenu);
		// Fokus auf das Fenster setzen (damit die Tasten sofort erkannt werden)
		setFocusable(true);
		requestFocus();
		bilder();

		sound.loadSounds();
		start();
	}

	public static void clearLevel() {
		fmenu.open(level, true);
		move = false;
	}

	public static void failLevel() {
		fmenu.open(level, false);
		move = false;
	}

	public static void stopLevel() {
		if (ownmap) {
			loadMap(merker);

		} else {
			gmenu.open();
			go = false;
			move = false;
		}

	}

	public static void vorbereitenLevel(int lvl) {
		sound.stopTitle();
		map = new Map();
		level = lvl;

		map.ladeMap(lvl);

		int startx = map.getStartPos()[0] * 40 + 20;
		int starty = map.getStartPos()[1] * 40 + 20;
		player.init(startx, starty);
		go = true;
		sound.playMusic((lvl - 1) / 5);
		move = true;
	}

	public static void starten(Profil p) {
		profil = p;
		gmenu.open();
	}

	private void action() {
		// Map Daten komprimieren
		boolean[][] feld = getPassierbarkeit();
		boolean[][] feld2 = getClimb();

		player.testeFallen(feld, feld2);
		feld = getPassierbarkeit();
		feld2 = getClimb();
		boolean[] tasten = listener.getTastenDruck();
		for (int i = tasten.length - 1; i > -1; i--) {
			if (tasten[i]) {
				player.move(i, feld, feld2);
			}
		}
		listener.clear();
		if (tasten[5]) {// Esc
			failLevel();
			sound.playSound(5);
		}

	}

	public static void schaden(boolean kill) {
		if (kill) {
			player.kill();
		} else {
			player.schaden(map.getDimension()[1]);
		}
	}

	public void paint(Graphics g) {
		if (go) {
			// Spiel aktionen
			if (move) {
				action();
			}
			int x = player.getPos()[0];
			int y = player.getPos()[1];
			// Hintergrund zeichnen
			map.drawBackground(g, x, y);
			// Map zeichnen
			int mapw = map.getDimension()[0];
			int maph = map.getDimension()[1];
			// Randstücke weiterzeichnen
			for (int i = -25; i < mapw + 25; i++) {
				for (int h = -15; h < maph + 15; h++) {
					int tx = i * tilex - x + 480;
					int ty = h * tiley - y + 280;
					int id = map.getTileID(0, 0);
					int drehung = 0;
					if (i < 0 || h < 0 || i >= mapw || h >= maph) {
						if (tx + 100 > 0 && tx - 100 < size[0] && ty + 100 > 0 && ty - 100 < size[1]) // Überhaupt im
																										// Bildschirm?
						{
							switch (drehung) {
							case 0:
								g.drawImage(mapBilder[id], tx, ty, tilex, tiley, null);
								break;
							case 1:
								g.drawImage(mapBilder[id], tx + tilex, ty, -tilex, tiley, null);
								break;
							case 2:
								g.drawImage(mapBilder[id], tx, ty + tiley, tilex, -tiley, null);
								break;
							case 3:
								g.drawImage(mapBilder[id], tx + tilex, ty + tiley, -tilex, -tiley, null);
								break;
							}
						}
					}
				}
			}

			// MapTiles Zeichnen

			for (int i = 0; i < mapw; i++) {
				for (int h = 0; h < maph; h++) {
					int id = map.getTileID(i, h);
					int drehung = map.getTileDrehung(i, h);
					int tx = i * tilex - x + 480;
					int ty = h * tiley - y + 280;

					if (tx + 100 > 0 && tx - 100 < size[0] && ty + 100 > 0 && ty - 100 < size[1]) // Überhaupt im
																									// Bildschirm?
					{
						switch (drehung) {
						case 0:
							g.drawImage(mapBilder[id], tx, ty, tilex, tiley, null);
							break;
						case 1:
							g.drawImage(mapBilder[id], tx + tilex, ty, -tilex, tiley, null);
							break;
						case 2:
							g.drawImage(mapBilder[id], tx, ty + tiley, tilex, -tiley, null);
							break;
						case 3:
							g.drawImage(mapBilder[id], tx + tilex, ty + tiley, -tilex, -tiley, null);
							break;
						}
					}
				}
			}

			// Spieler zeichnen

			player.paint(g);

			// MapObjekte zeichnen
			map.drawObjetcs(g, x, y);
			gmenu.paintCoins(g);
			fmenu.paint(g);
		} else {

			gmenu.paint(g);
			hmenu.paint(g);
			if (hmenu.network()) {
				network.open();
			}
			network.paint(g);

		}

	}

	private void bilder() {
		URL filename = getClass().getResource("tiles.png");
		mapBilder[99] = Toolkit.getDefaultToolkit().getImage(filename);
		for (int i = 0; i < 10; i++) {
			for (int h = 0; h < 10; h++) {
				mapBilder[i * 10 + h] = createImage(new FilteredImageSource(mapBilder[99].getSource(),
						new CropImageFilter(i * 40, h * 40, 40, 40)));
			}
		}

		filename = getClass().getResource("liveicon.png");
		icon = Toolkit.getDefaultToolkit().getImage(filename);

		for (int h = 0; h < snailimages.length; h++) {
			filename = getClass().getResource("snail" + h + ".png");
			snailimages[h][9] = Toolkit.getDefaultToolkit().getImage(filename);
			for (int i = 0; i < 10; i++) {
				snailimages[h][i] = createImage(
						new FilteredImageSource(snailimages[h][9].getSource(), new CropImageFilter(i * 40, 0, 40, 40)));
			}
		}
		snail = snailimages[0];
		for (int i = 0; i < shot.length; i++) {
			filename = getClass().getResource("shot" + i + ".png");
			shot[i][4] = Toolkit.getDefaultToolkit().getImage(filename);
			for (int h = 0; h < 5; h++) {
				shot[i][h] = createImage(
						new FilteredImageSource(shot[i][4].getSource(), new CropImageFilter(h * 40, 0, 40, 40)));
			}
		}
		filename = getClass().getResource("lava.png");
		lava[2] = Toolkit.getDefaultToolkit().getImage(filename);
		for (int i = 0; i < 3; i++) {
			lava[i] = createImage(new FilteredImageSource(lava[2].getSource(), new CropImageFilter(i * 40, 0, 40, 40)));
		}

		filename = getClass().getResource("coin.png");
		coin[4] = Toolkit.getDefaultToolkit().getImage(filename);
		for (int i = 0; i < 5; i++) {
			coin[i] = createImage(new FilteredImageSource(coin[4].getSource(), new CropImageFilter(i * 40, 0, 40, 40)));
		}
		filename = getClass().getResource("stages.png");
		stages[4] = Toolkit.getDefaultToolkit().getImage(filename);
		for (int i = 0; i < 5; i++) {
			stages[i] = createImage(
					new FilteredImageSource(stages[4].getSource(), new CropImageFilter(i * 200, 0, 200, 200)));
		}
		filename = getClass().getResource("stars.png");
		stars[1] = Toolkit.getDefaultToolkit().getImage(filename);
		for (int i = 0; i < 2; i++) {
			stars[i] = createImage(
					new FilteredImageSource(stars[1].getSource(), new CropImageFilter(i * 32, 0, 32, 32)));
		}
		filename = getClass().getResource("seniorfrog.png");
		boss[3] = Toolkit.getDefaultToolkit().getImage(filename);
		for (int i = 0; i < 4; i++) {
			boss[i] = createImage(new FilteredImageSource(boss[3].getSource(), new CropImageFilter(i * 80, 0, 80, 80)));
		}

		for (int i = 0; i < enemy.length; i++) {
			filename = getClass().getResource("enemy" + i + ".png");
			enemy[i][1] = Toolkit.getDefaultToolkit().getImage(filename);
			for (int h = 0; h < 2; h++) {
				enemy[i][h] = createImage(
						new FilteredImageSource(enemy[i][1].getSource(), new CropImageFilter(h * 40, 0, 40, 40)));
			}
		}
		filename = getClass().getResource("flame0.gif");
		fire[0] = Toolkit.getDefaultToolkit().getImage(filename);
		filename = getClass().getResource("flame2.gif");
		fire[1] = Toolkit.getDefaultToolkit().getImage(filename);
	}

	public void update(Graphics g) {
		// DOUBLE BUFFER gegen Bildschirmflackern
		if (dbImage == null) {
			dbImage = createImage(this.getSize().width, this.getSize().height);
			dbg = dbImage.getGraphics();
		}
		dbg.setColor(getBackground());
		dbg.fillRect(0, 0, this.getSize().width, this.getSize().height);
		dbg.setColor(getForeground());
		paint(dbg);
		g.drawImage(dbImage, 0, 0, this);
	}

	public void start() {
		// Thread starten
		Thread th = new Thread(this);
		th.start();
	}

	public void stop() {
		// Wird ausgeführt wenn Thread gestoppt wird
	}

	public void destroy() {
		// Wird ausgeführt wenn Thread geschlossen wird (Programmende)
		// Hier müsst ihr alle Lieder stoppen, falls ihr welche verwendet
		// damit sie im Browser nicht weiterlaufen
	}

	public void run() {
		// Run-Methode
		while (true) {
			try {
				// Pausenzeit
				Thread.sleep(10);
			} catch (InterruptedException ex) {
			}
			repaint();
		}
	}

	private boolean[][] getPassierbarkeit() {
		boolean feld[][] = new boolean[3][3];
		int xpos = player.getPos()[0] / 40;
		int ypos = player.getPos()[1] / 40;
		for (int i = -1; i < 2; i++) {
			for (int h = -1; h < 2; h++) {
				feld[i + 1][h + 1] = map.getBlock(xpos + i, ypos + h);
			}
		}
		return feld;
	}

	private boolean[][] getClimb() {
		boolean feld[][] = new boolean[3][3];
		int xpos = player.getPos()[0] / 40;
		int ypos = player.getPos()[1] / 40;
		for (int i = -1; i < 2; i++) {
			for (int h = -1; h < 2; h++) {
				feld[i + 1][h + 1] = map.getClimb(xpos + i, ypos + h);
			}
		}
		return feld;
	}

	public static void shotEnemy(Shot s)// Gegnerschuss
	{
		map.schuss(s);
	}

	public static boolean hitPlayer(Shot s, int h) {
		int sx = s.getPos()[0];
		int sy = s.getPos()[1];
		return player.hit(sx, sy, h);
	}

	private static String merker;

	public static void loadMap(String mapd) {
		merker = mapd;
		sound.stopTitle();
		map = new Map();
		map.ladeMap(mapd);
		int startx = map.getStartPos()[0] * 40 + 20;
		int starty = map.getStartPos()[1] * 40 + 20;
		player.init(startx, starty);
		go = true;
		sound.playMusic(3);
		level = 26;
		move = true;
		ownmap = true;
	}
}
