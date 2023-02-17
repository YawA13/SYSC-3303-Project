package elevatorSimulation;

import java.util.ArrayList;
import java.util.List;

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
	
	
	/**
	 * Initialize scheduler object
	 */
	public Scheduler()
	{
		pendingInstructions = new ArrayList<Instruction>(); 
		activeInstructions = new ArrayList<Instruction> (); 
		state = SchedulerStates.Waiting;
	}
	
	/**
	 * Scheduler thread that does nothing in this iteration
	 */
	public void run()
	{
		while(true)
		{
			
			switch (state)
			{
				case Waiting:
					break;
				case ProcessRequest:
					break;
				case ProcessElevator:
					break;
			}
			
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
	public synchronized Instruction getInstructionForElevator() 
	{
		//While there are no instructions for elevator wait
		state = SchedulerStates.ProcessRequest;
		
		while (activeInstructions.size() == 0 && pendingInstructions.size() == 0)
		{
			try { 
                wait();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
		}
		
		System.out.println("Scheduler sending instructions to elevator");
		notifyAll();
		
		if (activeInstructions.size() > 0)
		{
			state = SchedulerStates.Waiting;
			return activeInstructions.get(0);
		}
		else
		{
			state = SchedulerStates.Waiting;
			return pendingInstructions.get(0);
		}
		
		
	}

	public synchronized void elevatorFinished(Instruction instruction) 
	{
		//While there are instructions available wait 
		while (activeInstructions.size() == 0)
		{
			try { 
                wait();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
		}
		
		System.out.println("Scheduler received the finished instructions from elevator");
		//activeInstructions.remove(instruction);
		notifyAll(); //Notify synchronized methods
		
	}
	
	public synchronized boolean checkElevatorLocation(int elevatorLocation) 
	{
		boolean stopElevator = false;
		//While there are instructions available wait 
		while (activeInstructions.size() == 0)
		{
			try { 
                wait();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
		}
		
		for (Instruction instruction : activeInstructions)
		{
			if (instruction.getCarButton() == elevatorLocation)
			{
				activeInstructions.remove(instruction);
				stopElevator =  true;
			}
		}
		
		for (Instruction instruction : pendingInstructions)
		{
			if ((instruction.getFloor() == elevatorLocation) 
					&& (instruction.getButtonStatus() == activeInstructions.get(0).getButtonStatus()))
			{
				activeInstructions.add(instruction);
				pendingInstructions.remove(instruction);
				stopElevator = true;
			}
			
		}
		
		notifyAll(); //Notify synchronized methods
		return stopElevator;
			
	}
	
	public int getElevatorFinalDest()
	{
		return activeInstructions.get(0).getCarButton();
	}
	
}
