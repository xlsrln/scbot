import bwapi.*;
/**  A resource manager class for minerals and gas. 
 * Methods:
 * 	- updateResources: Updates the known resource bank.
 * 	- canSpend: calls updateResources and checks if it can spend the given amount.
 * 	- spendResource: calls updateResources and adjusts the resource bank accordingly.
 */
public class ResourceManager {
	//Initializing
	public int resources;
	public int allocatedResources;
	public int availableResources;
	
	public LinkedBuilders linkedBuilders;
	public BuilderNode currentBuilderNode;
	
	// Constructor
	public ResourceManager() {
		this.resources = 0;
		this.allocatedResources = 0;
		this.availableResources = 0;
		this.linkedBuilders =  new LinkedBuilders();
		this.currentBuilderNode = null;
	}
	
	// Methods
	/**  Updates the known resource bank
	 * Input: int, int
	 * Example: self.minerals(), self.gas()+
	 */
	public void updateResources(int currentMinerals,int currentGas) {
		updateAllocatedResources();
		resources = currentMinerals;
		availableResources = resources - allocatedResources; 
	}
	
	public void updateAllocatedResources() {
		currentBuilderNode = linkedBuilders.getFirst();
		if (currentBuilderNode != null) {
			// note: !currentBuilderNode.unit.isConstructing() betyder att den har hindrats att bygga
			if (currentBuilderNode.hasBegunConstruction() || !currentBuilderNode.unit.isConstructing()) {
				System.out.println("at line 41");
					allocatedResources -= currentBuilderNode.requiredMin;
					//currentBuilderNode = currentBuilderNode.getNextBuilderNode();
					linkedBuilders.removeFirst();
					currentBuilderNode = linkedBuilders.getFirst();
			}
		}
			
	}
	/** Checks if construction can be afforded the current resource bank and a building price.
	 * Input: int, int, int, int
	 * Example: self.minerals(), self.gas(), 150, 100
	 * Output: boolean
	 */
	public boolean canSpend(int currentMinerals, int currentGas, int mineralExpense,int gasExpense) {
		updateResources(currentMinerals,currentGas);
		return (availableResources > mineralExpense);
	}
	/** Allocates resources for building construction given a worker, the current resource bank and a building price.
	 * Input: UnitType , int, int, int, int
	 * Example: my Unit, self.minerals(), self.gas(), 150, 100
	 */
	public void spendResource(Unit unit,int currentMinerals, int currentGas,int mineralExpense,int gasExpense) {
		
		allocatedResources += mineralExpense;
		updateResources(currentMinerals,currentGas);
		linkedBuilders.addLast(unit,mineralExpense, gasExpense);

	}
}
// TODO:
//1. hasBegunConstruction funkar inte då rörelsen till platsen räknas som constructing...