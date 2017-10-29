import bwapi.*;
import bwta.BWTA;
import bwta.BaseLocation;

public class TestBot1 extends DefaultBWListener {

    private Mirror mirror = new Mirror();

    private Game game;

    private Player self;
    
    private Position startingPosition;
    
    //private Integer availableMinerals = 0;
    
    private State currentState;

	private static State state1;
	private static State state2;
	private static State state3;
	private static State state4;
	private static State state5;

	
    
    public void run() {
        mirror.getModule().setEventListener(this);
        mirror.startGame();
    }

    @Override
    public void onUnitCreate(Unit unit) {
        System.out.println("New unit discovered " + unit.getType());
    }

    public void macroCycle()
    {
    	//Integer self.minerals() = self.minerals();
    	game.drawTextScreen(300, 300, Integer.toString(self.minerals()));
    	for(Unit myUnit : self.getUnits()) 
    	{
    		
            //if there's enough minerals, train an SCV
            if (myUnit.getType() == UnitType.Terran_Command_Center && self.minerals() >= 50 && !myUnit.isTraining()) {
                myUnit.train(UnitType.Terran_SCV);
                //self.minerals() -= 50;
            }
            
            //if there's enough minerals, train a marine
            if (myUnit.getType() == UnitType.Terran_Barracks && self.minerals() >= 50 && !myUnit.isTraining()) {
                myUnit.train(UnitType.Terran_Marine);
                //self.minerals() -= 50;
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
        
        //hardcoded stuff
        /*state1 = new State();
        state2 = new State();
        state3 = new State();
        state4 = new State();
        state5 = new State();
        
        state1.setNext(state2);
        state2.setNext(state3);
        state3.setNext(state4);
        state4.setNext(state5);
        state5.setNext(state5);
        
        state1.setUnitType(UnitType.Terran_Supply_Depot);
        state1.setSupply(8);
        
        state2.setUnitType(UnitType.Terran_Barracks);
        state2.setSupply(12);
        
        state3.setUnitType(UnitType.Terran_Barracks);
        state3.setSupply(11);
        
        state4.setUnitType(UnitType.Terran_Supply_Depot);
        state4.setSupply(15);
        
        state5.setUnitType(UnitType.Terran_Barracks);
        state5.setSupply(200);
        
        currentState = state1;*/
        

    }
    
    public void armyManager()
    {
    	//Position place = new Position(500,500);
    	// send all army to nearest chokepoint
    	Position place = BWTA.getNearestChokepoint(startingPosition).getCenter();
    	
    	for(Unit unit : game.getAllUnits())
    	{
    		if( !unit.getType().isWorker() && unit.canAttack() )
    		{
    			unit.attack(place);
    		}
    	}
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
    	//System.out.print(self.minerals());
		for (Unit myUnit : self.getUnits()) {
			// If it is a worker, check build order.
			/*if (myUnit.getType().isWorker()) {
				game.drawTextScreen(200, 300, Integer.toString(currentState.getSupply()));
				// If we are above the demanded supply for the next step in the build, build the
				// building and advance the build
				if (!(currentState == null) )
				{
					if( currentState.getSupply()*2 <= self.supplyUsed()
						&& (self.minerals() > (currentState.getUnitType().mineralPrice() + 50))) {
					buildBuilding(currentState.getUnitType(), myUnit);
					currentState = currentState.getNext();
					game.drawTextScreen(200, 200, "changed state");
					// currentState = null;
					// break;
					}
				}
				else
				{
					System.out.print("error");
					break;
				}
			}*/
			
			//hardcode istället -- har vissa problem, alla scvs vill bygga
			if(myUnit.getType().isWorker() && !myUnit.isConstructing() && self.supplyTotal()-self.supplyUsed()<4 && self.minerals() > 100)
			{
				buildBuilding(UnitType.Terran_Supply_Depot,myUnit);
				//self.minerals() -= 100;
				break;
			}
			
			if(myUnit.getType().isWorker() && !myUnit.isConstructing() && self.minerals() > 200)
			{
				buildBuilding(UnitType.Terran_Barracks,myUnit);
				//self.minerals() -= 150;
				break;
			}
		}
	}
    
    @Override
    public void onFrame()
    {
    	stuffBuilder();
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
    	TilePosition buildTile = getBuildTile(myUnit, myBuilding,self.getStartLocation());
		if (buildTile != null) {
			myUnit.build(myBuilding, buildTile);
		}   
    }
}