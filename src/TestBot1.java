import bwapi.*;
import bwta.BWTA;
import bwta.BaseLocation;

public class TestBot1 extends DefaultBWListener {

    private Mirror mirror = new Mirror();

    private Game game;

    private Player self;
    
    private Position startingPosition;
    
    // LinkedStates initialization
    private LinkedStates buildOrder;
    private State currentState;
    
    // Initialization
    private ResourceManager resourceManager;
    private SupplyManager supplyManager;
    private WorkerManager workerManager;

    public void run() {
        mirror.getModule().setEventListener(this);
        mirror.startGame();
    }

    @Override
    public void onUnitCreate(Unit unit) {
        System.out.println("New unit discovered " + unit.getType() );
    }
    
    @Override
    public void onUnitComplete(Unit unit) {
    	if(unit.getType() == UnitType.Terran_Supply_Depot) {
    		supplyManager.complete();
    		} 
    	if (unit.getType().isWorker()) {
    		workerManager.addWorker(unit);
    		} 
    	if (unit.getType() == UnitType.Terran_Command_Center) {
    		workerManager.addCommandCenter(unit);
    	}
    	if (unit.getType() == UnitType.Terran_Marine) {
    		Position place = BWTA.getNearestChokepoint(startingPosition).getCenter();
        	unit.attack(place);
    	}
    	
    }

    public void macroCycle()
    {
    	//Integer self.minerals() = self.minerals();
    	for(Unit myUnit : self.getUnits()) 
    	{
    		
            //if there's enough minerals, train an SCV. Number of workers are capped at 22*#CC
            if (myUnit.getType() == UnitType.Terran_Command_Center
            						&& resourceManager.canSpend(self.minerals(), self.gas(), 50, 0) 
            						&& !myUnit.isTraining()    
            						&& workerManager.numWorkers() < workerManager.numCommandCenters()*22) {
                myUnit.train(UnitType.Terran_SCV);
            }
            
            //if there's enough minerals, train a marine
            if (myUnit.getType() == UnitType.Terran_Barracks && resourceManager.canSpend(self.minerals(), self.gas(), 50, 0) && !myUnit.isTraining()) {
                myUnit.train(UnitType.Terran_Marine);
            }
            
            // if it's a worker and it's idle, send it to the closest mineral patch
        	if (myUnit.getType().isWorker() && myUnit.isIdle()) 
        	{
         		Unit closestMineral = null;

         		// find the closest mineral
         		for (Unit neutralUnit : game.neutral().getUnits()) 
         		{
         			if (neutralUnit.getType().isMineralField()) 
         			{
         				if (closestMineral == null || myUnit.getDistance(neutralUnit) < myUnit.getDistance(closestMineral)) 
         				{
         					closestMineral = neutralUnit;
         				}
         			}
         		}

         		// if a mineral patch was found, send the worker to gather it
         		if (closestMineral != null) 
         		{
         			myUnit.gather(closestMineral, false);
         		}
        	}
    	}
    }
    
    @Override
    public void onStart() 
    {
        game = mirror.getGame();
        self = game.self();

        //Use BWTA to analyze map
        //This may take a few minutes if the map is processed first time!
        System.out.println("Analyzing map...");
        BWTA.readMap();
        BWTA.analyze();
        System.out.println("Map data ready");
        
        game.enableFlag(1);
        game.setLocalSpeed(8);
        
        int i = 0;
        for(BaseLocation baseLocation : BWTA.getBaseLocations()){
        	System.out.println("Base location #" + (++i) + ". Printing location's region polygon:");
        	for(Position position : baseLocation.getRegion().getPolygon().getPoints()){
        		System.out.print(position + ", ");
        	}
        	System.out.println();
        }
        
        //find our startingposition
        startingPosition = BWTA.getStartLocation(self).getPosition();
        
        //Buildorder
        
        
        buildOrder = new LinkedStates();

        buildOrder.addFirst(10, UnitType.Terran_Barracks);
        buildOrder.addFirst(10, UnitType.Terran_Barracks);
        buildOrder.addFirst(10, UnitType.Terran_Barracks);
        
        currentState = buildOrder.getFirst();
        
        // Managers
        resourceManager = new ResourceManager();
        supplyManager = new SupplyManager();
        workerManager = new WorkerManager();
    }
    
