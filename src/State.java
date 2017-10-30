import bwapi.*;
import bwta.BWTA;
import bwta.BaseLocation;

public class State {
	int supply = 0;
	UnitType unitType = null;
	State nextState = null;
	
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