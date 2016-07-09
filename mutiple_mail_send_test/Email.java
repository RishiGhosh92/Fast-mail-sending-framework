
public class Email {
	private int id;
	private String from;
	private String to;
	private String subject;
	private String body;
	public int getid()
	{
		return id;
	}
	public String getfrom()
	{
		return from;
	}
	public String getto()
	{
		return to;
	}
	public String getsubject()
	{
		return subject;
	}
	public String getbody()
	{
		return body;
	}
	public void setid(int id)
	{
		this.id=id;
	}
	public void setfrom(String from)
	{
		this.from=from;
	}
	public void setto(String to)
	{
		this.to=to;
	}
	public void setsubject(String subject)
	{
		this.subject=subject;
	}
	public void setbody(String body)
	{
		this.body=body;
	}
	
}
