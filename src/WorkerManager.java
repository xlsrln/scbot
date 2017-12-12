import java.util.*;
import bwapi.*;

/**  A unit counter class, keeps track of the total amount of stuff. For example to not overmake workers.
 * Methods:
 *  - "TBA": "description."
 */

public class WorkerManager {
	private ArrayList<Unit> workerList;
	private ArrayList<Unit> commandCenterList;
	
	public WorkerManager() {
		this.workerList = new ArrayList<Unit>();
		this.commandCenterList = new ArrayList<Unit>();
	}
	
	public void addWorker(Unit unit) {
		workerList.add(unit);
	}
	
	public void addCommandCenter(Unit unit) {
		commandCenterList.add(unit);
	}
	
	public int numWorkers() {
		return workerList.size();
	}
	
	public int numCommandCenters() {
		return commandCenterList.size();
	}
		
}
