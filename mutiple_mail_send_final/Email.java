/*
 * Email class is used to represent a type email.This was useful in making Arraylist of Email objects
 * where each Email object represents an email to be sent.
 */
public class Email {
	
	//Email properties
	private int id;
	private String from;
	private String to;
	private String subject;
	private String body;
	
	//Email getter methods
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
	
	//Email setter methods
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