    public void armyManager()
    {
    	//Position place = new Position(500,500);
    	// send all army to nearest chokepoint
    	Position place = BWTA.getNearestChokepoint(startingPosition).getCenter();
    	
//    	CAUSES THE MARINES TO NOT ATTACK 
//    	for(Unit unit : self.getUnits())
//    	{
//    		if( !unit.getType().isWorker() && unit.canAttack())
//    		{
//    			unit.attack(place);
//    		}
//    	}
    }
    
    private void buildSupply() {  // UNDER CONSTRUCTION (Markus 17-12-06)
    	// Check if a new supply depot is needed and affordable.
    	if (self.supplyTotal()>100) {}
    	else if (self.supplyTotal()>=84) {
    		supplyManager.setSupplyMax(2);
    		// TODO: does seem to be 2/2 and hence only build 1 at a time :(
    	}
    	if (self.supplyUsed() + 8 >= self.supplyTotal() && resourceManager.canSpend(self.minerals(), self.gas(), 100, 0) ) {
    		if (currentState.unitType != UnitType.Terran_Supply_Depot && supplyManager.canConstruct()) {
    			supplyManager.construct();
    			System.out.println(" constr. supply/max: "+ supplyManager.getConstructingSupply()+"/"+supplyManager.getSupplyMax());
    			currentState = new State(0,UnitType.Terran_Supply_Depot,currentState);
    		}
    	}
    }
    
    
    
    private Unit findWorker() {
		Unit unit = null;
    	for(Unit u : self.getUnits()) {
			if(u.isGatheringMinerals())  {
				unit = u;
				break;
			}
		}
		return unit;
	}
    
    private void writeAllUnitsOnScreen() 
    {    
    	//the old code for writing all units on the screen
        game.drawTextScreen(10, 10, "Playing as " + self.getName() + " - " + self.getRace());
    	
    	StringBuilder units = new StringBuilder("My units:\n");
        
    	//iterate through my units
        for (Unit myUnit : self.getUnits()) 
        {
            units.append(myUnit.getType()).append(" ").append(myUnit.getTilePosition()).append("\n");  
        }
        game.drawTextScreen(10, 25, units.toString());
	}
    
    public void stuffBuilder()
    {
    	// Displayes resources on-screen
		resourceManager.updateResources(self.minerals(),self.gas());
		game.drawTextScreen(200, 300, "Next building: " + currentState.getUnitType() + " at " + Integer.toString( currentState.getSupply()) + " supply.");
		game.drawTextScreen(200, 310, "Current Minerals: " + Integer.toString(resourceManager.totalMinerals));
		game.drawTextScreen(200, 320, "Allocated Minerals: " + Integer.toString(resourceManager.allocatedMinerals)); 
		game.drawTextScreen(200, 330, "Available Minerals: " + Integer.toString(resourceManager.availableMinerals));
		game.drawTextScreen(330, 310, "Current Gas: " + Integer.toString(resourceManager.totalGas));
		game.drawTextScreen(330, 320, "Allocated Gas: " + Integer.toString(resourceManager.allocatedGas)); 
		game.drawTextScreen(330, 330, "Available Gas: " + Integer.toString(resourceManager.availableGas));
		
		
		for (Unit myUnit : self.getUnits()) {
			// If it is a worker, check build order.
			if (myUnit.getType().isWorker() && !myUnit.isConstructing() && !myUnit.isBeingConstructed() && myUnit.exists()) {
				// If we are above the demanded supply for the next step in the build, build the
				// building and advance the buildOrder
				if (!(currentState == null) )
				{
					if( (currentState.getSupply()*2 <= self.supplyUsed())
						&& (resourceManager.canSpend(self.minerals(),self.gas(), currentState.getUnitType().mineralPrice(), currentState.getUnitType().gasPrice()))) {
						
						if (!resourceManager.canSpend(self.minerals(),self.gas(), currentState.getUnitType().mineralPrice(), currentState.getUnitType().gasPrice())) {
							System.out.println("Error: Available minerals lower then demanded minerals");
							System.out.println("canSpend bool: " + Boolean.toString(resourceManager.canSpend(self.minerals(),self.gas(), currentState.getUnitType().mineralPrice(), currentState.getUnitType().gasPrice())));
							System.out.print(resourceManager.canSpend(self.minerals(),self.gas(), currentState.getUnitType().mineralPrice(), currentState.getUnitType().gasPrice()));
						}
						// resourceManager.spendResource(myUnit,self.minerals(),self.gas(), currentState.getUnitType().mineralPrice(), currentState.getUnitType().gasPrice());
						buildBuilding(currentState.getUnitType(), myUnit);
						System.out.println(myUnit);
						currentState = currentState.getNext();
						break;
					}
				}
			}
		}
	}
    
