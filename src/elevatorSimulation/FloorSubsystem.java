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
	
	/**
	 * Initialize Floor subsystem
	 * 
	 * @param scheduler Scheduler object to synchronize information between elevator and floor class
	 * @param inputLoc String location of text file to read
	 */
	public FloorSubsystem(Scheduler scheduler, String inputLoc)
	{
		this.scheduler = scheduler;
		this.inputLoc = inputLoc;
		instructions = new ArrayList<>(); //Initialize Array list to hold instruction's
	}
	
	/**
	 * Start Elevator subsystem thread to convert instructions in text file to
	 * array list of Instruction objects and send each instruction line to scheduler
	 * and receive before moving to next instruction line. Program will then exit
	 */
	public void run()
	{
		readInput(); //Convert text file to list of instructions
		
		//for each instruction in list send instruction to scheduler and get response
		for (int i = 0; i < instructions.size(); i++)
		{
			sendInstruction(instructions.get(i));
			getInstruction();
		}
		
		System.exit(0); //Program will exit 
			
	}
	
	/**
	 * Print statement showing floor subsystem is sending an Instruction object
	 * and pass object to scheduler method
	 */
	private void sendInstruction(Instruction newInstruction)
	{
		System.out.println("FloorSubsystem sending instructions, "+newInstruction);
		scheduler.setInstructionsFromFloor(newInstruction);
	}
	
	/**
	 * Get Instruction object from scheduler class and print instruction object
	 * that was received
	 */
	private void getInstruction()
	{
		Instruction newInstruction = scheduler.getInstructionForFloor();
		System.out.println("FloorSubsystem has received Intructions: "+ newInstruction + "\n");
	}
	
	/**
	 * Convert instructions text file to list of instructions
	 */
	private void readInput()
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
}
