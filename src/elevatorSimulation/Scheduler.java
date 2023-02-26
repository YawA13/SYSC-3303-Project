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
	
	//used for floor last arrived, since we remove the active before we check if finnal dest.Note maybe use line 152 instead
	private int floorLastArrived;
	
	
	
	/**
	 * Initialize scheduler object
	 */
	public Scheduler()
	{
		pendingInstructions = new ArrayList<Instruction>(); 
		activeInstructions = new ArrayList<Instruction> (); 
		state = SchedulerStates.Waiting;
		floorLastArrived = 1;
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
				System.out.println("wait in getInstructionForElevator");
                wait();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
		}
		
		state = SchedulerStates.ProcessRequest;
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
				System.out.println("wait in elevatorFinished");
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
		System.out.println("Check elevaor method running");
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
		
		for (int i = 0; i < activeInstructions.size(); i++)
		{
			if (activeInstructions.get(i).getCarButton() == elevatorLocation)
			{
				activeInstructions.remove(activeInstructions.get(i));
				floorLastArrived = elevatorLocation;
				stopElevator =  true;
			}
		}
		
		for (int i = 0; i < pendingInstructions.size(); i++)
		{
			if ((pendingInstructions.get(i).getFloor() == elevatorLocation) 
					&& (pendingInstructions.get(i).getButtonStatus() == activeInstructions.get(0).getButtonStatus()))
			{
				activeInstructions.add(pendingInstructions.get(i));
				pendingInstructions.remove(pendingInstructions.get(i));
				stopElevator = true;
			}
		}
		
		
		notifyAll(); //Notify synchronized methods
		return stopElevator;
			
	}
	
	public int getElevatorFinalDest()
	{
		if (activeInstructions.isEmpty())
		{
			return floorLastArrived;
		}
		return activeInstructions.get(0).getCarButton();
	}
	
}
