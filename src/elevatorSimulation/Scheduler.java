package elevatorSimulation;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Code to create Scheduler thread object for Milestone 1 SYSC 3303.
 * Scheduler object will have synchronized methods in order for elevator class
 * and Floor class can pass instructions back and forth
 * 
 * @author Group 3, SYSC 3303
 * @version Milestone 1
 * @since 2023-02-04
 */
public class Scheduler extends Thread
{
	
	/**
	list for instructions from ppl outside the elevator, 
	will be checked when elevator reaches a floor if going same direction **/
	private List <Instruction> pendingInstructions;
	
	/**
	 * list for instruction from ppl inside the elevator, 
	 * will be checked if ppl need to get off
	 */
	//private List <Instruction> activeInstructions;
	private SchedulerStates state;
	
	private String floorIp;
	private String elevatorIp;
	private Map<Integer, ArrayList<Instruction>> allActiveInstructions;
	/**
	 * packets to receive and send data
	 */
   private DatagramPacket sendPacket, receivePacket;
   
   /**
    * sockets to send and receive data
    */
   private DatagramSocket sendReceiveSocket;
   
   /**
    * Port that data will be sent to
    */
   private static final int SENDER_PORT_ELEVATOR = 30;
   private static final int RECEIVE_PORT_ELEVATOR = 23;
   
   private static final int SENDER_PORT_FLOOR1 = 50;
   private static final int RECEIVE_PORT_FLOOR1 = 43;
   
   private static final int SENDER_PORT = 40;
   private static final int RECEIVE_PORT = 33;
   
   private Thread [] schedulerToClient;
	
	/**
	 * Initialize scheduler object
	 */
	public Scheduler(String floorIp, String elevatorIp, int numOfElevators)
	{
		this.floorIp = floorIp;
		this.elevatorIp = elevatorIp;
		allActiveInstructions = new HashMap<>();
		for (int i = 0; i < numOfElevators; i++)
		{
			allActiveInstructions.put(i, new ArrayList<>());
		}
		
		pendingInstructions = new ArrayList<Instruction>(); 
		state = SchedulerStates.Waiting;
	
		schedulerToClient =  new Thread [numOfElevators];
		for(int i = 0; i < schedulerToClient.length; i++)
		{
			schedulerToClient[i] = new SchedulerElevatorHost(elevatorIp, this, SENDER_PORT_ELEVATOR+i, RECEIVE_PORT_ELEVATOR+i);
			schedulerToClient[i].start();
		}
		
		Thread schedulerFloorHost = new SchedulerFloorHost(floorIp, this, SENDER_PORT_FLOOR1, RECEIVE_PORT_FLOOR1);
		schedulerFloorHost.start();
		
		try {
			 sendReceiveSocket = new DatagramSocket(RECEIVE_PORT);  
	      } catch (SocketException se) {
	         se.printStackTrace();
	         System.exit(1);
	      } 
	}
	
	
	public int getRECEIVE_PORT_ELEVATOR()
	{
		return RECEIVE_PORT_ELEVATOR;
	}
	
	private void sendToFloor(byte [] request, int requestLength)
	{
		 try {
	         sendPacket = new DatagramPacket(request, requestLength, InetAddress.getByName(floorIp), SENDER_PORT);
	      } catch (UnknownHostException e) {
	         e.printStackTrace();
	         System.exit(1);
	      }
	      try {
		         sendReceiveSocket.send(sendPacket);
		      } catch (IOException e) {
		         e.printStackTrace();
		         System.exit(1);
		      }
	}
	
	/**
	 * Scheduler thread that does nothing in this iteration
	 */
	public void startScheduler()
	{
		while(true)
		{
			/**
			switch (state)
			{
				case Waiting:
					break;
				case ProcessRequest:
					break;
				case ProcessElevator:
					break;
			}
			**/
		}
	}

