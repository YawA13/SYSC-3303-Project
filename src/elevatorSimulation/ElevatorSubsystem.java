package elevatorSimulation;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Code to handle the elevator subsystem's tasks for Milestone 1 SYSC 3303.
 * ElevatorSubsystem class will receive and send instructions to floor class
 * through synchronized methods in the scheduler class
 * 
 * @author Group 3, SYSC 3303
 * @version Milestone 2
 * @since 2023-02-16
 */
public class ElevatorSubsystem extends Thread
{
	private Instruction instruction;
	private ElevatorStates state;
	private Elevator elevator;
	private int carNumber;
	private String schedulerIp;
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
   private static final int SENDER_PORT = 23;
   private static final int RECEIVE_PORT = 30;
	
	/**
	 * Initialize Elevator subsystem
	 * 
	 * @param schedulerIp Object to synchronize information between elevator and floor class
	 */
	public ElevatorSubsystem (String schedulerIP, int floors, int carNumber)
	{
		this.schedulerIp = schedulerIP;
		this.state = ElevatorStates.DoorsOpen;
		this.elevator = new Elevator(floors, this);
		this.carNumber = carNumber;
		try {
	         sendReceiveSocket = new DatagramSocket(RECEIVE_PORT);
	      } catch (SocketException se) { 
	         se.printStackTrace();
	         System.exit(1);
	      }
	}
	
	/**
	 * Start Elevator subsystem thread to repeatedly get and send instructions
	 * to scheduler
	 */
	public void startElevator()
	{
		ElevatorStates stateBeforeArriving = ElevatorStates.DoorsOpen;
		
		while (true)
		{
			switch (state)
			{
				case DoorsOpen:
					System.out.println("ElevatorSubsystem " +carNumber +" state: DoorsOpen, at floor"+ elevator.getCurrentFloor()+"\n\tStatus " +carNumber +": "+elevator.getStatus());
					if (instruction == null)
					{
						getInstructions();
						elevator.setElevatorFinalDest(instruction.getCarButton());
					}	
					elevator.setDoorOpen(false);
					state = ElevatorStates.DoorsClosing;
					break;
				case DoorsClosing:
					System.out.println("ElevatorSubsystem "+carNumber +" state: DoorsClosing, at floor:"+ elevator.getCurrentFloor());
					//System.out.println("\tStatus " +carNumber +": "+elevator.getStatus());
					setDirection();	
					break;
				case MovingUp:
					//timer
					elevator.moveUp();
					System.out.println("ElevatorSubsystem "+carNumber +" state: MovingUp, at floor"+ elevator.getCurrentFloor());
					//System.out.println("\tStatus " +carNumber +": "+elevator.getStatus());
					stateBeforeArriving = ElevatorStates.MovingUp;
					state = ElevatorStates.Arriving;
					break;
				case MovingDown:
					//timer 
					elevator.moveDown();
					System.out.println("ElevatorSubsystem "+carNumber +" state: MovingDown, at floor"+ elevator.getCurrentFloor());
					//System.out.println("\tStatus " +carNumber +": "+elevator.getStatus());
					stateBeforeArriving = ElevatorStates.MovingDown;
					state = ElevatorStates.Arriving;
					break;
				case Arriving:
					System.out.println("ElevatorSubsystem " +carNumber +" state: Arriving, at floor"+ elevator.getCurrentFloor());
					//System.out.println("\tStatus " +carNumber +": "+elevator.getStatus());
					boolean atDestination = checkElevatorLocation();
					
					if(atDestination)
					{
						state = ElevatorStates.DoorsOpening;			
					}
					else
					{
						state = stateBeforeArriving;
					}
					break;
				case DoorsOpening:
					System.out.println("ElevatorSubsystem "+carNumber +" state: DoorsOpening, at floor" + elevator.getCurrentFloor());
					//System.out.println("\tStatus " +carNumber +": "+elevator.getStatus());
					if (elevator.getCurrentFloor() == elevator.getElevatorFinalDest())
					{
						System.out.println("ElevatorSubsystem: Elevator Done");
						elevatorFinished();
						instruction = null;
						elevator.resetElevator();
					}
					
					elevator.setDoorOpen(true);
					state = ElevatorStates.DoorsOpen;
					break;
			}
			
		}
		
	}
	
	private void send(byte [] request, int requestLength)
	{
		 try {
	         sendPacket = new DatagramPacket(request, requestLength, InetAddress.getByName(schedulerIp), SENDER_PORT);
	      } catch (UnknownHostException e) {
	         e.printStackTrace();
	         System.exit(1);
	      }
	}
	
	private void receive()
	{
		 byte data[] = new byte[100];
	      receivePacket = new DatagramPacket(data, data.length);
	
	      try {
	          
	         sendReceiveSocket.receive(receivePacket);
	      } catch(IOException e) {
	         e.printStackTrace();
	         System.exit(1);
	      }
	}
	
	private void elevatorFinished()
	{
		byte [] request = {0, 2};
		String firstBytesText = new String (request);
		byte[] byteArray0 = {0};
		String byte0 = new String(byteArray0);
		
		String sendMsg = firstBytesText + instruction.getInputData()+byte0;
		byte [] sendRequest = sendMsg.getBytes();
		send(sendRequest, sendRequest.length);
	}
	
