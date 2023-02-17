package elevatorSimulation;


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
	
	/**
	 * Initialize Elevator subsystem
	 * 
	 * @param scheduler Object to synchronize information between elevator and floor class
	 */
	public ElevatorSubsystem (Scheduler scheduler, int floors)
	{
		this.scheduler = scheduler;
		this.state = ElevatorStates.DoorsOpen;
		this.elevator = new Elevator(floors, this);
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
					if (instruction == null)
					{
						getInstructions();
					}
					state = ElevatorStates.DoorsClosing;
					break;
				case DoorsClosing:
					//TODO setLightsAndStatus()
					setDirection();	
					break;
				case MovingUp:
					//timer
					stateBeforeArriving = ElevatorStates.MovingUp;
					state = ElevatorStates.Arriving;
					break;
				case MovingDown:
					//timer 
					stateBeforeArriving = ElevatorStates.MovingDown;
					state = ElevatorStates.Arriving;
					break;
				case Arriving:
					
					boolean atDestination = scheduler.checkElevatorLocation(elevator.getCurrentFloor());
					if(atDestination)
					{
						state = ElevatorStates.DoorsOpening;
					}
					else
					{
						state = stateBeforeArriving;
					}
					break;
				case DoorsOpening:
					
					if (elevator.currentFloor == scheduler.getElevatorFinalDest())
					{
						scheduler.elevatorFinished(instruction);
						instruction = null;
						elevator.resetElevator();
					}
				
					state = ElevatorStates.DoorsOpen;
					break;
			}
			
		}
		
	}
	
	private void setDirection()
	{
		int finalFloorNum;
		
		if (elevator.getNumOfPassengers() > 0)
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
			elevator.moving(ButtonStatus.Up, finalFloorNum);
		}
		else if (finalFloorNum - elevator.getCurrentFloor() < 0)
		{
			state = ElevatorStates.MovingDown;
			elevator.moving(ButtonStatus.Down, finalFloorNum);
		}
		else
		{
			state = ElevatorStates.DoorsOpening;	
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
	


}
