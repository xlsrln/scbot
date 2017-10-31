import bwapi.*;

public class Employer {
	Unit unit = null;
	
	public Unit findWorker() {
		Unit unit = null;
		for(Unit u : self.getUnits())
		{
			if(u.getType().isWorker())
			{
				unit = u;
			}
		}
<<<<<<< Updated upstream
=======
		return unit;
		
>>>>>>> Stashed changes
	}
	
	//methods to do stuff with the worker, i.e. build a building
}