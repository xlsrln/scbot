import bwapi.*;

public class Resource {
	//Initializing
	public int currentResources;
	public int allocatedResources;
	public int availableResources;
	
	private Player self;
	
	// Constructor
	public Resource() {
		this.currentResources = self.minerals();
		this.allocatedResources = 0;
		this.availableResources = this.currentResources;
	}
	
	// Methods
	public void updateResources() {
		currentResources = self.minerals();
		availableResources = currentResources - allocatedResources; 
	}
	
	
	public boolean canSpend(int expense) {
		updateResources();
		return (availableResources > expense);
	}
	
	public void spendResource(int expense, Unit unit) {
		allocatedResources += expense;
		updateResources();
		
	}
}
