package elevatorSimulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

public class Floor 
{
	private int floorNum;
	private int numOfElevators;
	
	private ButtonStatus [] floorLamps;
	private Map<Integer, ButtonStatus []> directionLamps;
	
	private boolean arrivalLamp;
	
	private Queue<Instruction> requests;
	private FloorSubsystem floorSubsystem;

	public Floor(FloorSubsystem floorSubsystem, int floorNum, int numOfElevators) 
	{
		this.arrivalLamp = false;
		this.requests = new LinkedList<>();
		this.floorSubsystem = floorSubsystem;
		this.floorNum = floorNum;
		this.numOfElevators = numOfElevators;
		this.floorLamps = new ButtonStatus [] {ButtonStatus.Off, ButtonStatus.Off};
		this.directionLamps = new HashMap<Integer, ButtonStatus []>();
		
		for (int i = 0; i < numOfElevators; i++)
		{
			directionLamps.put(i, new ButtonStatus [] {ButtonStatus.Off, ButtonStatus.Off});
		}
		 
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
		System.out.println("Send Request : "+getStatus());
	}
	
	public void setLampWhenElevatorReachFloor(int carNumber, ButtonStatus currentDirection, boolean isElevatorStopping)
	{
		if (isElevatorStopping)
		{
			//System.out.println("Stop at Floor");
			floorLamps [getLampLocation(currentDirection)] = ButtonStatus.Off;
		}
		directionLamps.get(carNumber)[getLampLocation(currentDirection)] = currentDirection;
		//System.out.println("Reach Floor : "+getStatus());
	}
	
	
	public void setLampWhenElevatorLeaves(int carNumber)
	{
		for (int i = 0; i < directionLamps.get(carNumber).length; i++)
		{
			directionLamps.get(carNumber)[i] = ButtonStatus.Off;
		}
		//System.out.println("Elevator Leaves :"+getStatus());
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
	
	public String getStatus()
	{
	
		String separator = "";
		String status = "{Floor Num: "+floorNum 
				+", Floor Lamps: " +printButtonStatus(floorLamps)
				+", Direction Lamps: {";
		
		for (Entry<Integer, ButtonStatus[]> lamp: directionLamps.entrySet())
		{
			status = status + separator +lamp.getKey() + "=" + printButtonStatus(lamp.getValue());
			separator = ", ";
		}
				
		status = status +"}";
		
		return status;
	}
	
	private String printButtonStatus(ButtonStatus[] buttStatus)
	{
		String separator = "";
		String status = "[";
		
		for (int i = 0; i < buttStatus.length; i++)
		{
			status = status + separator + buttStatus[i].toString();
			separator = ", ";
		}
		
		status = status + "]";
		return status;
	}
	
}
