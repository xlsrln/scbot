import bwapi.*;


public class LinkedStates extends State {
	// Initializing
	public State head;
	
	
	// Constructor
	public LinkedStates() {
		head = new State(400, UnitType.Terran_Barracks, null);
	}
	
	// Methods
	public boolean isEmpty() {
		return (head.supply >= 400);
	}
	
	public void addFirst(int supply, UnitType unitType) {
		head = new State(supply, unitType, this.head );
		
	}
	public State getFirst() {
		
	      if(isEmpty()) {
	    	  return new State(400, UnitType.Terran_Barracks, null);
	      }
	      else {
			return head;
	      }
	}
	
}
