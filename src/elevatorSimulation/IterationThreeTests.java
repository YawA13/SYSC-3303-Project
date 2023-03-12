package elevatorSimulation;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

class IterationThreeTests {

	@Test
	void unpackInstruction() {
		
		Scheduler scheduler = new Scheduler(1);
		FloorSubsystem floorSubsystem = new FloorSubsystem (scheduler, "InputInstructions.txt", 5, 1);
		
		//reads text file and stores instructions in inst
		floorSubsystem.readInput();
		List<Instruction> inst = floorSubsystem.getInstructionsAsList();
		String[] strings = new String[inst.size()];
		Instruction[] newIsnt = new Instruction[inst.size()]; 
		
		for(int i1 = 0; i1 < inst.size(); i1++) {
			strings[i1] = inst.get(i1).toInputData();
			newIsnt[i1] = new Instruction(strings[i1]);
		}
		
		for(int i2 = 0; i2 < inst.size(); i2++) {
			
			assertEquals(newIsnt[i2].getTime(), inst.get(i2).getTime()); 
			assertEquals(newIsnt[i2].getFloor(), inst.get(i2).getFloor());
			assertEquals(newIsnt[i2].getCarButton(), inst.get(i2).getCarButton());
			assertEquals(newIsnt[i2].getButtonStatus(), inst.get(i2).getButtonStatus());
		}
	}

}

