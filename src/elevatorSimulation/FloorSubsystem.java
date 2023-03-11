package elevatorSimulation;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Code to handle the floor subsystem's tasks for Milestone 1 SYSC 3303.
 * FloorSubsystem class will read a text file and send and receive instructions
 * to elevator class through synchronized methods in the scheduler class
 * 
 * @author Group 3, SYSC 3303
 * @version Milestone 1
 * @since 2023-02-04
 */
public class FloorSubsystem extends Thread
{

	private Scheduler scheduler; //variable to hold scheduler object
	private String inputLoc; //variable to hold location of instructions text file
	private List<Instruction> instructions; //variable to hold list of instructions from text file
	private ArrayList<Floor> floors = new ArrayList<>();
	
	/**
	 * Initialize Floor subsystem
	 * 
	 * @param scheduler Scheduler object to synchronize information between elevator and floor class
	 * @param inputLoc String location of text file to read
	 */
	public FloorSubsystem(Scheduler scheduler, String inputLoc, int numOfFloors, int numOfElevators)
	{
		this.scheduler = scheduler;
		this.inputLoc = inputLoc;
		this.scheduler.setFloorSubsystem(this);
		instructions = new ArrayList<>(); //Initialize Array list to hold instruction's
		for (int i = 0; i < numOfFloors; i++)
		{
			floors.add(new Floor(this, (i+1), numOfElevators));
		}
	}
	
	/**
	 * Start Elevator subsystem thread to convert instructions in text file to
	 * array list of Instruction objects and send each instruction line to scheduler
	 * and receive before moving to next instruction line. Program will then exit
	 */
	public void run()
	{
		readInput(); //Convert text file to list of instructions
		
		for (int i = 0; i < instructions.size(); i++)
		{
			int floorInstructionIndex = instructions.get(i).getFloor() - 1; 
			
			if (i != 0)
			{
				long diffInMilli = java.time.Duration.between(instructions.get(0).getTime(), instructions.get(i).getTime()).toMillis();
				instructions.get(i).setTimeAfterInital(diffInMilli);
			}
			
			floors.get(floorInstructionIndex).addRequest(instructions.get(i));
		}
		
		
		for (int i = 0; i < floors.size(); i++)
		{
			floors.get(i).startFloorRequest();
		}
		
		while (true)
		{
			
		}
		
		//System.exit(0); //Program will exit 
			
	}
	
	/**
	 * Print statement showing floor subsystem is sending an Instruction object
	 * and pass object to scheduler method
	 */
	public void sendInstruction(Instruction newInstruction)
	{
		scheduler.setInstructionsFromFloor(newInstruction);
	}
	
	/**
	 * Get Instruction object from scheduler class and print instruction object
	 * that was received
	 */
	private void getInstruction()
	{
		//Instruction newInstruction = scheduler.getInstructionForFloor();
		//System.out.println("FloorSubsystem has received Intructions: "+ newInstruction + "\n");
	}
	
	/**
	 * Convert instructions text file to list of instructions
	 */
	public void readInput()
	{
		try {
			//Create and initialize file and scanner objects to read text file
			File inputInstructions = new File(inputLoc);
			Scanner scan = new Scanner(inputInstructions);
			
			scan.nextLine(); //Ignore first line (header of table)
			
			//While there are more instructions create instruction object and 
			//add to list
			while (scan.hasNextLine())
			{
				String data = scan.nextLine();
				instructions.add(new Instruction(data));
				
			}
			scan.close(); //Close scanner
		} catch (FileNotFoundException e) { //Catch exception and print error
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @return returns the list of instructions held by the floor subsystem
	 */
	public List<Instruction> getInstructionsAsList(){
		return this.instructions; 
	}
	
	/**
	 * function used to test the getInstruction() function
	 */
	public void testGetInstruciton() {
		getInstruction(); 
	}
	/**
	 * function used to test the sendInstruction() function
	 * @param int n is the index of the instruction we want to test
	 */
	public void testSendInstruciton(int n) {
		sendInstruction(instructions.get(n)); 
	}
	
	
	/**
	public void elevatorStopFloor(int elevatorCarNum, int currentFloorNum, Instruction currentRequest)
	{
		//Since arraylist, starts at 0 and floor start at 1
		currentFloorNum--;
		
		//when reach floor set lamps for direction on, and floor lamp is turned off
		floors.get(currentFloorNum).setLampWhenElevatorReachFloor(elevatorCarNum, currentRequest);
		//floors.get(currentRequest.getFloor()).setLampWhenElevatorReachFloor(currentRequest);
			
		//for previous floor reached, set lamp for direction off
		int previousFloor;
		if ((currentRequest.getButtonStatus() == ButtonStatus.Up) && (currentFloorNum > 1))
		{
			previousFloor = currentFloorNum - 1;
			floors.get(previousFloor).setLampWhenElevatorLeaves(elevatorCarNum);
		}
		else if (((currentRequest.getButtonStatus() == ButtonStatus.Down) && (currentFloorNum < floors.size() - 1)))
		{
			previousFloor = currentFloorNum + 1;
			floors.get(previousFloor).setLampWhenElevatorLeaves(elevatorCarNum);
		}
			
	}
	**/
	
	public void elevatorReachFloor(int elevatorCarNum, int currentFloorNum, ButtonStatus currentDirection, boolean isElevatorStopping)
	{
		//Since arraylist, starts at 0 and floor start at 1
		currentFloorNum--;
		
		//when reach floor set lamps for direction on, and floor lamp is turned off
		floors.get(currentFloorNum).setLampWhenElevatorReachFloor(elevatorCarNum, currentDirection, isElevatorStopping);
		//floors.get(currentRequest.getFloor()).setLampWhenElevatorReachFloor(currentRequest);
			
		//for previous floor reached, set lamp for direction off
		
		if ((currentFloorNum < floors.size() - 1) && (currentFloorNum > 0))
		{
			floors.get(currentFloorNum - 1).setLampWhenElevatorLeaves(elevatorCarNum);
			floors.get(currentFloorNum + 1).setLampWhenElevatorLeaves(elevatorCarNum);
		}
		else if (currentFloorNum < floors.size() - 1)
		{
			floors.get(currentFloorNum+1).setLampWhenElevatorLeaves(elevatorCarNum);
		}
		else
		{
			floors.get(currentFloorNum - 1).setLampWhenElevatorLeaves(elevatorCarNum);
		}
			
	}
	
	
}
