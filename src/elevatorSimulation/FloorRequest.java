package elevatorSimulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

//maybe change to floor button class
public class FloorRequest extends Thread
{
	private Floor floor;
	Queue<Instruction> requests;
	private long timePassed;
   

	public FloorRequest(Floor floor, Queue<Instruction> requests) 
	{
		this.floor = floor;
		this.requests = requests;
		this.timePassed = 0;
	}
	
	
	public void run()
	{
		while (!requests.isEmpty())
		{
			Instruction currentRequest = requests.remove();
			long timeToNextRequest = currentRequest.getTimeAfterInital() - timePassed;

			try {
				Thread.sleep(timeToNextRequest);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			timePassed = timePassed + timeToNextRequest;
			floor.sendLatestRequestToSubsystem(currentRequest);
		}
	}

	

}
