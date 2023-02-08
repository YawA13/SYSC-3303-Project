package elevatorSimulation;


/**
 * Code to start the Elevator simulation for Milestone 1 SYSC 3303
 * 
 * @author Group 3, SYSC 3303
 * @version Milestone 1
 * @since 2023-02-04
 */
public class ElevatorSimulation {
	
	public static void main (String [] args)
	{
		
		Thread scheduler = new Scheduler ();
		//Create Elevator and floor objects and pass scheduler to constructor to share information between them 
		Thread elevatorSubsytem = new ElevatorSubsystem ((Scheduler) scheduler);
		Thread floorSubsystem = new FloorSubsystem ((Scheduler) scheduler, "InputInstructions.txt");
		
		//Start all threads
		scheduler.start();
		elevatorSubsytem.start();
		floorSubsystem.start();
		
		
	}

}
