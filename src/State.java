import bwapi.*;

public class State {
	// Initializing
	public int supply;
	public UnitType unitType;
	public State nextState;
	
	// Constructor
	public State(int supply, UnitType unitType, State nextState ) {
		this.supply= supply;
		this.unitType = unitType;
		this.nextState = nextState;
	}
	
	public State() {
		super();
	}
	
	// Methods
	public int getSupply() {
		return supply;
	}
	
	public UnitType getUnitType() {
		return unitType;
	}
	
	public State getNext() {
		return nextState;
	}
	
	public void setSupply(int newSupply) {
		supply = newSupply;
	}
	
	public void setUnitType(UnitType newUnitType) {
		unitType = newUnitType;
	}
	
	public void setNext(State newState) {
		nextState = newState;
	}
}