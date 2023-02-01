
public class Instruction {
	
	private String time;
	private int floor;
	private int elevatorNum;
	
	private ButtonStatus buttonStatus;
	public enum ButtonStatus {Up,Down};
	
	public Instruction(String time, int floor, int elevatorNum, String buttonStatus)
	{
		this.time = time;
		this.floor = floor;
		this.elevatorNum = elevatorNum;
		
		if (buttonStatus.equalsIgnoreCase("Up"))
		{
			this.buttonStatus = ButtonStatus.Up;
			
		}
		else
		{
			this.buttonStatus = ButtonStatus.Down;
		}
	}

}
