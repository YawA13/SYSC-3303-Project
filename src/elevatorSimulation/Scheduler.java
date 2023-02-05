package elevatorSimulation;

/**
 * Code to create Scheduler thread object for Milestone 1 SYSC 3303.
 * Scheduler object will have synchronized methods in order for elevator class
 * and Floor class can pass instructions back and forth
 * 
 * @author Group 3, SYSC 3303
 * @version Milestone 1
 * @since 2023-02-04
 */
public class Scheduler extends Thread
{
	Instruction instruction; //Variable to hold instruction object
	private boolean instructionForElevator; //Variable to see if instruction is meant for elevator
	private boolean instructionForFloor; //Variable to see if instruction is meant for floor
	
	/**
	 * Initialize scheduler object
	 */
	public Scheduler()
	{
		instruction = null;
		instructionForElevator = false;
		instructionForFloor = false;
	}
	
	/**
	 * Scheduler thread that does nothing in this iteration
	 */
	public void run()
	{
		while(true)
		{
			
		}
	}

	/**
	 * Set instruction method to be called from floor class that will save the
	 * instruction
	 * 
	 * @param instruction Instruction object to be saved
	 */
	public synchronized void setInstructionsFromFloor(Instruction instruction) 
	{
		//Until there are no instructions for floor or elevator wait
		while (instructionForFloor || instructionForElevator)
		{
			try { 
                wait();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
		}
		
		System.out.println("Scheduler received instructions from floor");
		this.instruction = instruction; //Save instruction object
		instructionForElevator = true; //Set true to allow elevator to get instruction object in other synchronized method
		notifyAll(); //Notify synchronized methods
	}

	/**
	 * Set instruction method to be called from elevator class to retrieve 
	 * instruction object
	 * 
	 * @return Instruction object
	 */
	public synchronized Instruction getInstructionForElevator() 
	{
		//While there are no instructions for elevator wait
		while (!instructionForElevator)
		{
			try { 
                wait();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
		}
		
		System.out.println("Scheduler sending instructions to elevator");
		instructionForElevator = false; //Set false to allow elevator to set instruction object in other synchronized method
		notifyAll(); //Notify synchronized methods
		return instruction;
	}

	public synchronized void setInstructionsFromElevator(Instruction instruction) 
	{
		//While there are instructions available wait 
		while (instructionForElevator)
		{
			try { 
                wait();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
		}
		
		System.out.println("Scheduler received instructions from elevator");
		this.instruction = instruction; //Save instruction object
		instructionForFloor = true; 
		notifyAll(); //Notify synchronized methods
		
	}
	
	public synchronized Instruction getInstructionForFloor() 
	{
		//While there are no instructions for floor wait
		while (!instructionForFloor)
		{
			try { 
                wait();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
		}
		
		System.out.println("Scheduler sending instructions to Floor");
		instructionForFloor = false; //Set false to allow floor to set instruction object in other synchronized method
		notifyAll(); //Notify synchronized methods
		return instruction;
	}
	
}
