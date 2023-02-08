package elevatorSimulation;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

class IterationOneTests {

	@Test
	void testInputFile() {
		//set up scheduler and floorSubsystem
		Scheduler scheduler = new Scheduler ();
		FloorSubsystem floorSubsystem = new FloorSubsystem (scheduler, "InputInstructions.txt");
		
		//reads text file and stores instructions in inst
		floorSubsystem.readInput();
		List<Instruction> inst = floorSubsystem.getInstructionsAsList();
		
		
		//tests each instruction to the expected value in the text file
		assertEquals("Time:14:05:15.0, Floor:2, Elevator Number:4, Button:Up", inst.get(0).toString()); 
		assertEquals("Time:20:00:30.0, Floor:4, Elevator Number:6, Button:Down", inst.get(1).toString()); 
	}
	
	@Test
	void testDataSharing() {
		
		//set up scheduler, elavatorSubsystem and floorSubsystem
		Scheduler scheduler = new Scheduler ();
		ElevatorSubsystem elevatorSubsytem = new ElevatorSubsystem (scheduler);
		FloorSubsystem floorSubsystem = new FloorSubsystem (scheduler, "InputInstructions.txt");
		
		//reads text file and stores instructions
		floorSubsystem.readInput();
		
		//sends instruction from floorSubsystem to scheduler
		floorSubsystem.testSendInstruciton(0); 
		
		assertEquals(floorSubsystem.getInstructionsAsList().get(0), scheduler.getCurrentInstruction()); 
		
		
		//sends instruction from scheduler to elevatorSubsystem
		elevatorSubsytem.testGetInstruciton(); 
		
		assertEquals(scheduler.getCurrentInstruction(), elevatorSubsytem.getCurrentInstruction()); 
		
		//clears instruction in scheduler to ensure the elevavtorSubsystem can send the instruction back
		scheduler.clearInstruction(); 
		
		//sends instruction from elevatorSubsystem to scheduler
		elevatorSubsytem.testSendInstruciton();
		
		assertEquals(scheduler.getCurrentInstruction(), elevatorSubsytem.getCurrentInstruction()); 
		
		System.out.println("Junit Tests passed");
		
	}

}
