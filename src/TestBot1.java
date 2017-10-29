import bwapi.*;
import bwta.BWTA;
import bwta.BaseLocation;

public class TestBot1 extends DefaultBWListener {

    private Mirror mirror = new Mirror();

    private Game game;

    private Player self;
    
    private Position startingPosition;
    
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
    	game.drawTextScreen(300, 300, "test");
    	for(Unit myUnit : self.getUnits()) 
    	{
            //if there's enough minerals, train an SCV
            if (myUnit.getType() == UnitType.Terran_Command_Center && self.minerals() >= 50 && !myUnit.isTraining()) {
                myUnit.train(UnitType.Terran_SCV);
            }
            
            //if there's enough minerals, train a marine
            if (myUnit.getType() == UnitType.Terran_Barracks && self.minerals() >= 50 && !myUnit.isTraining()) {
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
    
    @Override
    public void onFrame()
    {
        macroCycle();
        armyManager();
        writeAllUnitsOnScreen();
    }

	public static void main(String[] args) {
        new TestBot1().run();
    }
}