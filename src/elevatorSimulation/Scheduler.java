package elevatorSimulation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
	

	
	
	private List<ElevatorSubsystem> elevatorSubsystems;
	
	
	/**
	 * Initialize scheduler object
	 */
	public Scheduler()
	{
		this.elevatorSubsystems = new ArrayList<>();
		pendingInstructions = new ArrayList<Instruction>(); 
		state = SchedulerStates.Waiting;
	
	}
	
	public void setElevatorSubsystem(ElevatorSubsystem elevatorSubsystem)
	{
		this.elevatorSubsystems.add(elevatorSubsystem);
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
		
		boolean emptyActive = false;
		for (int i = 0; i < elevatorSubsystems.size(); i++)
		{
			if(elevatorSubsystems.get(i).getActiveInstructions().isEmpty() && !emptyActive)
			{
				elevatorSubsystems.get(i).addToActiveInstructions(instruction);
				emptyActive = true;
			}
		}
		
		if (!emptyActive)
		{
			this.pendingInstructions.add(instruction); //Save instruction object
		}
		
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
		
		List<Instruction> activeInstructions = elevatorSubsystems.get(elevatorCarNum).getActiveInstructions();
		
		while (activeInstructions.size() == 0 && pendingInstructions.size() == 0)
		{
			try { 
				System.out.println("Elevator waiting for Instruction");
                wait();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
		}
		
		state = SchedulerStates.ProcessRequest;
		System.out.println("Scheduler sending instructions to elevator");
		notifyAll();
		
		if (activeInstructions.size() <= 0)
		{
			pendingInstructions.remove(pendingInstructions.get(0));
		}
		
		state = SchedulerStates.Waiting;
		return activeInstructions.get(0);
		
	}

	public void elevatorFinished(Instruction instruction) 
	{
		System.out.println("Scheduler has been notified that finished elevator has finished its instructions");
	}
	
	public synchronized boolean checkElevatorLocation(int elevatorCarNum) 
	{

		List<Instruction> activeInstructions = elevatorSubsystems.get(elevatorCarNum).getActiveInstructions();
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
		
		int elevatorLocation = elevatorSubsystems.get(elevatorCarNum).getCurrentLocation();
		
		
		for (int i = pendingInstructions.size() -1; i >= 0; --i)
		{
			if ((pendingInstructions.get(i).getFloor() == elevatorLocation) 
					&& (pendingInstructions.get(i).getButtonStatus() == activeInstructions.get(0).getButtonStatus()))
			{
				elevatorSubsystems.get(elevatorCarNum).addToActiveInstructions((pendingInstructions.get(i)));
				pendingInstructions.remove(pendingInstructions.get(i));
				stopElevator = true;	
			}
		}
		
		
		
		for (int i = activeInstructions.size()-1; i >= 0 ; --i)
		{
			if (activeInstructions.get(i).getFloor() == elevatorLocation)
			{
				//floorLastArrived = elevatorLocation;
				elevatorSubsystems.get(elevatorCarNum).incrementEelevator();
				stopElevator =  true;
			}
			
			else if (activeInstructions.get(i).getCarButton() == elevatorLocation &&
					elevatorSubsystems.get(elevatorCarNum).getElevatorDirection() == activeInstructions.get(i).getButtonStatus())
			{
				elevatorSubsystems.get(elevatorCarNum).removeActiveInstruction(activeInstructions.get(i));
				elevatorSubsystems.get(elevatorCarNum).decrementEelevator();
				stopElevator =  true;
			}
			
		}
		
		notifyAll(); //Notify synchronized methods
		return stopElevator;
			
	}
	
	
}
