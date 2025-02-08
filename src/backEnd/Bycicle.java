package backEnd;

public class Bycicle extends Vehicle {
	private int reachWithOneCharging;
	
	public int getReach() {
		return this.reachWithOneCharging;
	}
	public Bycicle(String id,String mn,String m, int priceOfAcq,int rwoc, String bd) {
		super(id,mn,m,priceOfAcq,bd);
		this.reachWithOneCharging = rwoc;
	}
	
}
