import java.util.LinkedList;
import java.util.ListIterator;

import bwapi.*;
/**  A resource manager class for minerals and gas. 
 * Methods:
 * 	- updateResources: Updates the known resource bank.
 * 	- canSpend: calls updateResources and checks if it can spend the given amount.
 * 	- spendResource: calls updateResources and adjusts the resource bank accordingly.
 */
public class ResourceManager {
	//Initializing
	public int totalMinerals;
	public int allocatedMinerals;
	public int availableMinerals;
	
	public int totalGas;
	public int allocatedGas;
	public int availableGas;
	
	public BuilderNode currentBuilderNode;
	
	public LinkedList<BuilderNode> queueOfBuilders;
	public ListIterator<BuilderNode> queueIterator;

	// Constructor
	public ResourceManager() {
		this.totalMinerals = 0;
		this.allocatedMinerals = 0;
		this.availableMinerals = 0;
		this.totalGas = 0;
		this.allocatedGas = 0;
		this.availableGas = 0;
		this.currentBuilderNode = null;
		this.queueOfBuilders = new LinkedList<BuilderNode>();
		this.queueIterator = null;

	}
	
	// Methods
	/**  Updates the known resource bank
	 * Input: int, int
	 * Example: self.minerals(), self.gas()
	 */
	public void updateResources(int currentMinerals,int currentGas) {
		
		updateAllocatedResources();
		totalMinerals = currentMinerals;
		totalGas = currentGas;
		availableMinerals = totalMinerals - allocatedMinerals; 
		availableGas = totalGas - allocatedGas;
		if (availableMinerals <0) {
			System.out.println("ERROR in ResourceManager: Available resources should not be negative");	
		}
	}
	/*
	public void updateAllocatedResources() {
		linkedBuilders.length();
		currentBuilderNode = linkedBuilders.getFirst();
		if (currentBuilderNode != null) {
			// note: !currentBuilderNode.unit.isConstructing() betyder att den har hindrats att bygga
			if (currentBuilderNode.hasBegunConstruction() || !currentBuilderNode.unit.isConstructing()) {
					allocatedMinerals -= currentBuilderNode.getRequiredMinerals();
					allocatedGas -= currentBuilderNode.getRequiredGas();
					linkedBuilders.removeFirst();
			}
		}
			
	}
	*/
	
	public void updateAllocatedResources() {
		queueIterator = queueOfBuilders.listIterator(0);
		allocatedMinerals = 0;
		allocatedGas = 0;
		/*if (!queueOfBuilders.isEmpty()) {
			System.out.println(Integer.toString(queueOfBuilders.getFirst().getRequiredMinerals()));
		}*/
		while (queueIterator.hasNext()) {
			currentBuilderNode = queueIterator.next();
			if (currentBuilderNode.hasBegunConstruction() || !currentBuilderNode.unit.exists() 
					|| currentBuilderNode.unit.isGatheringMinerals() || currentBuilderNode.unit.isGatheringGas()) {
				queueOfBuilders.removeFirst();
			}
			else {
				allocatedMinerals += currentBuilderNode.getRequiredMinerals();
				allocatedGas += currentBuilderNode.getRequiredGas();
			}
		}
		
	}
	/*
 	public void updateAllocatedResources() {
		System.out.println(queueOfBuilders.size());
		if (!queueOfBuilders.isEmpty()) {
			currentBuilderNode = queueOfBuilders.getFirst();
			// note: !currentBuilderNode.unit.isConstructing() betyder att den har hindrats att bygga
			if (currentBuilderNode.hasBegunConstruction() || !currentBuilderNode.unit.isConstructing()) {
					allocatedMinerals -= currentBuilderNode.getRequiredMinerals();
					allocatedGas -= currentBuilderNode.getRequiredGas();
					queueOfBuilders.removeFirst();
			}
		}
	}
	 */
	
	/** Checks if construction can be afforded the current resource bank and a building price.
	 * Input: int, int, int, int
	 * Example: self.minerals(), self.gas(), 150, 100
	 * Output: boolean
	 */
	public boolean canSpend(int currentMinerals, int currentGas, int mineralExpense,int gasExpense) {
		updateResources(currentMinerals,currentGas);
		return (availableMinerals >= mineralExpense && availableGas >= gasExpense);
	}
	/** Allocates resources for building construction given a worker, the current resource bank and a building price.
	 * Input: UnitType , int, int, int, int
	 * Example: my Unit, self.minerals(), self.gas(), 150, 100
	 */
	public void spendResource(Unit unit,int currentMinerals, int currentGas,int mineralExpense,int gasExpense) {
		
		if (canSpend(currentMinerals,currentGas,mineralExpense,gasExpense)) {		
			queueOfBuilders.addLast(new BuilderNode(unit,mineralExpense, gasExpense,null));
		}
		else {
		System.out.println("ERROR in ResourceManager.spendResource: Tried to spend more than available");
		}
	}
	/*	public void spendResource(Unit unit,int currentMinerals, int currentGas,int mineralExpense,int gasExpense) {
		
		if (canSpend(currentMinerals,currentGas,mineralExpense,gasExpense)) {
			allocatedMinerals += mineralExpense;
			allocatedGas += gasExpense;
			updateResources(currentMinerals,currentGas);
			linkedBuilders.addLast(unit,mineralExpense, gasExpense);
		}
		else {
		System.out.println("ERROR in ResourceManager.spendResource: Tried to spend more than available");
		}
	}
	 */
}
// TODO:
//1. hasBegunConstruction funkar inte då rörelsen till platsen räknas som constructing...