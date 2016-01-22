package vazkii.botania.api.corporea;

public class CorporeaRequest {
	Object matcher;
	boolean checkNBT;
	int count;
	int foundItems = 0;
	int extractedItems = 0;
	
	public CorporeaRequest(Object matcher, boolean checkNBT, int count) {
		super();
		this.matcher = matcher;
		this.checkNBT = checkNBT;
		this.count = count;
	}

}
