import bwapi.*;

public class LinkedBuilders extends BuilderNode {
	// Initializing
	public BuilderNode head;
	public BuilderNode temp;
	
	// Constructor
	public LinkedBuilders(Unit unit) {
		head = new BuilderNode(unit,null);
	}
	
	//Methods
	public void addLast(Unit unit) {	
		temp = new BuilderNode(unit,null);
		head.nextBuilderNode = temp;
	}
	
	public BuilderNode getFirst() {
		return head;
	}
}
