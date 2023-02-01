package Assignment1;

/**
 * Represents a Table
 * @author Yaw Asamoah
 *
 */
public class Table 
{
	/**
	 * true if table is empty, false is table not empty
	 */
	private boolean emptyTable;
	
	/**
	 * Ingredient 1 on the table
	 */
	private String ingredient1;
	
	/**
	 * Ingredient 2 on the table
	 */
	private String ingredient2;
	
	/**
	 * Number of sandwich wanted to be made on the table
	 */
    private int numOfSandwichWanted;
    
    /**
     * Number of sandwich made on the table
     */
    private int numOfSandwichMade;
	
    /**
     * Constructor for Table
     * 
     * @param numOfSandwichWanted		int, number of sandwich wanted to be made
     */
	public Table(int numOfSandwichWanted)
	{
		this.numOfSandwichWanted = numOfSandwichWanted;
	    numOfSandwichMade = 0;
		ingredient1 = "";
		ingredient2 = "";
		emptyTable = true;
	}
		
	/**
	 * Puts two ingredients on the table
	 * 
	 * @param ingredient1		String, ingredient 1 on the table
	 * @param ingredient2		String, ingredient 2 on the table
	 */
	public synchronized void putIngredients(String ingredient1, String ingredient2) 
	{
		while (!emptyTable)
		{
            try 
            { 
            	wait();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }

		this.ingredient1 = ingredient1;
		this.ingredient2 = ingredient2;
		emptyTable = false;
		notifyAll();
	}
	
	/**
	 * Gets the two ingredients on the table if they are different from the one specified
	 * 
	 * @param ingredient		String, ingredient from the chef
	 */
    public synchronized void getOtherIngredients(String ingredient)
    {
    	while ((ingredient1.equals(ingredient)) || (ingredient2.equals(ingredient)) || emptyTable)
    	{
            try { 
                wait();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }
        
    	this.ingredient1 = "";
		this.ingredient2 = "";
    	numOfSandwichMade++;
    	emptyTable = true;
        notifyAll();
    }
	
	/**
	 * Gets the number of sandwichs wanted
	 * 
	 * @return			int, the number of sandwichs wanted
	 */
    public int getNumOfSandwichWanted()
    {
    	return numOfSandwichWanted;
    }
    
    /**
     * Gets the number of sandwichs made On the table
     * 
     * @return			int, the number of sandwichs made
     */
    public int getNumOfSandwichMade()
    {
    	return numOfSandwichMade;
    }
    
    
}