	/**
	 * Set instruction method to be called from floor class that will save the
	 * instruction
	 * 
	 * @param instruction Instruction object to be saved
	 */
	public synchronized void setInstructionsFromFloor(Instruction instruction) 
	{
		//Until there are no instructions for floor or elevator wait
		while (!(state == SchedulerStates.Waiting))
		{
			try { 
				System.out.println("wait in setInstructionsFromFloor, state:"+state);
                wait();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
		}
		
		System.out.println("Scheduler received instructions from floor");
		this.pendingInstructions.add(instruction); //Save instruction object
		notifyAll(); //Notify synchronized methods
	}

	/**
	 * Set instruction method to be called from elevator class to retrieve 
	 * instruction object
	 * 
	 * @return Instruction object
	 */
	public synchronized Instruction getInstructionForElevator(int elevatorCarNum) 
	{
		//While there are no instructions for elevator wait
		//state = SchedulerStates.ProcessRequest;
		
		List<Instruction> activeInstructions = allActiveInstructions.get(elevatorCarNum);
		
		while (activeInstructions.size() == 0 && pendingInstructions.size() == 0)
		{
			try { 
				System.out.println("Elevator "+elevatorCarNum +" waiting for Instruction");
                wait();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
		}
		
		state = SchedulerStates.ProcessRequest;
		System.out.println("Scheduler sending instructions to elevator");

		Instruction newInstruction;
		if(activeInstructions.isEmpty())
		{
			newInstruction = pendingInstructions.remove(0);
			activeInstructions.add(newInstruction);
		}
		else
		{
			newInstruction = activeInstructions.get(0);
		}
		
		state = SchedulerStates.Waiting;
		notifyAll();
		return newInstruction;
		
	}

	public void elevatorFinished(Instruction instruction) 
	{
		System.out.println("Scheduler has been notified that finished elevator has finished its instructions");
	}
	
	public synchronized void checkElevatorLocation(int elevatorCarNum, int elevatorLocation, ButtonStatus elevatorDirection, int hostIndex) 
	{

		List<Instruction> activeInstructions = allActiveInstructions.get(elevatorCarNum);
		boolean stopElevator = false;
		//While there are instructions available wait 
		while (activeInstructions.size() == 0)
		{
			try { 
				System.out.println("wait in checkElevatorLocation");
                wait();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
		}
		
		
		boolean stop = false;
		int increment = 0;
		int decrement = 0;
		//list of floor lamps false
		//list of floor lamps true
		
		for (int i = pendingInstructions.size() -1; i >= 0; --i)
		{
			if ((pendingInstructions.get(i).getFloor() == elevatorLocation) 
					&& (pendingInstructions.get(i).getButtonStatus() == activeInstructions.get(0).getButtonStatus()))
			{
				//add to floor lamps for true
				activeInstructions.add((pendingInstructions.get(i)));
				pendingInstructions.remove(pendingInstructions.get(i));
				stop = true;	
			}
		}
		
		ButtonStatus directionFloor = elevatorDirection;
		for (int i = activeInstructions.size()-1; i >= 0 ; --i)
		{
			if (activeInstructions.get(i).getFloor() == elevatorLocation)
			{
				//floorLastArrived = elevatorLocation;
				increment++;
				directionFloor = activeInstructions.get(i).getButtonStatus();
				stop = true;
			}
			
			else if (activeInstructions.get(i).getCarButton() == elevatorLocation &&
					elevatorDirection == activeInstructions.get(i).getButtonStatus())
			{
				//add to floor lamps false
				activeInstructions.remove(activeInstructions.get(i));
				decrement++;
				stop =  true;
			}
			
		}
		
		
		
		byte[] byteArray0 = {0};
		String byte0 = new String(byteArray0);
		
		String sendMsg =  Integer.toString(elevatorCarNum)+byte0
				+ Integer.toString(elevatorLocation)+byte0
				+ directionFloor.toString()+byte0
				+ Boolean.toString(stopElevator)+byte0;
		byte [] sendRequest = sendMsg.getBytes();


		sendToFloor(sendRequest, sendRequest.length);
		//floorSubsystem.elevatorReachFloor(elevatorCarNum, elevatorLocation, directionFloor, stopElevator);
		
		((SchedulerElevatorHost) schedulerToClient[hostIndex]).checkElevatorLocation(stop, increment, decrement);
		
		notifyAll(); //Notify synchronized methods
			
	}
	
	public static void main (String [] argsd)
	{
		int numOfElevators = 1;
		String floorIp = "1.1.1.1"; //NEED TO Change
		String elevatorIp = "1.1.1.1"; //NEED TO Change
		
		Scheduler scheduler = new Scheduler (floorIp, elevatorIp, numOfElevators);
		scheduler.startScheduler();
	}
	
}
