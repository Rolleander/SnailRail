import java.awt.Graphics;
import java.awt.Point;

public class MapObjekt extends Objekt {

	protected boolean climb, stand, destroy = false;
	protected int image, drehung, coins;

	public MapObjekt() {

	}

	public void paint(Graphics g, int x, int y) {

	}

	public void setBlockGround(boolean sta, boolean cli) {
		climb = cli;
		stand = sta;
	}

	public int getCoins() {
		int c = coins;
		coins = 0;
		return c;
	}

	public boolean isCoin() {
		return false;
	}

	public boolean getBlock() {
		return stand;
	}

	public boolean getClimb() {
		return climb;
	}

	public boolean isEnemy() {
		return false;
	}

	public boolean canDestroy() {
		return destroy;
	}

	public boolean isBlock() {
		return true;
	}

	public boolean isFlyer() {
		return false;
	}

	public void setWaypoints(int x, int x2, int y, int y2) {

	}

	public boolean hit(int sx, int sy) {
		return false;
	}

	protected boolean inRadius(int x1, int x2, int y1, int y2, int r)// Punkt im Radius des anderen Punkts?
	{
		boolean b = false;
		int abstand = (int) Point.distance(x1, y1, x2, y2);
		if (abstand <= r) {
			b = true;
		}
		return b;
	}

	protected double[] getMovement(double x1, double y1, double x2, double y2) {
		double w = Math.atan2((y2 - y1), (x2 - x1));
		double speed[] = new double[2];
		speed[0] = Math.cos(w);
		speed[1] = Math.sin(w);
		return speed;
	}

}
