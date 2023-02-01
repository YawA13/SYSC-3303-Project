package Assignment1;

/**
 * Represents a Chef
 * @author Yaw Asamoah
 *
 */
public class Chef extends Thread
{
	/**
	 * The ingredient the Chef has an infinite supply of
	 */
	private String ingredient;
	
	/**
	 * The table the Chef is using
	 */
	private Table table;
	
	
	/**
	 * Constructor for Chef
	 * 
	 * @param name				String, the name of the thread
	 * @param ingredient		String, the ingredient the chef has
	 * @param table				Table, the table being used by the Chef
	 */
	public Chef(String name, String ingredient, Table table)
	{
		super(name);
		this.ingredient = ingredient;
		this.table = table;
	}
		
	/**
	 * Runs the thread, and selects the two ingredients on the table 
	 * until the number of sanwichs made is greater than or equal to the 
	 * number of sandwichs wanted to be made 
	 */
    public void run() 
    {
    	while(table.getNumOfSandwichWanted() > table.getNumOfSandwichMade())
    	{
    		table.getOtherIngredients(ingredient);
    		System.out.println(this.getName()+" made and consumed sandwich #" +table.getNumOfSandwichMade() +" with his supply of "+ingredient);
    		System.out.println(" ");	
    	}	
    }
      
}
