
public abstract class Objekt {

	protected int pos[] = new int[2];
	protected boolean aktiv;

	public int[] getPos() {
		return pos;
	}

	public void setPos(int x, int y) {
		pos[0] = x;
		pos[1] = y;
	}

	public boolean getAktiv() {
		return aktiv;
	}

	public void setAktiv(boolean b) {
		aktiv = b;
	}
}
