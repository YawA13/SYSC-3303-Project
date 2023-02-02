import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Floor extends Thread
{

	private Scheduler scheduler;
	private String inputLoc;
	
	public Floor(Scheduler scheduler, String inputLoc)
	{
		this.scheduler = scheduler;
		this.inputLoc = inputLoc;
	}
	
	
	public void run()
	{
		Instruction instruction;
		try {
			File inputInstructions = new File(inputLoc);
			Scanner scan = new Scanner(inputInstructions);
			while (scan.hasNextLine())
			{
				String data = scan.nextLine();
				String [] split = data.split("\\t");
				instruction = new Instruction(split[0], Integer.parseInt(split[1]), Integer.parseInt(split[2]));
				
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
