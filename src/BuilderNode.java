import bwapi.*;

public class BuilderNode {
	// Initializing
	public Unit unit;
	public int requiredMin;
	public int requiredGas;
	public BuilderNode nextBuilderNode;
	
	
	// Constructor
	public BuilderNode(Unit unit, int requiredMin, int requiredGas, BuilderNode nextBuilderNode) {
		this.unit = unit;
		this.requiredMin = requiredMin;
		this.requiredGas = requiredGas;
		this.nextBuilderNode = nextBuilderNode;
	}
	
//	public BuilderNode(Unit unit,int requiredMin, int requiredGas) {
//		this.unit = unit;
//		this.requiredMin = requiredMin;
//		this.requiredGas = requiredGas;	
//		this.nextBuilderNode = null;
//	}
//	
//	public BuilderNode() {
//		this.unit = null;
//		this.requiredMin = 0;
//		this.requiredGas = 0;	
//		this.nextBuilderNode = null;
//	}
	
	// Methods
	public boolean hasBegunConstruction() {
		if (unit == null) {
			return false;
		}
		else {
			return unit.canHaltConstruction(); // TERRAN HARDCODED...	
					
		}				
	}
	
	public Unit getBuilder() {
		return unit;
	}
	
	public BuilderNode getNextBuilderNode() {
		return nextBuilderNode;
	}
	
	public void setBuilder(Unit unit) {
		this.unit = unit;
	}
	
	public void setNextBuilderNode(BuilderNode nextBuilderNode) {
		this.nextBuilderNode = nextBuilderNode;
	}
}
