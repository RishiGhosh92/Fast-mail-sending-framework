import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class DBread extends Activity implements Runnable
{
	private Connection con;
	private Statement st;
	private ResultSet rs;
	private int lower=0,upper=0;
	public String Delimiter="\\|";
	public static ArrayList<Email> aprev;
	public static ArrayList<Email> anext=new ArrayList<Email>();
	public DBread(int lower,int upper,Connection con,Statement st)
	{
		this.lower=lower;
		this.upper=upper;
		this.con=con;
		this.st=st;
	}
	
	public void run()
	{
		System.out.println("lower:"+lower);
		System.out.println("upper:"+upper);
		try {
			//aprev=new ArrayList<Email>(anext);
			aprev.clear();
			rs=st.executeQuery("select data_e from test where id>="+lower+" and id<"+upper);
			while(rs.next())
			{
				Email e=new Email();
				String s=rs.getString("data_e");
				String[] mail=s.split(Delimiter);
				//System.out.println(mail[0]+"-"+mail[1]+"-"+mail[2]+"-"+mail[3]+"-"+mail[4]);
				e.setid(Integer.parseInt(mail[0]));
				e.setfrom(mail[1]);
				e.setto(mail[2]);
				e.setsubject(mail[3]);
				e.setbody(mail[4]);
				aprev.add(e);
			}
			//System.out.println("From run() size of anext:"+anext.size());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void Firstfetch(Statement st)
	{
		try {
			aprev=new ArrayList<Email>();
			rs=st.executeQuery("select data_e from test where id<"+upper);
			while(rs.next())
			{
				Email e=new Email();
				String s=rs.getString("data_e");
				String[] mail=s.split(Delimiter);
				//System.out.println(mail[0]+"-"+mail[1]+"-"+mail[2]+"-"+mail[3]+"-"+mail[4]);
				e.setid(Integer.parseInt(mail[0]));
				e.setfrom(mail[1]);
				e.setto(mail[2]);
				e.setsubject(mail[3]);
				e.setbody(mail[4]);
				aprev.add(e);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
