import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

public class Equipment {

	private int ammo;
	private boolean items[] = new boolean[10];
	private Image button;
	private int choose = 1;

	public Equipment() {
		ammo = 0;
		for (int i = 0; i < items.length; i++) {
			items[i] = false;
		}
		items[0] = true;
		URL filename = getClass().getResource("button.png");
		button = Toolkit.getDefaultToolkit().getImage(filename);

	}

	public void setEquipment(String list) {
		String[] liste = list.split("\\#");
		ammo = Integer.parseInt(liste[0]);
		for (int i = 1; i < liste.length; i++) {
			if (Integer.parseInt(liste[i]) == 1) {
				items[i - 1] = true;
			} else {
				items[i - 1] = false;
			}
		}
	}

	public void buyAmmo(int nr) {
		ammo += nr;
	}

	public String getEquipmentList() {
		String list;
		list = "" + ammo;
		for (int i = 0; i < items.length; i++) {
			int z = 0;
			if (items[i]) {
				z = 1;
			}
			list = list + "#" + z;
		}
		return list;
	}

	public String getDefaultEquipmentList() {
		String deflist;
		deflist = "0#1";
		for (int i = 1; i < items.length; i++) {
			deflist = deflist + "#0";
		}
		return deflist;
	}

	public boolean canFire() {
		if (ammo > 0) {
			return true;
		} else {
			return false;
		}
	}

	public int getAmmo() {
		return ammo;
	}

	public void fire() {
		ammo--;
	}

	public void purchaseItem(int nr) {
		items[nr] = true;
	}

	public boolean isItemPurchased(int nr) {
		return items[nr];
	}

	public void paint(Graphics g, String[] names) {
		names[1] = "Slimeball: " + ammo;
		names[2] = "Standard Costume";
		for (int i = 1; i < 11; i++) {
			boolean ok = false;
			if (i == 1) {
				ok = true;
			} else {
				if (items[i - 2]) {
					ok = true;
				}
			}
			if (ok) {

				g.drawImage(button, 370, 60 + i * 40, 450, 35, null);
				if (i - 1 == choose) {

					g.setColor(new Color(255, 255, 255, 100));
					g.fillRect(370, 60 + i * 40, 450, 35);
				}
				g.setColor(new Color(0, 0, 0));
				g.drawString(names[i], 400, 85 + i * 40);

			}
		}
	}

	public boolean equip(int mx, int my) {
		boolean b = false;
		for (int i = 2; i < 11; i++) {
			if (mx > 370 && mx < 820) {
				if (items[i - 2]) {
					if (my > 60 + i * 40 && my < 60 + i * 40 + 35) {
						choose = i - 1;
						Main.snail = Main.snailimages[choose - 1];
						b = true;
					}
				}
			}
		}
		return b;
	}

}
