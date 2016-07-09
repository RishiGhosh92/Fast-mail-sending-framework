/*
 * DBread is DB records reading thread which inherits Activity so as to perfom Data reading tasks parallely with 
 * Mail sending tasks.DB records reading is performed page by page so as to speed up execution and no memory problem
 * arises.It receives lower row index and upper row index which determines a page to be read by that object.
 * Major function of this Class is "Database Reading and Email Object Creation using Paging"  
 */

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DBread extends Activity implements Runnable
{
	//private Connection con;
	private Statement st;
	private ResultSet rs;
	private int lower=0,upper=0;
	public String Delimiter="\\|";   //Delimiter used in "data_email" field of db
	public static ArrayList<Email> aprev;	//After Page reading,aprev is arraylist of Email objects created
	
	//DBread constructor which receives page lower index and page upper index
	public DBread(int lower,int upper,Statement st)
	{
		this.lower=lower;
		this.upper=upper;
		//this.con=con;
		this.st=st;
	}
	
	/*
	 * Things to be executed in a thread overrides Activity.run()
	 * This method reads a record from DB page and creates Email object and adds it to aprev Arraylist 
	 */
	public void run()
	{
		try {
			
			//previous DB page data(which is already sent) is cleared to make space for new page data
			aprev.clear();
			
			//only data_email column is read(for speedup) for lower row and Upper row index being passed
			rs=st.executeQuery("select data_email from emailqueue where id>="+lower+" and id<"+upper);
			
			//This loop creates an Arraylist of Email objects from the data being fetched by the query
			while(rs.next())
			{
				Email e=new Email();
				String s=rs.getString("data_email");
				
				//split function is used to split a string given a delimiter("|") as a parameter and returns String array
				String[] mail=s.split(Delimiter);
				e.setid(Integer.parseInt(mail[0]));
				e.setfrom(mail[1]);
				e.setto(mail[2]);
				e.setsubject(mail[3]);
				e.setbody(mail[4]);
				
				//Email object e is added to aprev arraylist
				aprev.add(e);
			}
		} 
		catch (SQLException ex) 
		{
			System.out.println("Error:"+ex.getMessage());
		}
	}
	
	/*
	 * This method is called before the Mail Sending + DB Fetch Thread pool starts
	 * This reads 1st DB page and constructs Email objects to be used by Mailsend during 1st Thread pool execution
	 */
	public void Firstfetch(Statement st)
	{
		try {
			aprev=new ArrayList<Email>();
			
			//only data_email column is read(for speedup) for lower row=1 and Upper row index being passed(Page-1)
			rs=st.executeQuery("select data_email from emailqueue where id<"+upper);
			
			//This loop creates an Arraylist of Email objects from the data being fetched by the query
			while(rs.next())
			{
				Email e=new Email();
				String s=rs.getString("data_email");
				
				//split function is used to split a string given a delimiter("|") as a parameter and returns String array
				String[] mail=s.split(Delimiter);
				e.setid(Integer.parseInt(mail[0]));
				e.setfrom(mail[1]);
				e.setto(mail[2]);
				e.setsubject(mail[3]);
				e.setbody(mail[4]);
				
				//Email object e is added to aprev arraylist
				aprev.add(e);
			}
		} 
		catch (SQLException e) 
		{
			System.out.println("Error:"+e.getMessage());
		}
	}
}