	private void setDirection()
	{
		int finalFloorNum;
		
		if (elevator.getNumOfPassengers() == 0)
		{
			finalFloorNum = instruction.getFloor();
		}
		else
		{
			finalFloorNum = instruction.getCarButton();
		}

		
		if (finalFloorNum - elevator.getCurrentFloor() > 0)
		{
			state = ElevatorStates.MovingUp;
			elevator.moving(ButtonStatus.Up);
		}
		else if (finalFloorNum - elevator.getCurrentFloor() < 0)
		{
			state = ElevatorStates.MovingDown;
			elevator.moving(ButtonStatus.Down);
		}
		else
		{
			checkElevatorLocation();	
			state = ElevatorStates.DoorsOpening;
		}
		
	}
	
	/**
	 * Get Instruction object from scheduler class and print instruction object
	 * that was received
	 */	
	private void getInstructions()
	{
		byte [] request = {0, 0};
		String firstBytesText = new String (request);
		byte[] byteArray0 = {0};
		String byte0 = new String(byteArray0);
		
		String sendMsg = firstBytesText + Integer.toString(carNumber)+byte0;
		byte [] sendRequest = sendMsg.getBytes();
		send(sendRequest, sendRequest.length);
		receive();
		
		String instructionTxt = new String(receivePacket.getData(),0,receivePacket.getLength());
		instruction = new Instruction(instructionTxt);
		
		addToActiveInstructions(instruction);	
	}


	private boolean checkElevatorLocation()
	{
		byte [] request = {0, 1};
		String firstBytesText = new String (request);
		byte[] byteArray0 = {0};
		String byte0 = new String(byteArray0);
		
		String sendMsg = firstBytesText 
				+ Integer.toString(carNumber)+byte0
				+ Integer.toString(elevator.getCurrentFloor())+byte0
				+ elevator.getDirection().toString()+byte0;
		byte [] sendRequest = sendMsg.getBytes();


		send(sendRequest, sendRequest.length);
		receive();

		/**
		boolean stop = 0;
		int increment = 0;
		int decrement = 0;
		//list of floor lamps false
		//list of floor lamps true
		 **/

		String [] receiveMessages = new String [3];
		int receiveMessagesIndex = 0;
		int textIndex = 0;
		int textLength;

		for (int i = 0; i < receivePacket.getLength(); i++)
		{
			if (receivePacket.getData() [i] == 0)
			{	

				textLength = i - textIndex;
				String possibleWord = new String(request, textIndex, textLength);
				receiveMessages [receiveMessagesIndex] = possibleWord;
				textIndex = i + 1;
				receiveMessagesIndex++;

			}
		}
		
		boolean stopElevator = Boolean.parseBoolean(receiveMessages[0]);
		int numOfPassengersJoining = Integer.parseInt(receiveMessages[1]);
		int numOfPassengersLeaving = Integer.parseInt(receiveMessages[2]);
		
		elevator.setNumOfPassengers(elevator.getNumOfPassengers()+numOfPassengersJoining);
		elevator.setNumOfPassengers(elevator.getNumOfPassengers()-numOfPassengersLeaving);

		return stopElevator;
	}
	
	
	/**
	 * public function used in test script to get instruction 
	 */
	public Instruction getCurrentInstruction() {
		return instruction; 
	}

	/**
	 * public function used in test script to call test send getInstruction() function
	 */
	public void testGetInstruciton() {
		getInstructions(); 
	}
	
	public void incrementEelevator()
	{
		elevator.setNumOfPassengers(elevator.getNumOfPassengers()+1);
	}
	
	public void decrementEelevator()
	{
		elevator.setNumOfPassengers(elevator.getNumOfPassengers()-1);
	}
	
	public ButtonStatus getElevatorDirection()
	{
		return elevator.getDirection();
	}
	
	public void turnButtonLampOn(Set<Integer> buttonNumbers)
	{
		for (int buttonNumber: buttonNumbers)
		{
			elevator.turnButtonLamp(buttonNumber, true);
		}
	}

	
	public void addToActiveInstructions(Instruction newInstruction)
	{
		//activeInstructions.add(newInstruction);
		elevator.turnButtonLamp(newInstruction.getCarButton(), true);
		System.out.println("ElevatorSubsystem "+carNumber +" added instruction " +newInstruction+"\n");
	}
	
	public int getCurrentLocation()
	{
		return elevator.getCurrentFloor();
	}

	public void removeActiveInstruction(Instruction newInstruction) {
		// TODO Auto-generated method stub
		//activeInstructions.remove(newInstruction);
		elevator.turnButtonLamp(newInstruction.getCarButton(), false);
		System.out.println("ElevatorSubsystem "+carNumber +" finished instruction " +newInstruction +"\n");
		
	}
	
	public static void main (String [] argsd)
	{
		int numOfloors = 5;
		int numOfElevators = 1;
		String schedulerIp = "1.1.1.1"; //NEED TO Change
		
		ArrayList<ElevatorSubsystem> elevatorSubsystems = new ArrayList<>();
		
		for(int i = 0; i < numOfElevators; i++)
		{
			elevatorSubsystems.add(new ElevatorSubsystem (schedulerIp, numOfloors, i));
		}
		
		
		for(int i = 0; i < numOfElevators; i++)
		{
			elevatorSubsystems.get(i).startElevator();
		}
	}
}
