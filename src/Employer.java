import bwapi.*;
import bwta.BWTA;
import bwta.BaseLocation;

public class Employer {
	Unit unit = null;
	
	public Unit findWorker() {
		for(Unit u : self.getUnits())
		{
			if(u.getType().isWorker())
			{
				return u;
			}
		}
	}
	
	//methods to do stuff with the worker, i.e. build a building
}