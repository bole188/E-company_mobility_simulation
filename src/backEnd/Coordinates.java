package backEnd;

public class Coordinates {
	public int X;
	public int Y;
	public Coordinates() {
		
	}
	
	public Coordinates(int a,int b) {
		this.X = a;
		this.Y = b;
	}
	
	public int getX() {
		return this.X;
	}
	public int getY() {
		return this.Y;
	}
	public void setCoordinates(int a,int b) {
		this.X = a;
		this.Y = b;
	}
}
