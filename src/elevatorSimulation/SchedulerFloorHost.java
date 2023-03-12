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
public class SchedulerFloorHost extends Thread
{
	
	private Scheduler scheduler;
	private SchedulerStates state;
	private String floorIp;
	
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
	public SchedulerFloorHost(String floorIp, Scheduler scheduler, int senderPort, int receivePort)
	{
		this.scheduler = scheduler;
		this.floorIp = floorIp;
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

			//set instructions from floor
			//receiveMessages = checkRequest(receivePacket.getData(), receivePacket.getLength());
			//String instructionTxt = receiveMessages.get(0);
			
			String instructionTxt = new String(receivePacket.getData(), 0, receivePacket.getLength());
			Instruction instruction = new Instruction (instructionTxt);
			scheduler.setInstructionsFromFloor(instruction);
			
		}
	}
}
