package elevatorSimulation;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Code to create Instruction object for Milestone 1 SYSC 3303.
 * Instruction object is used to contain instruction from a text file
 * 
 * @author Group 3, SYSC 3303
 * @version Milestone 1
 * @since 2023-02-04
 */
public class Instruction {
	
	private final String TIME_FORMAT = "hh:mm:ss.mmm";
	
	private LocalTime time; //Time button was pressed
	private int floor; //Floor elevator is on
	private int carButton; //Elevator number
	private ButtonStatus buttonStatus; //Direction elevator will go
	private long timeAfterInital;
	
	/**
	 * Initialize Instruction object by passing in time, floor, elevator number,
	 * and button status individually
	 * 
	 * @param time Time button was pressed
	 * @param floor Floor elevator is on
	 * @param carButton Elevator number
	 * @param buttonStatus Direction elevator will go
	 */
	public Instruction(String time, int floor, int carButton, String buttonStatus)
	{
	
		this.time = LocalTime.parse(time);
		
		this.floor = floor;
		this.carButton = carButton;
		
		if (buttonStatus.equalsIgnoreCase("Up")) //If buttonStatus is up
		{
			this.buttonStatus = ButtonStatus.Up; //Set to enum Up
		}
		else //Else ButtonStatus is down
		{
			this.buttonStatus = ButtonStatus.Down; //Set to enum down
		}
		
		this.timeAfterInital = 0;
	}
	
	/**
	 * Initialize Instruction object by passing in text of instruction line to
	 * be converted
	 * 
	 * @param inputData //Text line to be converted to instruction object
	 */
	public Instruction (String inputData)
	{
		String [] split = inputData.split("\\t"); //Split text based on TAB delimiter into array
		
		//Set time as first object in array
		String timeText = split[0]; 
		this.time = LocalTime.parse(timeText);
		this.floor = Integer.parseInt(split[1]); //Set floor as second object in array
		
		//Set Button status as third object in array
		if (split[2].equalsIgnoreCase("Up")) //If buttonStatus is up
		{
			this.buttonStatus = ButtonStatus.Up; //Set to enum Up
		}
		else // Else ButtonStatus is down
		{
			this.buttonStatus = ButtonStatus.Down; //Set to enum Down
		}
		
		this.carButton = Integer.parseInt(split[3]); //Set elevator number as 4th object in array
		this.timeAfterInital = 0;
		
	}
	
	/**
	 * Convert Instruction object to string object and return it
	 */
	@Override
	public String toString()
	{
		String message = "Time:"+time+", Floor:"+floor+", Elevator Number:"+carButton
				+", Button:"+buttonStatus.toString();
		return message;
		
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	public int getFloor() {
		return floor;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}

	public int getCarButton() {
		return carButton;
	}

	public void setElevatorNum(int elevatorNum) {
		this.carButton = elevatorNum;
	}

	public ButtonStatus getButtonStatus() {
		return buttonStatus;
	}

	public void setButtonStatus(ButtonStatus buttonStatus) {
		this.buttonStatus = buttonStatus;
	}

	public void setTimeAfterInital(long diffInMilli)
	{
		this.timeAfterInital = diffInMilli;
	}
	
	
	public long getTimeAfterInital()
	{
		return timeAfterInital;
	}
}
