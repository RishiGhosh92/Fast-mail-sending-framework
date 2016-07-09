
public class Sendmail extends Activity implements Runnable
{ 
	static int j=0;
	private String email;
	private int time;
	public Sendmail(String email,int time)
	{
		this.email=email;
		this.time=time;
	}
	public void run()
	{
		System.out.println(email+" started");
		
		try
		{
			for(int i=1;i<=time;i++)
			Thread.sleep(1000);
		}catch(Exception e)
		{
			System.out.println("Error:"+e);
		}
		System.out.println(email+" sent");
    }
}
