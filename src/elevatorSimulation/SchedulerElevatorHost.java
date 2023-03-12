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
public class SchedulerElevatorHost extends Thread
{
	
	private Scheduler scheduler;
	private SchedulerStates state;
	private String elevatorIp;
	
   private DatagramPacket sendPacket, receivePacket;
   
   /**
    * sockets to send and receive data
    */
   private DatagramSocket sendReceiveSocket;

   private int senderPort;
   private int receivePort;
   private ArrayList<String> receiveMessages;
	
	/**
	 * Initialize scheduler object
	 */
	public SchedulerElevatorHost(String elevatorIp, Scheduler scheduler, int senderPort, int receivePort)
	{
		this.scheduler = scheduler;
		this.elevatorIp = elevatorIp;
		this.senderPort = senderPort;
		this.receivePort = receivePort;
		receiveMessages = new ArrayList();
		state = SchedulerStates.Waiting;
	
		 try {
			 sendReceiveSocket = new DatagramSocket(receivePort);  
	      } catch (SocketException se) {
	         se.printStackTrace();
	         System.exit(1);
	      } 
	}
	
	

	/**
	 * Scheduler thread that does nothing in this iteration
	 */
	public void run()
	{
		while(true)
		{
			byte data[] = new byte[100];
			receivePacket = new DatagramPacket(data, data.length);
			System.out.println("Server: Waiting for Packet.\n");

			// Block until a datagram packet is received from receiveSocket.
			try {        
				System.out.println("Waiting..."); // so we know we're waiting
				sendReceiveSocket.receive(receivePacket);
			} catch (IOException e) {
				System.out.print("IO Exception: likely:");
				System.out.println("Receive Socket Timed Out.\n" + e);
				e.printStackTrace();
				System.exit(1);
			}

			
			receiveMessages = checkRequest(receivePacket.getData(), receivePacket.getLength());
			if ((receivePacket.getData() [0] == 0) && (receivePacket.getData()[1] == 0))
			{
				//getelevatorInstruction
				int carNum = Integer.parseInt(receiveMessages.get(0));
				Instruction newInstruction = scheduler.getInstructionForElevator(carNum);
				
				byte [] sendMsg= newInstruction.getInputData().getBytes();
				send(sendMsg, sendMsg.length);
				
			}

			else if ((receivePacket.getData() [0] == 0) && (receivePacket.getData()[1] == 1))
			{
				//check elevator location
				int carNum = Integer.parseInt(receiveMessages.get(0));
				int elevatorLocation = Integer.parseInt(receiveMessages.get(1));
				ButtonStatus direction;
				
				if (receiveMessages.get(2).equalsIgnoreCase("Up")) //If buttonStatus is up
				{
					direction = ButtonStatus.Up; //Set to enum Up
				}
				else //Else ButtonStatus is down
				{
					direction = ButtonStatus.Down; //Set to enum down
				}
				
				int posOfHost = receivePort - scheduler.getRECEIVE_PORT_ELEVATOR();
				scheduler.checkElevatorLocation(carNum, elevatorLocation, direction, posOfHost);
				
			}
			else if ((receivePacket.getData() [0] == 0) && (receivePacket.getData()[1] == 2))
			{
				//elevator finished
				String instructionTxt = receiveMessages.get(0);
				Instruction instruction = new Instruction (instructionTxt);
				scheduler.elevatorFinished(instruction);
			}
			
			
			
		}
	}

	private void send(byte [] request, int requestLength)
	{
		 try {
	         sendPacket = new DatagramPacket(request, requestLength, InetAddress.getByName(elevatorIp), senderPort);
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
	
	private ArrayList<String> checkRequest (byte [] request, int requestLength)
	{
		ArrayList<String> receiveMessages = new ArrayList<>();
		int textIndex = 2;
		int textLength;

		for (int i = 2; i < requestLength; i++)
		{
			if (request [i] == 0)
			{	

				textLength = i - textIndex;
				String possibleWord = new String(request, textIndex, textLength);
				receiveMessages.add(possibleWord);
				textIndex = i + 1;

			}
		}
		
		return receiveMessages;
	}
	
	
	public void checkElevatorLocation(boolean stop, int increment, int decrement) 
	{
		byte[] byteArray0 = {0};
		String byte0 = new String(byteArray0);
		
		String sendMsg = Boolean.toString(stop)+byte0
				+ Integer.toString(increment)+byte0
				+ Integer.toString(decrement)+byte0;
		byte [] sendRequest = sendMsg.getBytes();

		send(sendRequest, sendRequest.length);
	}
	
}
