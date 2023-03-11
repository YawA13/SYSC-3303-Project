package elevatorSimulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	
	/**
	list for instructions from ppl outside the elevator, 
	will be checked when elevator reaches a floor if going same direction **/
	private List <Instruction> pendingInstructions;
	
	/**
	 * list for instruction from ppl inside the elevator, 
	 * will be checked if ppl need to get off
	 */
	//private List <Instruction> activeInstructions;
	private SchedulerStates state;
	
	private FloorSubsystem floorSubsystem;
	private Map<Integer, ArrayList<Instruction>> allActiveInstructions;
	
	
	/**
	 * Initialize scheduler object
	 */
	public Scheduler(int numOfElevators)
	{
		allActiveInstructions = new HashMap<>();
		for (int i = 0; i < numOfElevators; i++)
		{
			allActiveInstructions.put(i, new ArrayList<>());
		}
		
		pendingInstructions = new ArrayList<Instruction>(); 
		state = SchedulerStates.Waiting;
	
	}
	
	
	public void setFloorSubsystem(FloorSubsystem floorSubsystem)
	{
		this.floorSubsystem = floorSubsystem;
	}
	
	/**
	 * Scheduler thread that does nothing in this iteration
	 */
	public void run()
	{
		while(true)
		{
			/**
			switch (state)
			{
				case Waiting:
					break;
				case ProcessRequest:
					break;
				case ProcessElevator:
					break;
			}
			**/
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
		while (!(state == SchedulerStates.Waiting))
		{
			try { 
				System.out.println("wait in setInstructionsFromFloor, state:"+state);
                wait();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
		}
		
		System.out.println("Scheduler received instructions from floor");
		this.pendingInstructions.add(instruction); //Save instruction object
		notifyAll(); //Notify synchronized methods
	}

	/**
	 * Set instruction method to be called from elevator class to retrieve 
	 * instruction object
	 * 
	 * @return Instruction object
	 */
	public synchronized Instruction getInstructionForElevator(int elevatorCarNum) 
	{
		//While there are no instructions for elevator wait
		//state = SchedulerStates.ProcessRequest;
		
		List<Instruction> activeInstructions = allActiveInstructions.get(elevatorCarNum);
		
		while (activeInstructions.size() == 0 && pendingInstructions.size() == 0)
		{
			try { 
				System.out.println("Elevator "+elevatorCarNum +" waiting for Instruction");
                wait();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
		}
		
		state = SchedulerStates.ProcessRequest;
		System.out.println("Scheduler sending instructions to elevator");

		Instruction newInstruction;
		if(activeInstructions.isEmpty())
		{
			newInstruction = pendingInstructions.remove(0);
			activeInstructions.add(newInstruction);
		}
		else
		{
			newInstruction = activeInstructions.get(0);
		}
		
		state = SchedulerStates.Waiting;
		notifyAll();
		return newInstruction;
		
	}

	public void elevatorFinished(Instruction instruction) 
	{
		System.out.println("Scheduler has been notified that finished elevator has finished its instructions");
	}
	
	public synchronized int [] checkElevatorLocation(int elevatorCarNum, int elevatorLocation, ButtonStatus elevatorDirection) 
	{

		List<Instruction> activeInstructions = allActiveInstructions.get(elevatorCarNum);
		boolean stopElevator = false;
		//While there are instructions available wait 
		while (activeInstructions.size() == 0)
		{
			try { 
				System.out.println("wait in checkElevatorLocation");
                wait();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
		}
		
		
		int stop = 0;
		int increment = 0;
		int decrement = 0;
		//list of floor lamps false
		//list of floor lamps true
		
		for (int i = pendingInstructions.size() -1; i >= 0; --i)
		{
			if ((pendingInstructions.get(i).getFloor() == elevatorLocation) 
					&& (pendingInstructions.get(i).getButtonStatus() == activeInstructions.get(0).getButtonStatus()))
			{
				//add to floor lamps for true
				activeInstructions.add((pendingInstructions.get(i)));
				pendingInstructions.remove(pendingInstructions.get(i));
				stop = 1;	
			}
		}
		
		ButtonStatus directionFloor = elevatorDirection;
		for (int i = activeInstructions.size()-1; i >= 0 ; --i)
		{
			if (activeInstructions.get(i).getFloor() == elevatorLocation)
			{
				//floorLastArrived = elevatorLocation;
				increment++;
				directionFloor = activeInstructions.get(i).getButtonStatus();
				stop = 1;
			}
			
			else if (activeInstructions.get(i).getCarButton() == elevatorLocation &&
					elevatorDirection == activeInstructions.get(i).getButtonStatus())
			{
				//add to floor lamps false
				activeInstructions.remove(activeInstructions.get(i));
				decrement++;
				stop =  1;
			}
			
		}
		
		floorSubsystem.elevatorReachFloor(elevatorCarNum, elevatorLocation, directionFloor, stopElevator);
		

		int [] returnInt = {stop, increment, decrement}; //stop ( ==1), increment, decrement;  
		
		notifyAll(); //Notify synchronized methods
		return returnInt;
			
	}
	
	
}
