package elevatorSimulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Code to handle the elevator subsystem's tasks for Milestone 1 SYSC 3303.
 * ElevatorSubsystem class will receive and send instructions to floor class
 * through synchronized methods in the scheduler class
 * 
 * @author Group 3, SYSC 3303
 * @version Milestone 2
 * @since 2023-02-16
 */
public class ElevatorSubsystem extends Thread
{
	private Scheduler scheduler;
	private Instruction instruction;
	private ElevatorStates state;
	private Elevator elevator;
	private int carNumber;
	
	/**
	 * Initialize Elevator subsystem
	 * 
	 * @param scheduler Object to synchronize information between elevator and floor class
	 */
	public ElevatorSubsystem (Scheduler scheduler, int floors, int carNumber)
	{
		this.scheduler = scheduler;
		this.state = ElevatorStates.DoorsOpen;
		this.elevator = new Elevator(floors, this);
		this.carNumber = carNumber;
	}
	
	/**
	 * Start Elevator subsystem thread to repeatedly get and send instructions
	 * to scheduler
	 */
	public void run()
	{
		ElevatorStates stateBeforeArriving = ElevatorStates.DoorsOpen;
		
		while (true)
		{
			switch (state)
			{
				case DoorsOpen:
					System.out.println("ElevatorSubsystem " +carNumber +" state: DoorsOpen, at floor"+ elevator.getCurrentFloor()+"\n\tStatus " +carNumber +": "+elevator.getStatus());
					if (instruction == null)
					{
						getInstructions();
						elevator.setElevatorFinalDest(instruction.getCarButton());
					}	
					elevator.setDoorOpen(false);
					state = ElevatorStates.DoorsClosing;
					break;
				case DoorsClosing:
					System.out.println("ElevatorSubsystem "+carNumber +" state: DoorsClosing, at floor:"+ elevator.getCurrentFloor());
					//System.out.println("\tStatus " +carNumber +": "+elevator.getStatus());
					setDirection();	
					break;
				case MovingUp:
					//timer
					elevator.moveUp();
					System.out.println("ElevatorSubsystem "+carNumber +" state: MovingUp, at floor"+ elevator.getCurrentFloor());
					//System.out.println("\tStatus " +carNumber +": "+elevator.getStatus());
					stateBeforeArriving = ElevatorStates.MovingUp;
					state = ElevatorStates.Arriving;
					break;
				case MovingDown:
					//timer 
					elevator.moveDown();
					System.out.println("ElevatorSubsystem "+carNumber +" state: MovingDown, at floor"+ elevator.getCurrentFloor());
					//System.out.println("\tStatus " +carNumber +": "+elevator.getStatus());
					stateBeforeArriving = ElevatorStates.MovingDown;
					state = ElevatorStates.Arriving;
					break;
				case Arriving:
					System.out.println("ElevatorSubsystem " +carNumber +" state: Arriving, at floor"+ elevator.getCurrentFloor());
					//System.out.println("\tStatus " +carNumber +": "+elevator.getStatus());
					int [] msg = scheduler.checkElevatorLocation(carNumber, elevator.getCurrentFloor(), elevator.getDirection());
					
					elevator.setNumOfPassengers(elevator.getNumOfPassengers()+msg[1]);
					elevator.setNumOfPassengers(elevator.getNumOfPassengers()-msg[2]);
					
					if(msg [0] == 1)
					{
						state = ElevatorStates.DoorsOpening;			
					}
					else
					{
						state = stateBeforeArriving;
					}
					break;
				case DoorsOpening:
					System.out.println("ElevatorSubsystem "+carNumber +" state: DoorsOpening, at floor" + elevator.getCurrentFloor());
					//System.out.println("\tStatus " +carNumber +": "+elevator.getStatus());
					if (elevator.getCurrentFloor() == elevator.getElevatorFinalDest())
					{
						System.out.println("ElevatorSubsystem: Elevator Done");
						scheduler.elevatorFinished(instruction);
						instruction = null;
						elevator.resetElevator();
					}
					
					elevator.setDoorOpen(true);
					state = ElevatorStates.DoorsOpen;
					break;
			}
			
		}
		
	}
	
	private void setDirection()
	{
		int finalFloorNum;
		
		if (elevator.getNumOfPassengers() == 0)
		{
			finalFloorNum = instruction.getFloor();
		}
		else
		{
			finalFloorNum = instruction.getCarButton();
		}

		
		if (finalFloorNum - elevator.getCurrentFloor() > 0)
		{
			state = ElevatorStates.MovingUp;
			elevator.moving(ButtonStatus.Up);
		}
		else if (finalFloorNum - elevator.getCurrentFloor() < 0)
		{
			state = ElevatorStates.MovingDown;
			elevator.moving(ButtonStatus.Down);
		}
		else
		{
			int [] msg = scheduler.checkElevatorLocation(carNumber, elevator.getCurrentFloor(), elevator.getDirection());
			
			elevator.setNumOfPassengers(elevator.getNumOfPassengers()+msg[1]);
			elevator.setNumOfPassengers(elevator.getNumOfPassengers()-msg[2]);
			
			state = ElevatorStates.DoorsOpening;
		}
		
	}
	
	/**
	 * Get Instruction object from scheduler class and print instruction object
	 * that was received
	 */
	private void getInstructions()
	{
		instruction = scheduler.getInstructionForElevator(carNumber);
		addToActiveInstructions(instruction);	
	}
	

	
	/**
	 * public function used in test script to get instruction 
	 */
	public Instruction getCurrentInstruction() {
		return instruction; 
	}

	/**
	 * public function used in test script to call test send getInstruction() function
	 */
	public void testGetInstruciton() {
		getInstructions(); 
	}
	
	public void incrementEelevator()
	{
		elevator.setNumOfPassengers(elevator.getNumOfPassengers()+1);
	}
	
	public void decrementEelevator()
	{
		elevator.setNumOfPassengers(elevator.getNumOfPassengers()-1);
	}
	
	public ButtonStatus getElevatorDirection()
	{
		return elevator.getDirection();
	}
	
	public void turnButtonLampOn(Set<Integer> buttonNumbers)
	{
		for (int buttonNumber: buttonNumbers)
		{
			elevator.turnButtonLamp(buttonNumber, true);
		}
	}

	
	public void addToActiveInstructions(Instruction newInstruction)
	{
		//activeInstructions.add(newInstruction);
		elevator.turnButtonLamp(newInstruction.getCarButton(), true);
		System.out.println("ElevatorSubsystem "+carNumber +" added instruction " +newInstruction+"\n");
	}
	
	public int getCurrentLocation()
	{
		return elevator.getCurrentFloor();
	}

	public void removeActiveInstruction(Instruction newInstruction) {
		// TODO Auto-generated method stub
		//activeInstructions.remove(newInstruction);
		elevator.turnButtonLamp(newInstruction.getCarButton(), false);
		System.out.println("ElevatorSubsystem "+carNumber +" finished instruction " +newInstruction +"\n");
		
	}
}
