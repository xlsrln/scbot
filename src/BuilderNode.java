import bwapi.*;

public class BuilderNode {
	// Initializing
	public Unit unit;
	public BuilderNode nextBuilderNode;
	
	// Constructor
	public BuilderNode(Unit unit, BuilderNode nextBuilderNode) {
		this.unit = unit;
		this.nextBuilderNode = nextBuilderNode;
	}
	
	public BuilderNode(Unit unit) {
		this.unit = unit;
		this.nextBuilderNode = null;
	}
	
	public BuilderNode() {
		super();
	}
	
	// Methods
	public boolean hasBegunConstruction() {
		return unit.isConstructing();
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
