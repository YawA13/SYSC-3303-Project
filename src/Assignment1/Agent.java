package Assignment1;
import java.util.Random;

/**
 * Represents an Agent
 * @author Yaw Asamoah
 *
 */
public class Agent extends Thread
{
	/**
	 * The table the Agent is using
	 */
	private Table table;
	
	/**
	 * Constructor for the Agent
	 * 
	 * @param name		String, name of the thread
	 * @param table		Table, the table being used
	 */
	public Agent(String name, Table table)
	{
		super(name);
		this.table = table;
	}
	
	/**
	 * Run the thread and the Agent puts two random ingredients on the table, 
	 * until the number of sanwich made is greater than or equal to the 
	 * number of sandwichs wanted to be made
	 */
	public void run()
	{
		while(table.getNumOfSandwichWanted() > table.getNumOfSandwichMade())
		{
		
			Random rand = new Random();
			int ranNum = rand.nextInt(3);
			String ingredient1 = "";
			String ingredient2 = "";
			switch (ranNum)
			{
				case 0:
					ingredient1 = "Bread";
					ingredient2 = "Jelly";
					break;
				case 1:
					ingredient1 = "Jelly";
					ingredient2 = "Peanut";
					break;
				case 2:
					ingredient1 = "Bread";
					ingredient2 = "Peanut";
					break;
			}
			                                                                             
			System.out.println(this.getName()+ " placed " +ingredient1 + " and " +ingredient2 + " on the table");
			table.putIngredients(ingredient1, ingredient2);
			
			try {
	            Thread.sleep(500);
	        } 
			catch (InterruptedException e) {}
		}
		
		System.exit(0);
    
	}
	
}