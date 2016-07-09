import java.sql.*;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Final {

	public static void main(String[] args) {
		long start=System.currentTimeMillis();
		Connection con=null;
		Statement st=null;
		ResultSet rs=null;
		Scanner in=new Scanner(System.in);
		int num_pages=0,max_id=0;
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			con=DriverManager.getConnection("jdbc:mysql://localhost:3306/multiple_mail_send","root","");
			st=con.createStatement();
			rs=st.executeQuery("Select Max(id) from test");rs.next();
			max_id=rs.getInt(1);
			num_pages=max_id/10000;
			//if(num_pages*10000<max_id) num_pages++;
			//System.out.println(num_pages);
		}catch(Exception ex)
		{
			System.out.println("Error:"+ex);
		}
		//Fetching 1st page
		DBread d;
		if((max_id+1)<10000) {d=new DBread(1,max_id+1,con,st);}
		else {d=new DBread(1,10000,con,st);}
		d.Firstfetch(st);
		//Start mail sending + Data Fetching Thread pool
		int i,j,k;
		//Everytym a new pool is created bcoz we have to read db page before sending mails
		for(i=0;i<=num_pages;i++)   //num_pages loop
		{
			ExecutorService pool=Executors.newFixedThreadPool(1700);
			System.out.println("Num_page:"+i);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			int siz=10001,l=0;
			if(i==num_pages) {siz=(max_id%10000)+1;}                   //last page
			Activity vec[]=new Activity[siz];
			System.out.println("aprev size:"+DBread.aprev.size());
			for(k=0;k<vec.length-1;k++)
				vec[k]=new Sendmail(((i)*10000+k+1)+"@gmail.com",3);   //Send mails of prev. page
			l=vec.length-1;
			if(i!=num_pages) {l++;vec[k]=new DBread((i+1)*10000,(i+2)*10000,con,st);}				//Read mails of next page
			for(k=0;k<l;k++)
				pool.submit(vec[k]); 
			pool.shutdown();
			while(!pool.isTerminated())
			{
			}	
	    }
		long end=System.currentTimeMillis();
		System.out.println("Time taken:"+(end-start)+" milliseconds");
	}

}
