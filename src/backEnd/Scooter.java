package backEnd;

public class Scooter extends Vehicle {
	private int maxSpeed;
	
	public int getMaxSpeed() {
		return maxSpeed;
	}
	public Scooter(String id,String mn,String m,int priceOfAcq,int ms,String bd){
		super(id,mn,m,priceOfAcq,bd);
		this.maxSpeed = ms;
	}
}
