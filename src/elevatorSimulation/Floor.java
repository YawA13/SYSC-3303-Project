package elevatorSimulation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Floor 
{
	private int floorNum;
	
	private ButtonStatus [] floorLamps;
	private ButtonStatus [] directionLamps;
	
	private boolean arrivalLamp;
	
	private Queue<Instruction> requests;
	private FloorSubsystem floorSubsystem;

	public Floor(FloorSubsystem floorSubsystem, int floorNum) 
	{
		this.arrivalLamp = false;
		this.requests = new LinkedList<>();
		this.floorSubsystem = floorSubsystem;
		this.floorNum = floorNum;
		this.floorLamps = new ButtonStatus [] {ButtonStatus.Off, ButtonStatus.Off};
		this.directionLamps = new ButtonStatus [] {ButtonStatus.Off, ButtonStatus.Off};
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
		floorSubsystem.sendInstruction(currentRequest);
		floorLamps [getLampLocation(currentRequest.getButtonStatus())] = currentRequest.getButtonStatus();
		
	}
	
	private int getLampLocation (ButtonStatus status)
	{
		if(status == ButtonStatus.Up)
		{
			return 0;
		}
		else
		{
			return 1;
		}
		
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
