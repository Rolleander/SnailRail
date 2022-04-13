import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class LoadandSave {

	private String ordnerpfad = System.getProperty("user.home") + "/BRollGames/SnailRail";

	public LoadandSave() {

	}

	public void saveProfil(Profil p) {
		String s = "";
		s = s + p.getCoins() + "?";
		for (int i = 0; i < 25; i++) {
			s = s + p.getScoe(i) + "?";
		}
		s = s + Main.equipment.getEquipmentList();
		s = new Code().encode(s);
		try {

			FileWriter out = new FileWriter(ordnerpfad + "/profil.txt", false);
			out.write(s);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}

	public void killFile() {
		File file = new File(ordnerpfad + "/profil.txt");
		if (file.exists()) {
			file.delete();
		}
	}

	public Profil loadGame() {
		Profil p = new Profil();
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(ordnerpfad + "/profil.txt"));
			String s = br.readLine();
			s = new Code().decode(s);

			String[] save = s.split("\\?");
			p.setCoins(Integer.parseInt(save[0]));
			for (int i = 0; i < 25; i++) {
				p.setScore(i, Integer.parseInt(save[i + 1]));
			}
			Main.equipment.setEquipment(save[26]);
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return p;
	}

	public Profil newGame() {
		String s = "0?";
		for (int i = 0; i < 25; i++) {
			s = s + "0?";
		}
		s = s + Main.equipment.getDefaultEquipmentList();
		s = new Code().encode(s);
		try {
			FileWriter out = new FileWriter(ordnerpfad + "/profil.txt", false);
			out.write(s);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		Profil p = new Profil();
		p.newProfil();
		return p;
	}

	public Profil newOwnGame() {
		Profil p = new Profil();
		p.newProfil();
		return p;
	}

	public boolean checkSaveFile() {
		boolean b = false;
		File f = new File(ordnerpfad);
		if (f.exists()) {

			f = new File(ordnerpfad + "/profil.txt");
			if (f.exists()) {
				b = true;
			}

		} else {
			f.mkdirs();
		}
		return b;
	}

	public void save(String s) {

	}

	public String read() {
		String s = "";
		return s;
	}

}
