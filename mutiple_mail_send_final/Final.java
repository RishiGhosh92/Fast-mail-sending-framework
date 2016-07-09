/*
 * Author-Rishi Ghosh(Mail Sender) 
 * This is the main Class.It counts the number of DB pages to be read using max(id).
 * Then before starting a Thread pool,1st DB page read is performed to provide Email objects to Mailsend thread
 * during 1st thread pool execution.
 * Then for each i-th Thread pool execution(it exeutes #DBpages-1):
 * 		Thread pool of Activity objects is created.
 * 		Here Mailsending for i-th DB page Email objects and DB (i+1)-th page reading is performed Concurrently
 * 		Atlast,waiting for all threads in threadpool to finish is done.This is important because,DBpage read
 * 		thread is important for next cycle Email sending(Otherwise All Email Objects are not created).     
 */

import java.sql.*;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

public class Final {

	public static void main(String[] args) {
		long start=System.currentTimeMillis();
		Connection con=null;
		Statement st=null;
		ResultSet rs=null;
		Properties properties=null;
		Session session=null;
		int num_pages=0,max_id=0;
		
		//Mandrill credentials for using mandrillapp as smtp mail server.Please create a Mandrill app account first to use this
		final String username="Your_mandrill_username@gmail.com";
	    final String password="Your)mandrill_password";
	    try
		{
			//Connection to "Coupondunia" Database is made
	    	Class.forName("com.mysql.jdbc.Driver");
			con=DriverManager.getConnection("jdbc:mysql://localhost:3306/multiple_mail_send","root","");
			st=con.createStatement();
			
			//This is used to determine # DB pages to be read
			rs=st.executeQuery("Select Max(id) from emailqueue");rs.next();
			max_id=rs.getInt(1);
			
			//num_pages->no. of DB pages to be read
			num_pages=max_id/10000;
		    
			//Persistent object to store properties
			properties = new Properties();
		      
		    // Setting up Mandrillapp mail server
		    properties.put("mail.smtp.auth", "true");
		    properties.put("mail.smtp.starttls.enable", "true"); 
		    properties.put("mail.smtp.host", "smtp.mandrillapp.com");
		    properties.put("mail.smtp.port", "587");
		     
		    //Session is created using Mandrill credentials to use Mandrill as smtp mail server
		    session = Session.getInstance(properties,
		    	    new javax.mail.Authenticator() {
		    	       protected PasswordAuthentication getPasswordAuthentication() {
		    	          return new PasswordAuthentication(username, password);
		    	         }
		    	      });
		    
		}catch(Exception ex)
		{
			System.out.println("Error:"+ex);
		}
	    
		//Fetching 1st DB page(Here DB page size=10000 rows)
		DBread d;
		if((max_id+1)<10000) {d=new DBread(1,max_id+1,st);}
		else {d=new DBread(1,10000,st);}
		
		//Firstfetch method of DBread is called passing statement object as parameter
		d.Firstfetch(st);
		int i,k;
		/*
		 * Start mail sending + Data Fetching Thread pool
		 * Everytym a new pool is created because we have to read i-th db page before sending mails of i-th db page 
		 */
		for(i=0;i<=num_pages;i++)   //num_pages loop
		{
			ExecutorService pool=Executors.newFixedThreadPool(1700);	//A Thread pool of 1700 threads is created
			//System.out.println("Num_page:"+i);
			int siz=10001,l=0;
			if(i==num_pages)	//last page
			{
				siz=(max_id%10000)+1;
			}                   
			
			//Thread pool of Activity objects
			Activity vec[]=new Activity[siz];
			
			//Send mails of previous DB page
			for(k=0;k<vec.length-1;k++)
				vec[k]=new Sendmail(DBread.aprev.get(k),session);   
			l=vec.length-1;
			
			//if this is not last page then perform DBread for next cycle Email object creation
			if(i!=num_pages) {l++;vec[k]=new DBread((i+1)*10000,(i+2)*10000,st);}				//Read mails of next page
			
			//Start submitting Activity objects to Thread pool
			for(k=0;k<l;k++)
				pool.submit(vec[k]); 
			
			pool.shutdown();
			//waiting for all threads in threadpool to finish is done.
			while(!pool.isTerminated())
			{
			}	
	    }
		
		long end=System.currentTimeMillis();
		System.out.println("Time taken:"+(end-start)+" milliseconds");
	}

}
