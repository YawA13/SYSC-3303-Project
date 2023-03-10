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
	private List <Instruction> activeInstructions;
	private SchedulerStates state;
	
	//used for floor last arrived, since we remove the active before we check if finnal dest.Note maybe use line 152 instead
	private int elevatorFinalDest;
	
	
	private ElevatorSubsystem elevatorSubsystem;
	
	
	/**
	 * Initialize scheduler object
	 */
	public Scheduler()
	{
		pendingInstructions = new ArrayList<Instruction>(); 
		activeInstructions = new ArrayList<Instruction> (); 
		state = SchedulerStates.Waiting;
		elevatorFinalDest = 1;
	}
	
	public void setElevatorSubsystem(ElevatorSubsystem elevatorSubsystem)
	{
		this.elevatorSubsystem = elevatorSubsystem;
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
		if(activeInstructions.isEmpty())
		{
			activeInstructions.add(instruction);
		}
		else
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
	public synchronized Instruction getInstructionForElevator() 
	{
		//While there are no instructions for elevator wait
		//state = SchedulerStates.ProcessRequest;
		
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
			activeInstructions.add(pendingInstructions.get(0));
			pendingInstructions.remove(pendingInstructions.get(0));
		}
		

		state = SchedulerStates.Waiting;
		elevatorFinalDest = activeInstructions.get(0).getCarButton();
		return activeInstructions.get(0);

		
		
	}

	public void elevatorFinished(Instruction instruction) 
	{
		System.out.println("Scheduler has been notified that finished elevator has finished its instructions");
	}
	
	public synchronized boolean checkElevatorLocation(int elevatorLocation) 
	{

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
		

		Set<Integer> activeInstructCarButtons = new HashSet<Integer>();
		for (int i = pendingInstructions.size() -1; i >= 0; --i)
		{
			if ((pendingInstructions.get(i).getFloor() == elevatorLocation) 
					&& (pendingInstructions.get(i).getButtonStatus() == activeInstructions.get(0).getButtonStatus()))
			{
				activeInstructions.add(pendingInstructions.get(i));
				activeInstructCarButtons.add(pendingInstructions.get(i).getCarButton());
				pendingInstructions.remove(pendingInstructions.get(i));
				stopElevator = true;	
			}
		}
		
		if(activeInstructCarButtons.size() > 0) {
			elevatorSubsystem.turnButtonLampOn(activeInstructCarButtons);
		}
		
		
		for (int i = activeInstructions.size()-1; i >= 0 ; --i)
		{
			if (activeInstructions.get(i).getFloor() == elevatorLocation)
			{
				//floorLastArrived = elevatorLocation;
				elevatorSubsystem.incrementEelevator();
				stopElevator =  true;
			}
			
			else if (activeInstructions.get(i).getCarButton() == elevatorLocation &&
					elevatorSubsystem.getElevatorDirection() == activeInstructions.get(i).getButtonStatus())
			{
				activeInstructions.remove(activeInstructions.get(i));
				elevatorSubsystem.decrementEelevator();
				stopElevator =  true;
			}
			
		}
		
		notifyAll(); //Notify synchronized methods
		return stopElevator;
			
	}
	
	public int getElevatorFinalDest()
	{
		return elevatorFinalDest;
	}
	
}
