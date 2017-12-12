import bwapi.UnitType;


/**  A supply manager class, makes sure that a reasonable amount of supply depots are constructed concurrently. 
 * Methods:
 * 	- canConstruct: returns true if not too many supply depots are being built.
 * 	- construct: increases internal counter for supply depots being constructed.
 * 	- complete: decreases internal counter for supply depots being constructed.
 *  - "TBA": are currently not used.
 */


//UNDER CONSTRUCTION (Markus 17-12-06)
// TODO: isConstructing will be wrong if a building supply is canceled or destroyed before completion.
// we got an error once that we were unable to replicate. Stay sharp! Stopped building supply once at 18 supply.  
public class SupplyManager {
	int isConstructing = 0;
	int maxSupply = 1;
	
	public SupplyManager() {}
	
	public int getSupplyMax() {
		return maxSupply;
	}
	
	public void setSupplyMax(int x) {
		maxSupply = x;
	}
	
	public boolean canConstruct() {
		return isConstructing < maxSupply;
	}
	
	public void construct(){
		isConstructing += 1;
	}
	
	public void complete(){
		isConstructing -= 1;
	}
	
	public int getConstructingSupply() {
		return isConstructing;
	}
	
}
