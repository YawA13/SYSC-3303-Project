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
		this.scheduler.setElevatorSubsystem(this);
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
					System.out.println("ElevatorSubsystem state: DoorsOpen, at floor"+ elevator.currentFloor);
					if (instruction == null)
					{
						getInstructions();
					}					
					state = ElevatorStates.DoorsClosing;
					break;
				case DoorsClosing:
					System.out.println("ElevatorSubsystem state: DoorsClosing, at floor"+ elevator.currentFloor + ", Num Of Passenegers:"+elevator.getNumOfPassengers());
					//TODO setLightsAndStatus()
					setDirection();	
					break;
				case MovingUp:
					//timer
					elevator.moveUp();
					System.out.println("ElevatorSubsystem state: MovingUp, at floor"+ elevator.currentFloor);
					stateBeforeArriving = ElevatorStates.MovingUp;
					state = ElevatorStates.Arriving;
					break;
				case MovingDown:
					//timer 
					elevator.moveDown();
					System.out.println("ElevatorSubsystem state: MovingDown, at floor"+ elevator.currentFloor);
					stateBeforeArriving = ElevatorStates.MovingDown;
					state = ElevatorStates.Arriving;
					break;
				case Arriving:
					System.out.println("ElevatorSubsystem state: Arriving, at floor"+ elevator.currentFloor);
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
					System.out.println("ElevatorSubsystem state: DoorsOpening, at floor" + elevator.currentFloor);
					if (elevator.currentFloor == scheduler.getElevatorFinalDest())
					{
						System.out.println("ElevatorSubsystem: Elevator Done");
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
			elevator.moving(ButtonStatus.Up, finalFloorNum);
		}
		else if (finalFloorNum - elevator.getCurrentFloor() < 0)
		{
			state = ElevatorStates.MovingDown;
			elevator.moving(ButtonStatus.Down, finalFloorNum);
		}
		else
		{
			scheduler.checkElevatorLocation(elevator.getCurrentFloor());
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
		System.out.println(" ");
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
	
	public void incrementEelevator()
	{
		elevator.setNumOfPassengers(elevator.getNumOfPassengers()+1);
	}
	
	public void decrementEelevator()
	{
		elevator.setNumOfPassengers(elevator.getNumOfPassengers()-1);
	}
	
	public ButtonStatus getElevatorButtonStatus()
	{
		return elevator.getbStatus();
	}

}
