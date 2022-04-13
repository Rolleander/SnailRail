
public class Profil {

	private int scores[] = new int[25];
	private int coins;

	public Profil() {

	}

	public void newProfil() {
		for (int i = 0; i < 25; i++) {
			scores[i] = 0;
		}
		coins = 0;
	}

	public boolean getLevelFree(int level) {
		boolean free = false;
		if (level > 0) {
			if (scores[level - 1] > 0) {
				free = true;
			}
		} else {
			free = true;
		}
		// return free;
		return true;
	}

	public boolean getStageFree(int stage) {
		boolean free = false;
		for (int i = 0; i < stage * 5; i++) {
			if (scores[i] > 0) {
				free = true;
			} else {
				free = false;
				break;
			}
		}
		// return free;
		return true;
	}

	public void setScore(int level, int score) {
		scores[level] = score;
	}

	public int getScoe(int level) {
		return scores[level];
	}

	public int getCoins() {
		return coins;
	}

	public void setCoins(int coin) {
		coins = coin;

	}
}
