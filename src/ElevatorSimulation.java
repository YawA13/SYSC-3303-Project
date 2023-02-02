
public class ElevatorSimulation {
	
	public static void main (String [] args)
	{
		
		
		Scheduler scheduler = new Scheduler ();
		Thread elevator = new Elevator (scheduler);
		Thread floor = new Floor (scheduler, "C:\\Users\\aasay\\Desktop\\University Work\\Winter 3rd Year\\SYSC 3303\\Projects");
		
		elevator.start();
		floor.start();
		
	}

}
