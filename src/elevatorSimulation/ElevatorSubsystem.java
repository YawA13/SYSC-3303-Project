package elevatorSimulation;


/**
 * Code to handle the elevator subsystem's tasks for Milestone 1 SYSC 3303.
 * ElevatorSubsystem class will receive and send instructions to floor class
 * through synchronized methods in the scheduler class
 * 
 * @author Group 3, SYSC 3303
 * @version Milestone 1
 * @since 2023-02-04
 */
public class ElevatorSubsystem extends Thread
{
	private Scheduler scheduler;
	private Instruction instruction;
	
	/**
	 * Initialize Elevator subsystem
	 * 
	 * @param scheduler Object to synchronize information between elevator and floor class
	 */
	public ElevatorSubsystem (Scheduler scheduler)
	{
		this.scheduler = scheduler;
	}
	
	/**
	 * Start Elevator subsystem thread to repeatedly get and send instructions
	 * to scheduler
	 */
	public void run()
	{
		while (true)
		{
			getInstructions();
			sendInstructions();
		}
		
	}
	
	/**
	 * Get Instruction object from scheduler class and print instruction object
	 * that was received
	 */
	private void getInstructions()
	{
		instruction = scheduler.getInstructionForElevator();
		System.out.println("ElevatorSubsystem received Intructions: "+instruction);
	}
	
	/**
	 * Print statement showing elevator subsystem is sending an Instruction object
	 * and pass object to scheduler method
	 */
	private void sendInstructions()
	{
		System.out.println("ElevatorSubsystem sending instructions");
		scheduler.setInstructionsFromElevator(instruction);
	}

}
