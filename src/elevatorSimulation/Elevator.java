package elevatorSimulation;

import java.util.ArrayList;

public class Elevator {
	ButtonStatus bStatus; 
	ArrayList<Boolean> elevatorButtonLamps;
	Boolean motorOn; // true = on, false = off
	Boolean doorOpen; // true = open, false = closed 
	
	ElevatorSubsystem subsystem; 
	
	int currentFloor; 
	private int numOfPassengers;
	
	public Elevator(int floors, ElevatorSubsystem subsystem) {
		elevatorButtonLamps = new ArrayList<>();
		currentFloor = 1; 
		bStatus = ButtonStatus.Off;
		
		for(int i = 0; i < floors; i++) {
			elevatorButtonLamps.add(false); 
		}
		motorOn = false; 
		doorOpen = true; 
		this.subsystem = subsystem;
		numOfPassengers = 0;
	}

	public void moving(ButtonStatus bStatus, int destination)
	{
		motorOn = true;
		doorOpen = false;
		this.bStatus = bStatus;
		elevatorButtonLamps.set(destination-1, true);
	}
	
	public void resetElevator()
	{
		motorOn = false; 
		doorOpen = true; 
		this.bStatus = ButtonStatus.Off;
		elevatorButtonLamps.set(currentFloor-1, false);
	}
	
	public ButtonStatus getbStatus() {
		return bStatus;
	}

	public void setbStatus(ButtonStatus bStatus) {
		this.bStatus = bStatus;
	}

	public ArrayList<Boolean> getElevatorButtonLamps() {
		return elevatorButtonLamps;
	}

	public void setElevatorButtonLamps(ArrayList<Boolean> elevatorButtonLamps) {
		this.elevatorButtonLamps = elevatorButtonLamps;
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
}