package elevatorSimulation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Floor 
{
	private int floorNum;
	private boolean upFloorLamp;
	private boolean downFloorLamp;
	
	private boolean upDirectionLamp;
	private boolean downDirectionLamp;
	
	private boolean upButton;
	private boolean downButton;
	
	private boolean arrivalLamp;
	
	private Queue<Instruction> requests;
	private FloorSubsystem floorSubsystem;

	public Floor(FloorSubsystem floorSubsystem, int floorNum) 
	{
		this.upFloorLamp = false;
		this.downFloorLamp = false;
		this.upDirectionLamp = false;
		this.downDirectionLamp = false;
		this.upButton = false;
		this.downButton = false;
		this.arrivalLamp = false;
		this.requests = new LinkedList<>();
		this.floorSubsystem = floorSubsystem;
		this.floorNum = floorNum;
	}

	public boolean getUpFloorLamp() {
		return upFloorLamp;
	}

	public void setUpFloorLamp(boolean upFloorLamp) {
		this.upFloorLamp = upFloorLamp;
	}

	public boolean getDownFloorLamp() {
		return downFloorLamp;
	}

	public void setDownFloorLamp(boolean downFloorLamp) {
		this.downFloorLamp = downFloorLamp;
	}

	public boolean getUpDirectionLamp() {
		return upDirectionLamp;
	}

	public void setUpDirectionLamp(boolean upDirectionLamp) {
		this.upDirectionLamp = upDirectionLamp;
	}

	public boolean getDownDirectionLamp() {
		return downDirectionLamp;
	}

	public void setDownDirectionLamp(boolean downDirectionLamp) {
		this.downDirectionLamp = downDirectionLamp;
	}

	public boolean getUpButton() {
		return upButton;
	}

	public void setUpButton(boolean upButton) {
		this.upButton = upButton;
	}

	public boolean getDownButton() {
		return downButton;
	}

	public void setDownButton(boolean downButton) {
		this.downButton = downButton;
	}

	public boolean getArrivalLamp() {
		return arrivalLamp;
	}

	public void setArrivalLamp(boolean arrivalLamp) {
		this.arrivalLamp = arrivalLamp;
	}
	
	public void addRequest(Instruction instruction)
	{
		requests.add(instruction);
	}
	
	public void sendLatestRequestToSubsystem(Instruction currentRequest)
	{
		System.out.println(this.floorNum+" Floor: sendInstructions");
		floorSubsystem.sendInstruction(currentRequest);
	}
	
	//Change to be in run if this class becomes a thread
	public void startFloorRequest()
	{
		Thread floorRequest = new FloorRequest (this, requests);
		floorRequest.start();
	}

	public int getFloorNum()
	{
		return floorNum;
	}
	
}
