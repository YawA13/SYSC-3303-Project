package elevatorSimulation;

import java.util.ArrayList;

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
		int numOfloors = 5;
		int numOfElevators = 2;
		
		//Create Elevator and floor objects and pass scheduler to constructor to share information between them 
		//Thread elevatorSubsytem = new ElevatorSubsystem ((Scheduler) scheduler, numOfFloors, 0);
		Thread scheduler = new Scheduler ();
		ArrayList<Thread> elevatorSubsystems = new ArrayList<>();
		Thread floorSubsystem = new FloorSubsystem ((Scheduler) scheduler, "InputInstructions.txt", numOfloors, numOfElevators);
		
		for(int i = 0; i < numOfElevators; i++)
		{
			elevatorSubsystems.add(new ElevatorSubsystem ((Scheduler) scheduler, numOfloors, i));
		}
		
		//Start all threads
		scheduler.start();
		floorSubsystem.start();
		for(int i = 0; i < numOfElevators; i++)
		{
			elevatorSubsystems.get(i).start();
		}
		
	}

}