    @Override
    public void onFrame()
    {
    	stuffBuilder();
    	buildSupply();
    	macroCycle();
        armyManager();
        writeAllUnitsOnScreen();
    }

	public static void main(String[] args) {
        new TestBot1().run();
    }
	
	
	public TilePosition getBuildTile(Unit builder, UnitType buildingType, TilePosition aroundTile) {
		TilePosition ret = null;
		int maxDist = 3;
		int stopDist = 40;

		// Refinery, Assimilator, Extractor
		if (buildingType.isRefinery()) {
			for (Unit n : game.neutral().getUnits()) {
				if ((n.getType() == UnitType.Resource_Vespene_Geyser)
						&& (Math.abs(n.getTilePosition().getX() - aroundTile.getX()) < stopDist)
						&& (Math.abs(n.getTilePosition().getY() - aroundTile.getY()) < stopDist)) {
					return n.getTilePosition();
				}
			}
		}

		while ((maxDist < stopDist) && (ret == null)) {
			for (int i = aroundTile.getX() - maxDist; i <= aroundTile.getX() + maxDist; i++) {
				for (int j = aroundTile.getY() - maxDist; j <= aroundTile.getY() + maxDist; j++) {
					if (game.canBuildHere(new TilePosition(i, j), buildingType, builder, false)) {
						// units that are blocking the tile
						boolean unitsInWay = false;
						for (Unit u : game.getAllUnits()) {
							if (u.getID() == builder.getID())
								continue;
							if ((Math.abs(u.getTilePosition().getX() - i) < 4)
									&& (Math.abs(u.getTilePosition().getY() - j) < 4))
								unitsInWay = true;
						}
						if (!unitsInWay) {
							return new TilePosition(i, j);
						}
						// creep for Zerg
						if (buildingType.requiresCreep()) {
							boolean creepMissing = false;
							for (int k = i; k <= i + buildingType.tileWidth(); k++) {
								for (int l = j; l <= j + buildingType.tileHeight(); l++) {
									if (!game.hasCreep(k, l))
										creepMissing = true;
									break;
								}
							}
							if (creepMissing)
								continue;
						}
					}
				}
			}
			maxDist += 2;
		}
		if (ret == null)
			game.printf("Unable to find suitable build position for " + buildingType.toString());
		return ret;
	}
	
    public void buildBuilding(UnitType myBuilding, Unit myUnit) {
    	if (myUnit != null) {
	    	resourceManager.spendResource(myUnit,self.minerals(),self.gas(), myBuilding.mineralPrice(), myBuilding.gasPrice());
	    	TilePosition buildTile = getBuildTile(myUnit, myBuilding,self.getStartLocation());
			if (buildTile != null) {
				myUnit.build(myBuilding, buildTile);
			}
		}   
    }
    
}