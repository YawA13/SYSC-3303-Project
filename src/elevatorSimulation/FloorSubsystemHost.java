package elevatorSimulation;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Code to handle the floor subsystem's tasks for Milestone 1 SYSC 3303.
 * FloorSubsystem class will read a text file and send and receive instructions
 * to elevator class through synchronized methods in the scheduler class
 * 
 * @author Group 3, SYSC 3303
 * @version Milestone 1
 * @since 2023-02-04
 */
public class FloorSubsystemHost extends Thread
{

	private String schedulerIp;
	private FloorSubsystem floorSubsystem;
	private static final int RECEIVE_PORT = 40;
	private static final int SENDER_PORT = 33;
	/**
	 * packets to receive and send data
	 */
   private DatagramPacket sendPacket, receivePacket;
   
   /**
    * sockets to send and receive data
    */
   private DatagramSocket sendReceiveSocket;
   
   
	
	/**
	 * Initialize Floor subsystem
	 * 
	 * @param scheduler Scheduler object to synchronize information between elevator and floor class
	 * @param inputLoc String location of text file to read
	 */
	public FloorSubsystemHost(String schedulerIp, FloorSubsystem floorSubsystem)
	{
		this.schedulerIp = schedulerIp;
		this.floorSubsystem = floorSubsystem;
		try {
	         sendReceiveSocket = new DatagramSocket(RECEIVE_PORT);
	      } catch (SocketException se) { 
	         se.printStackTrace();
	         System.exit(1);
	      }
		
	}
	
	
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
				System.out.println("Received packet");
			} catch (IOException e) {
				System.out.print("IO Exception: likely:");
				System.out.println("Receive Socket Timed Out.\n" + e);
				e.printStackTrace();
				System.exit(1);
			}

			//set floor floor lamp fron scheduler
			
			ArrayList<String> receiveMessages = checkRequest(receivePacket.getData(), receivePacket.getLength());
			
			int elevatorCarNum = Integer.parseInt(receiveMessages.get(0));
			int elevatorLocation = Integer.parseInt(receiveMessages.get(1));
			
			ButtonStatus direction;
			if (receiveMessages.get(2).equalsIgnoreCase("Up")) //If buttonStatus is up
			{
				direction = ButtonStatus.Up; //Set to enum Up
			}
			else // Else ButtonStatus is down
			{
				direction = ButtonStatus.Down; //Set to enum Down
			}
			
			boolean stopElevator = Boolean.parseBoolean(receiveMessages.get(3));
			
			floorSubsystem.elevatorReachFloor(elevatorCarNum, elevatorLocation, direction, stopElevator);
			
		}
	}
	
	
	private ArrayList<String> checkRequest (byte [] request, int requestLength)
	{
		ArrayList<String> receiveMessages = new ArrayList<>();
		int textIndex = 0;
		int textLength;

		for (int i = 0; i < requestLength; i++)
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

	
}
