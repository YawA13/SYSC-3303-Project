package elevatorSimulation;

import java.util.ArrayList;

public class Elevator {
	private ButtonStatus direction; 
	private ArrayList<Boolean> elevatorButtonLamps;
	private Boolean motorOn; // true = on, false = off
	private Boolean doorOpen; // true = open, false = closed 
	
	ElevatorSubsystem subsystem; 
	
	private int currentFloor; 
	private int numOfPassengers;
	
	public Elevator(int floors, ElevatorSubsystem subsystem) {
		elevatorButtonLamps = new ArrayList<>();
		currentFloor = 1; 
		direction = ButtonStatus.Off;
		
		for(int i = 0; i < floors; i++) {
			elevatorButtonLamps.add(false); 
		}
		motorOn = false; 
		doorOpen = true; 
		this.subsystem = subsystem;
		numOfPassengers = 0;
	}

	public void moving(ButtonStatus bStatus)
	{
		motorOn = true;
		doorOpen = false;
		this.direction = bStatus;
	}
	
	public void resetElevator()
	{
		motorOn = false; 
		//doorOpen = true; 
		this.direction = ButtonStatus.Off;
		//turnButtonLamp(currentFloor, false);
	}
	
	public ButtonStatus getDirection() {
		return direction;
	}

	public void setDirection(ButtonStatus bStatus) {
		this.direction = bStatus;
	}

	public ArrayList<Boolean> getElevatorButtonLamps() {
		return elevatorButtonLamps;
	}

	public void setElevatorButtonLamps(ArrayList<Boolean> elevatorButtonLamps) {
		this.elevatorButtonLamps = elevatorButtonLamps;
	}
	
	public void turnButtonLamp(int buttonNum, boolean lampStatus )
	{
		elevatorButtonLamps.set(buttonNum -1, lampStatus);
	}

	public Boolean getMotorOn() {
		return motorOn;
	}

	public void setMotorOn(Boolean motorOn) {
		this.motorOn = motorOn;
	}

	public Boolean getDoorOpen() {
		return doorOpen;
	}

	public void setDoorOpen(Boolean doorOpen) {
		this.doorOpen = doorOpen;
	}

	public int getCurrentFloor() {
		return currentFloor;
	}

	public void setCurrentFloor(int currentFloor) {
		this.currentFloor = currentFloor;
	}

	public int getNumOfPassengers() {
		return numOfPassengers;
	}

	public void setNumOfPassengers(int numOfPassengers) {
		this.numOfPassengers = numOfPassengers;
	}
		
	public void moveUp()
	{
		currentFloor++;
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void moveDown()
	{
		currentFloor--;
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getStatus()
	{
		String status = "{Direction: "+direction 
							+", numOfPassengers: " +numOfPassengers
							+", buttonLamps: " +elevatorButtonLamps
							+", motorOn: "+motorOn 
							+", doorOpen: "+doorOpen 	
							+"}";
		return status;
	}
}
