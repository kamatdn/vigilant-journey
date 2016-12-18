package InventoryManagement;

public class InventoryInfo {

    private final String deviceid;
	private final String wt;
	
	public InventoryInfo(String deviceid, String wt) {
		this.deviceid = deviceid;
		this.wt = wt;
	}

    public String getdeviceId() {
        return deviceid;
    }
	
	public String getWt() {
		return wt;
	}
}