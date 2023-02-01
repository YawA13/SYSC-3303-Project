package Assignment1;

/**
 * Creates 20 random sandwichs
 * @author Yaw Asamoah
 *
 */
public class AgentChef 
{
	public static void main(String[] args)
	{
		Thread agentThread, chefThread1, chefThread2, chefThread3;
		Table table = new Table(20);
		
		Agent agent = new Agent("Agent",table);
		Chef chef1 = new Chef("Chef1", "Peanut", table);
		Chef chef2 = new Chef("Chef2","Jelly", table);
		Chef chef3 = new Chef("Chef3","Bread", table);
		
		agentThread = new Thread(agent);
		chefThread1 = new Thread (chef1);
		chefThread2 = new Thread (chef2);
		chefThread3 = new Thread (chef3);
		
		agentThread.start();
		chefThread1.start();
		chefThread2.start();
		chefThread3.start();

	}

}
