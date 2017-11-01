import bwapi.*;

public class LinkedBuilders{
	// Initializing
	public BuilderNode head;
	public BuilderNode tail;
	public BuilderNode temp;
	// Constructor
//	public LinkedBuilders(Unit unit, int requiredMin, int requiredGas) {
//		head = new BuilderNode(unit, requiredMin, requiredGas,null);
//	}
	public LinkedBuilders() {
		head = null;
		tail = null;
	}
	
	//Methods
	public boolean isEmpty() {
		return (head == null);
	}
	/*
	public void addFirst(Unit unit,int requiredMin,int requiredGas) {
		head = new BuilderNode(unit,requiredMin, requiredGas,head);
	}
	*/
	public void addLast(Unit unit,int requiredMin,int requiredGas) {
		if (isEmpty()) {
			head = tail = new BuilderNode(unit,requiredMin, requiredGas,null); 
		}
		else if (head.nextBuilderNode == null){
			tail = new BuilderNode(unit,requiredMin, requiredGas,null);
			head.nextBuilderNode = tail;
		}
		else {
			temp = new BuilderNode(unit,requiredMin, requiredGas,null);
			tail.nextBuilderNode = temp;
			tail = temp;
		}
	}
	
	public BuilderNode getFirst() {
		return head;
	}
	
	public void removeFirst( ) {
		if (isEmpty()) {
			
		}
		else if (head.nextBuilderNode == null) {
			head = tail = null;
		}
		else {
			head = head.nextBuilderNode;
		}
	}
}
