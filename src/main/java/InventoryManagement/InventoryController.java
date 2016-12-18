package InventoryManagement;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class InventoryController {

    @RequestMapping(value="/inventory-info",method=RequestMethod.GET)
    public InventoryInfo getInventoryInfo(@RequestParam(value="deviceid") String deviceid,
	                                      @RequestParam(value="wt") String wt ) {
        return new InventoryInfo(deviceid,
                            wt);
    }
	
	@RequestMapping(value="/inventory-info",method=RequestMethod.POST)
    public InventoryInfo setInventoryInfo(@RequestParam(value="deviceid") String deviceid,
	                                      @RequestParam(value="wt") String wt ) {
        //SFConnector sf_connector = new SFConnector("TBD","TBD","TBD","TBD");
        //sf_connector.login(); 	
		//sf_connector.updateInventory(deviceid, wt);
		
		return new InventoryInfo(deviceid,
                            wt);
    }
	
}
