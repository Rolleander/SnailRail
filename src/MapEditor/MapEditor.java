package MapEditor;

import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Event;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Toolkit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

public class MapEditor extends Frame
		implements ActionListener, Runnable, KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	// Bilder für DoubleBuffer
	private Image dbImage;
	private Graphics dbg;

	int mx, my, mx2, my2;
	int gx = 40, gy = 40;
	int zx = 0, zy = 0;

	int fields[][];
	boolean klick = false;
	boolean ziehen = false, ziehenende = false, show = true;
	int tile = -1;

	int mapw = 0, maph = 0;
	public static Image map[] = new Image[100];

	int tx = 40, ty = 40;

	Balken zoom = new Balken(655, 620, 198, 20);

	static Button b0 = new Button("+1x");
	static Button b1 = new Button("+1y");
	static Button b2 = new Button("Springe zum Anfang");
	static Button b3 = new Button("-1x");
	static Button b4 = new Button("-1y");
	static Button b5 = new Button("X");

	int drehung = 0;

	boolean raster = false;
	boolean shift = false;
	Font font = new Font("Arial", 18, 18);
	FileDialog fd, fd2;

	int fx = -1, fy = -1;

	public void paint(Graphics g) {
		// Paint Methode
		g.setFont(font);
		fx = -1;
		fy = -1;
		for (int i = 0; i < mapw; i++) {
			for (int h = 0; h < maph; h++) {
				g.setColor(new Color(100, 100, 250, 100));
				g.fillRect(i * tx + 8 - zx, h * ty + 30 - zy, tx, ty);
				if (fields[i * maph + h][1] == 0) {
					g.drawImage(map[fields[i * maph + h][0]], i * tx + 8 - zx, h * ty + 30 - zy, tx, ty, null);
				} else if (fields[i * maph + h][1] == 1) {
					g.drawImage(map[fields[i * maph + h][0]], i * tx + 8 - zx + tx, h * ty + 30 - zy, -tx, ty, null);
				} else if (fields[i * maph + h][1] == 2) {
					g.drawImage(map[fields[i * maph + h][0]], i * tx + 8 - zx, h * ty + 30 - zy + ty, tx, -ty, null);
				} else if (fields[i * maph + h][1] == 3) {
					g.drawImage(map[fields[i * maph + h][0]], i * tx + 8 - zx + tx, h * ty + 30 - zy + ty, -tx, -ty,
							null);
				}
				if (ziehen == false) {
					if (mx >= i * tx + 8 - zx && mx <= i * tx + 8 + tx - zx && my >= h * ty + 30 - zy
							&& my <= h * ty + 30 - zy + ty && mx < 650) {
						if (klick == true && tile > -1) {
							fields[i * maph + h][0] = tile;
							fields[i * maph + h][1] = drehung;
							klick = false;
							texture();
						}
						fx = i;
						fy = h;
					}
				}
			}
		}
		if (ziehen) {
			g.setColor(new Color(0, 250, 0, 150));
			if (mx < mx2) {
				if (my2 < my) {
					g.fillRect(mx, my2, mx2 - mx, my - my2);
				} else {
					g.fillRect(mx, my, mx2 - mx, my2 - my);
				}
			} else {
				if (my2 < my) {
					g.fillRect(mx2, my2, mx - mx2, my - my2);
				} else {
					g.fillRect(mx2, my, mx - mx2, my2 - my);
				}
			}

		}
		if (ziehenende && ziehen) {
			ziehen = false;
			ziehenende = false;

			int ziehx = mx;
			int ziehy = my;
			int ziehx2 = mx2;
			int ziehy2 = my2;
			if (ziehx < ziehx2) {
				if (ziehy2 < ziehy) {
					int t = ziehy2;
					ziehy2 = ziehy;
					ziehy = t;
				}
			} else {
				if (ziehy2 < ziehy) {
					int t = ziehy2;
					ziehy2 = ziehy;
					ziehy = t;
				}
				int t = ziehx2;
				ziehx2 = ziehx;
				ziehx = t;
			}

			if (tile > -1) {
				for (int i = 0; i < mapw; i++) {
					for (int h = 0; h < maph; h++) {
						if (i * tx + 8 - zx <= ziehx2 && i * tx + 8 + tx - zx >= ziehx) {
							if (h * ty + 30 - zy <= ziehy2 && h * ty + 30 - zy + ty >= ziehy) {
								fields[i * maph + h][0] = tile;
								fields[i * maph + h][1] = drehung;
							}
						}
					}
				}
			}

			mx = mx2;
			my = my2;
		}
		if (raster == true) {
			g.setColor(new Color(0, 0, 0));
			int a = (zy / ty);
			int ay = a * ty - zy;
			a = (zx / tx);
			int ax = a * tx - zx;
			for (int i = 0; i <= 700 / ty; i += 2) {
				g.drawRect(0, i * ty + 30 + ay, 650, ty);
			}
			for (int i = 0; i <= 1000 / tx; i += 2) {
				g.drawRect(i * tx + 8 + ax, 0, tx, 700);
			}
		}

		// GUI
		g.setColor(new Color(100, 100, 100));
		g.fillRect(650, 0, 350, 700);

		zoom.paint(g);
		int zooms = zoom.getSchieberWeite() + 2;
		tx = zooms;
		ty = zooms;

		double mass = (double) zooms / 40;
		g.setColor(new Color(255, 255, 255));
		g.drawString("Zoom: " + mass + " : 1", 865, 637);

		// MiniMap
		if (show) {
			g.setColor(new Color(150, 150, 150));
			g.fillRect(680, 410, 280, 180);
			if (mapw > 0 && maph > 0) {
				double mmx = 280 / (double) mapw;
				double mmy = 180 / (double) maph;
				for (int i = 0; i < mapw; i++) {
					for (int h = 0; h < maph; h++) {
						g.setColor(new Color(200, 200, 200));
						g.drawRect((int) (680 + i * mmx), (int) (410 + h * mmy), (int) mmx - 1, (int) mmy - 1);
						if (fields[i * maph + h][1] == 0) {
							g.drawImage(map[fields[i * maph + h][0]], (int) (680 + i * mmx), (int) (410 + h * mmy),
									(int) mmx, (int) mmy, null);
						} else if (fields[i * maph + h][1] == 1) {
							g.drawImage(map[fields[i * maph + h][0]], (int) ((int) (680 + i * mmx) + mmx),
									(int) (410 + h * mmy), (int) -mmx, (int) mmy, null);
						} else if (fields[i * maph + h][1] == 2) {
							g.drawImage(map[fields[i * maph + h][0]], (int) (680 + i * mmx),
									(int) ((int) (410 + h * mmy) + mmy), (int) mmx, (int) -mmy, null);
						} else if (fields[i * maph + h][1] == 3) {
							g.drawImage(map[fields[i * maph + h][0]], (int) (680 + i * mmx) + (int) mmx,
									(int) ((int) (410 + h * mmy) + mmy), (int) -mmx, (int) -mmy, null);
						}

						if (i == fx && h == fy) {
							g.setColor(new Color(250, 0, 0, 100));
							g.fillRect((int) (680 + i * mmx), (int) (410 + h * mmy), (int) mmx - 1, (int) mmy - 1);

						}
					}
				}
			}
		}

		for (int i = 0; i < 10; i++) {
			for (int h = 0; h < 10; h++) {
				g.setColor(new Color(150, 150, 150));
				g.fillRect(660 + i * 32, 64 + h * 32, 31, 31);

				g.setColor(new Color(200, 200, 200));
				g.drawRect(660 + i * 32, 64 + h * 32, 31, 31);

				g.drawImage(map[i * 10 + h], 660 + i * 32, 64 + h * 32, 32, 32, null);
				if (mx >= 660 + i * 32 && mx <= 692 + i * 32 && my >= 64 + h * 32 && my <= 96 + h * 32
						&& klick == true) {
					tile = i * 10 + h;
					drehung = 0;
					klick = false;
				}
			}
		}
		if (ziehen == false) {
			g.setColor(new Color(50, 50, 50));
			if (tile > -1) {
				int breit = 0, hoeh = 0;
				if (mx < 650) {
					breit = tx;
					hoeh = ty;
				} else if (mx >= 650) {
					breit = 40;
					hoeh = 40;
				}
				if (drehung == 0) {
					g.drawImage(map[tile], mx - 16, my - 16, breit, hoeh, null);
				} else if (drehung == 1) {
					g.drawImage(map[tile], mx - 16 + breit, my - 16, -breit, hoeh, null);
				} else if (drehung == 2) {
					g.drawImage(map[tile], mx - 16, my - 16 + hoeh, breit, -hoeh, null);
				} else if (drehung == 3) {
					g.drawImage(map[tile], mx - 16 + breit, my - 16 + hoeh, -breit, -hoeh, null);
				}

				g.drawRect(mx - 17, my - 17, breit + 1, hoeh + 1);
			}
		}
		g.setColor(new Color(255, 255, 255));

		if (fx == -1 || fy == -1) {
			g.drawString("Feld Koordinate: Außerhalb Map", 660, 660);

		} else {
			g.drawString("Feld Koordinate: " + fx + " : " + fy, 660, 660);

		}

		g.drawString("Mapgröße: " + mapw + " x " + maph + " = " + (mapw * maph) + " Felder", 660, 680);

	}

	public static void main(String args[]) {

		MapEditor me = new MapEditor();

		me.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		me.setTitle("Map Editor");
		me.setSize(1000, 700);

		me.setResizable(false);
		me.main();
		me.setVisible(true);
		me.setLayout(null);

		b3.setBounds(652, 410, 27, 180);
		b0.setBounds(962, 410, 27, 180);

		b1.setBounds(680, 590, 280, 25);
		b4.setBounds(820, 385, 140, 25);

		b2.setBounds(680, 385, 140, 25);
		b5.setBounds(962, 385, 27, 25);

		me.add(b0);
		me.add(b1);
		me.add(b2);
		me.add(b3);
		me.add(b4);
		me.add(b5);
	}

	public void main() {
		// Listener auf diese Klasse zuweisen
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		addKeyListener(this);
		zoom.setSchieber(38);
		addMouseListener(zoom);
		addMouseMotionListener(zoom);
		// Menüpunkte erstellen
		MenuBar objMenuBar = new MenuBar();

		Menu o = new Menu("Datei");

		MenuItem ob2 = new MenuItem("Neue Map");
		ob2.addActionListener(this);
		o.add(ob2);
		ob2 = new MenuItem("Map Laden");
		ob2.addActionListener(this);
		o.add(ob2);
		ob2 = new MenuItem("Map Speichern");
		ob2.addActionListener(this);
		o.add(ob2);
		o.addSeparator();
		ob2 = new MenuItem("Beenden");
		ob2.addActionListener(this);
		o.add(ob2);

		objMenuBar.add(o);

		o = new Menu("Map");
		Menu o2 = new Menu("Breite ändern");
		ob2 = new MenuItem("+10 [X]");
		ob2.addActionListener(this);
		o2.add(ob2);
		ob2 = new MenuItem("+5 [X]");
		ob2.addActionListener(this);
		o2.add(ob2);
		ob2 = new MenuItem("+1 [X]");
		ob2.addActionListener(this);
		o2.add(ob2);
		ob2 = new MenuItem("-1 [X]");
		ob2.addActionListener(this);
		o2.add(ob2);
		ob2 = new MenuItem("-5 [X]");
		ob2.addActionListener(this);
		o2.add(ob2);
		ob2 = new MenuItem("-10 [X]");
		ob2.addActionListener(this);
		o2.add(ob2);
		o.add(o2);
		o2 = new Menu("Höhe ändern");
		ob2 = new MenuItem("+10 [Y]");
		ob2.addActionListener(this);
		o2.add(ob2);
		ob2 = new MenuItem("+5 [Y]");
		ob2.addActionListener(this);
		o2.add(ob2);
		ob2 = new MenuItem("+1 [Y]");
		ob2.addActionListener(this);
		o2.add(ob2);
		ob2 = new MenuItem("-1 [Y]");
		ob2.addActionListener(this);
		o2.add(ob2);
		ob2 = new MenuItem("-5 [Y]");
		ob2.addActionListener(this);
		o2.add(ob2);
		ob2 = new MenuItem("-10 [Y]");
		ob2.addActionListener(this);
		o2.add(ob2);
		o.add(o2);
		o.addSeparator();
		ob2 = new MenuItem("Map nach Unten verschieben");
		ob2.addActionListener(this);
		o.add(ob2);
		ob2 = new MenuItem("Map nach Rechts verschieben");
		ob2.addActionListener(this);
		o.add(ob2);
		o.addSeparator();
		o2 = new Menu("Zoomstufe");
		ob2 = new MenuItem("5:1");
		ob2.addActionListener(this);
		o2.add(ob2);
		ob2 = new MenuItem("3:1");
		ob2.addActionListener(this);
		o2.add(ob2);
		ob2 = new MenuItem("1:1");
		ob2.addActionListener(this);
		o2.add(ob2);
		ob2 = new MenuItem("1:2");
		ob2.addActionListener(this);
		o2.add(ob2);
		ob2 = new MenuItem("1:3");
		ob2.addActionListener(this);
		o2.add(ob2);
		ob2 = new MenuItem("1:4");
		ob2.addActionListener(this);
		o2.add(ob2);
		o.add(o2);

		ob2 = new MenuItem("Finde Map-Anfang");
		ob2.addActionListener(this);
		o.add(ob2);
		ob2 = new MenuItem("Raster An/Aus");
		ob2.addActionListener(this);
		o.add(ob2);

		objMenuBar.add(o);
		setMenuBar(objMenuBar);

		// Fokus auf das Fenster setzen (damit die Tasten sofort erkannt werden)
		setFocusable(true);
		requestFocus();
		URL filename = getClass().getResource("/MapEditor/tiles.png");
		map[99] = Toolkit.getDefaultToolkit().getImage(filename);

		for (int i = 0; i < 10; i++) {
			for (int h = 0; h < 10; h++) {
				map[i * 10 + h] = createImage(
						new FilteredImageSource(map[99].getSource(), new CropImageFilter(i * 40, h * 40, 40, 40)));
			}
		}
		start();
		fd2 = new FileDialog(this, "Dateidialog", FileDialog.SAVE);
		fd = new FileDialog(this, "Dateidialog", FileDialog.LOAD);
	}

	public boolean action(Event e, Object o) {
		if (e.target == b5)// +1 x
		{
			if (show == true) {
				show = false;
				b5.setLabel("[ ]");
				b0.setVisible(false);
				b1.setVisible(false);
				b3.setVisible(false);
				b4.setVisible(false);
			} else {
				show = true;
				b5.setLabel("X");
				b0.setVisible(true);
				b1.setVisible(true);
				b3.setVisible(true);
				b4.setVisible(true);
			}
		}

		if (e.target == b0)// +1 x
		{
			vergroessernMap(0, true);
		}
		if (e.target == b3)// -1 x
		{
			vergroessernMap(0, false);
		}

		if (e.target == b1)// +1 y
		{
			vergroessernMap(1, true);
		}
		if (e.target == b4)// -1 y
		{
			vergroessernMap(1, false);
		}
		if (e.target == b2) {
			findeAnfang();
		}

		// setFocusable(true);
		requestFocus();
		return false;
	}

	public void actionPerformed(ActionEvent event) {
		String str;
		str = event.getActionCommand();

		if (str.equals("Map Speichern")) {
			speichernMap();
		}

		if (str.equals("Map Laden")) {
			ladeMap();
		}

		if (str.equals("Beenden")) {
			System.exit(0);
		}

		if (str.equals("5:1")) {
			zoom.setSchieber(tx = 5 * gx - 1);
		}
		if (str.equals("3:1")) {
			zoom.setSchieber(tx = 3 * gx - 1);
		}
		if (str.equals("1:1")) {
			zoom.setSchieber(tx = gx - 1);
		}
		if (str.equals("1:2")) {
			zoom.setSchieber(tx = gx / 2 - 1);
		}
		if (str.equals("1:3")) {
			zoom.setSchieber(tx = gx / 3 - 1);
		}
		if (str.equals("1:4")) {
			zoom.setSchieber(tx = gx / 4 - 1);
		}

		if (str.equals("-1 [X]")) {
			vergroessernMap(0, false);
		}
		if (str.equals("-5 [X]")) {
			for (int i = 0; i < 5; i++) {
				vergroessernMap(0, false);
			}
		}
		if (str.equals("-10 [X]")) {
			for (int i = 0; i < 10; i++) {
				vergroessernMap(0, false);
			}
		}
		if (str.equals("+1 [X]")) {
			vergroessernMap(0, true);
		}
		if (str.equals("+5 [X]")) {
			for (int i = 0; i < 5; i++) {
				vergroessernMap(0, true);
			}
		}
		if (str.equals("+10 [X]")) {
			for (int i = 0; i < 10; i++) {
				vergroessernMap(0, true);
			}
		}

		if (str.equals("-1 [Y]")) {
			vergroessernMap(1, false);
		}
		if (str.equals("-5 [Y]")) {
			for (int i = 0; i < 5; i++) {
				vergroessernMap(1, false);
			}
		}
		if (str.equals("-10 [Y]")) {
			for (int i = 0; i < 10; i++) {
				vergroessernMap(1, false);
			}
		}
		if (str.equals("+1 [Y]")) {
			vergroessernMap(1, true);
		}
		if (str.equals("+5 [Y]")) {
			for (int i = 0; i < 5; i++) {
				vergroessernMap(1, true);
			}
		}
		if (str.equals("+10 [Y]")) {
			for (int i = 0; i < 10; i++) {
				vergroessernMap(1, true);
			}
		}

		if (str.equals("Neue Map")) {
			newMap();
		}

		if (str.equals("Finde Map-Anfang")) {
			findeAnfang();
		}

		if (str.equals("Raster An/Aus")) {
			raster = !raster;
		}

		if (str.equals("Map nach Unten verschieben")) {
			schiebenMap(1);
		}

		if (str.equals("Map nach Rechts verschieben")) {
			schiebenMap(0);
		}
	}

	private void newMap() {
		mapw = 50;
		maph = 15;
		fields = new int[mapw * maph][2];
		for (int i = 0; i < mapw * maph; i++) {
			for (int h = 0; h < 5; h++) {
				fields[i * 5 + h][0] = 0;
			}
		}

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

	private void ladeMap() {
		fd.setVisible(true);
		String d = (fd.getDirectory() + fd.getFile());
		File file = new File(d);
		String zeile = "", map = ""; // Lesestrings
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));// Textdatei mit Name name einlesen
			while ((zeile = reader.readLine()) != null) {
				map = map + zeile;
			}
			mapLesen(map);
		} catch (IOException f) {
			System.err.println("Error2 :" + f);
		}
	}

	private void schiebenMap(int r) {

		if (r == 0) {// nach rechts
			for (int i = mapw - 1; i > 0; i--) {
				for (int h = 0; h < maph; h++) {
					if (i == 0) {
						fields[i * maph + h][0] = 0;
						fields[i * maph + h][1] = 0;
					} else {
						fields[i * maph + h][0] = fields[(i - 1) * maph + h][0];
						fields[i * maph + h][1] = fields[(i - 1) * maph + h][1];
					}
				}
			}

		} else {// nach unten
			for (int i = 0; i < mapw; i++) {
				for (int h = maph - 1; h > 0; h--) {
					if (h == 0) {
						fields[i * maph + h][0] = 0;
						fields[i * maph + h][1] = 0;
					} else {
						fields[i * maph + h][0] = fields[i * maph + (h - 1)][0];
						fields[i * maph + h][1] = fields[i * maph + (h - 1)][1];
					}
				}
			}
		}
	}

	private void vergroessernMap(int r, boolean groesser) {
		if (r == 0) {
			if (groesser) {
				mapw++;
				int t[][] = new int[maph * mapw][2];
				for (int i = 0; i < mapw - 1; i++) {
					for (int h = 0; h < maph; h++) {
						t[i * maph + h] = fields[i * maph + h];
					}
				}
				fields = t;
			} else {
				mapw--;
				int t[][] = new int[maph * mapw][2];
				for (int i = 0; i < mapw; i++) {
					for (int h = 0; h < maph; h++) {
						t[i * maph + h] = fields[i * maph + h];
					}
				}
				fields = t;
			}
		} else {
			if (groesser) {
				int t[][] = new int[(maph + 1) * mapw][2];
				maph++;
				for (int i = 0; i < mapw; i++) {
					for (int h = 0; h < maph; h++) {
						if (h == maph - 1) {
							t[i * maph + h][0] = 0;

						} else {
							t[i * maph + h] = fields[i * maph + h - i];
						}
					}
				}

				fields = t;
			} else {
				int t[][] = new int[(maph + 1) * mapw][2];
				maph--;
				for (int i = 0; i < mapw; i++) {
					for (int h = 0; h < maph; h++) {

						t[i * maph + h] = fields[i * maph + h + i];
					}
				}
				fields = t;
			}
		}

	}

	private void speichernMap() {
		fd2.setVisible(true);
		String d = (fd2.getDirectory() + fd2.getFile());

		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(d));
			out.write(mapSpeichern());
			out.close();
		} catch (IOException f) {
		}
	}

	private void findeAnfang() {
		zx = 0;
		zy = 0;
	}

	public void mapLesen(String s) {
		String[] maps = s.split("\\$");
		mapw = Integer.parseInt(maps[0]);
		maph = Integer.parseInt(maps[1]);
		int x = 0, y = 0;
		fields = new int[mapw * maph][2];
		for (int i = 2; i < maps.length; i++) {
			String[] felds = maps[i].split("\\:");
			fields[i - 2][0] = Integer.parseInt(felds[0]);
			fields[i - 2][1] = Integer.parseInt(felds[1]);
			x++;
			if (x >= mapw) {
				x = 0;
				y++;
			}
		}
	}

	public String mapSpeichern() {
		String s = "";
		s = s + mapw + "$";
		s = s + maph + "$";
		for (int i = 0; i < mapw; i++) {
			for (int h = 0; h < maph; h++) {

				s = s + fields[i * maph + h][0] + ":" + fields[i * maph + h][1] + "$";
			}
		}
		return s;
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
				Thread.sleep(5);
			} catch (InterruptedException ex) {
			}
			repaint();
		}
	}

	public void mouseClicked(MouseEvent e) {
		// Wird aufgerufen wenn die Maus die Taste klickt und dann wieder loslässt

	}

	public void mousePressed(MouseEvent e) {
		// Wird aufgerufen wenn eine Maustaste gedrückt wird

	}

	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == 1) {
			klick = true;
		} else {
			tile = -1;
		}
		ziehenende = true;
	}

	public void mouseEntered(MouseEvent e) {
		// Wird aufgerufen wenn die Maus einen Bereich betritt (zb. einen Button usw)

	}

	public void mouseExited(MouseEvent e) {
		// Wird aufgerufen wenn die Maus einen Bereich verlässt (zb. einen Button usw)

	}

	public void mouseDragged(MouseEvent e) {
		// Wird aufgerufen wenn die Maus bei gedrückter Taste bewegt wid
		if (shift == false) {
			mx = e.getX();

			klick = true;
			my = e.getY();
		} else {
			mx2 = e.getX();
			ziehen = true;
			my2 = e.getY();
		}
	}

	public void mouseMoved(MouseEvent e) {
		if (shift) {
			int mx2 = e.getX();
			int my2 = e.getY();
			if (mx2 != mx || my2 != my) {
				int xu = mx2 - mx;
				int yu = my2 - my;
				zx += xu * 2;
				zy += yu * 2;
			}
			mx = e.getX();
			my = e.getY();
		} else {
			mx = e.getX();
			my = e.getY();

		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub
		if (shift == true) {
			if (e.getWheelRotation() < 0) {
				int w = zoom.getSchieberWeite();
				w += 5;
				zoom.setSchieber(w);
			} else {
				int w = zoom.getSchieberWeite();
				w -= 5;
				zoom.setSchieber(w);
			}
		} else {
			if (e.getWheelRotation() < 0) {
				drehung++;
				if (drehung > 3) {
					drehung = 0;
				}
			} else {
				drehung--;
				if (drehung < 0) {
					drehung = 3;
				}
			}

		}
	}

	private void texture() {
		for (int i = 0; i < mapw; i++) {
			for (int h = 0; h < maph; h++) {
				if (isTextureBlock(fields[i * maph + h][0])) {
					// Ränder
					boolean seiten[] = new boolean[4];
					boolean ecken[] = new boolean[4];
					for (int j = 0; j < 4; j++) {
						int x = i, y = h;
						int ex = i, ey = h;
						switch (j) {
						case 0:
							x++;
							break;
						case 1:
							y++;
							break;
						case 2:
							x--;
							break;
						case 3:
							y--;
							break;
						}
						switch (j) {
						case 0:
							ex++;
							ey--;
							break;
						case 1:
							ey++;
							ex++;
							break;
						case 2:
							ex--;
							ey++;
							break;
						case 3:
							ey--;
							ex--;
							break;
						}
						if (x < 0 || x >= mapw || y < 0 || y >= maph) {
							seiten[j] = false;
						} else {
							if (isTextureBlock(fields[x * maph + y][0])) {
								seiten[j] = true;
							} else {
								seiten[j] = false;
							}
						}

						if (ex < 0 || ex >= mapw || ey < 0 || ey >= maph) {
							ecken[j] = true;
						} else {
							if (isTextureBlock(fields[ex * maph + ey][0])) {
								ecken[j] = true;
							} else {
								ecken[j] = false;
							}
						}
					}

					int[] inf = getTextureID(seiten, getTextureGround(fields[i * maph + h][0]), ecken);
					fields[i * maph + h][0] = inf[0];
					fields[i * maph + h][1] = inf[1];

				}
			}
		}
	}

	private int[] getTextureID(boolean[] border, int blockid, boolean[] eckborder) {
		int id = 0;
		int drehung = 0;
		int anz = 0;
		// seitenanzahl
		for (int i = 0; i < 4; i++) {
			border[i] = !border[i];
			if (border[i]) {
				anz++;
			}
		}
		if (anz == 0)// keine seiten
		{
			id = 0;
		} else if (anz == 1)// 1 seite
		{
			for (int i = 0; i < 4; i++) {
				if (border[i]) {
					switch (i) {
					case 0:
						id = 7;
						drehung = 0;
						break;// rechts
					case 1:
						id = 2;
						drehung = 2;
						break;// unten
					case 2:
						id = 6;
						drehung = 0;
						break;// links
					case 3:
						id = 2;
						drehung = 0;
						break;// oben
					}
					break;
				}
			}
		} else if (anz == 2)// 2 seiten
		{
			// im ansonstenfall :
			id = 8;
			drehung = 0;
			for (int i = 0; i < 4; i++) {
				if (i == 3) {
					if (border[i] && border[0]) {
						// oben rechts
						id = 3;
						drehung = 0;
						break;
					}
				} else {
					if (border[i] && border[i + 1]) {
						switch (i) {
						case 0:
							id = 3;
							drehung = 2;
							break;// rechts unten
						case 1:
							id = 1;
							drehung = 2;
							break;// unten links
						case 2:
							id = 1;
							drehung = 0;
							break;// links oben
						}
						break;
					}
				}
			}
		} else if (anz >= 3) {
			id = 8;
			drehung = 0;
		}
		// ecken
		if (id == 0) {
			for (int i = 0; i < 4; i++) {
				if (eckborder[i] == false) {
					switch (i) {
					case 0:
						id = 4;
						drehung = 0;
						break;// rechts oben
					case 1:
						id = 4;
						drehung = 2;
						break;// rechts unten
					case 2:
						id = 5;
						drehung = 2;
						break;// links unten
					case 3:
						id = 5;
						drehung = 0;
						break;// links oben
					}
					break;
				}
			}
		}
		int[] inf = new int[2];
		inf[0] = blockid + id * 10;
		inf[1] = drehung;
		return inf;
	}

	private int getTextureGround(int id) {
		int idb = 0;
		for (int k = 0; k < 9; k++) {
			if (id == 10 + k * 10) {
				idb = 10;
				break;
			}
			if (id == 11 + k * 10) {
				idb = 11;
				break;
			}
			if (id == 12 + k * 10) {

				idb = 12;
				break;
			}
		}
		return idb;
	}

	private boolean isTextureBlock(int id) {
		boolean b = false;

		for (int k = 0; k < 9; k++) {
			if (id == 10 + k * 10 || id == 11 + k * 10 || id == 12 + k * 10) {

				b = true;
				break;
			}
		}
		return b;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
			shift = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		shift = false;
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

}
