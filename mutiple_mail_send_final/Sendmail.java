/*
 * Sendmail is Mail sending thread which inherits Activity so as to perfom Mail Sending tasks parallely with 
 * DB Fething tasks.It receives Email object to be sent and Session created for sending mails over smtp server.
 * Major function of this Class is "Sending Emails"  
 */

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Sendmail extends Activity implements Runnable
{ 
	static int j=0;
	//private Properties properties;
	private Session session;
	private Email email;
	
	//Sendmail constructor
	public Sendmail(Email email,Session session)
	{
		this.email=email;
		//this.properties=properties;
		this.session=session;
	}
	
	/*
	 * Things to be executed in a thread overrides Activity.run()
	 * This method just sends an email using JavaMail API when an Email object is provided to it
	 */
	public void run()
	{
		System.out.println(email.getid()+" Sending....");
		try{
	         //MimeMessage object creation using received session
	         MimeMessage message = new MimeMessage(session);
	         
	         //SetFrom:to set the address of sender of the mail
	         message.setFrom(new InternetAddress(email.getfrom()));
	         
	         //setReplyTo: sets the address to which Receiver must reply
	         message.setReplyTo(new javax.mail.Address[]
	        		 {
	        		     new javax.mail.internet.InternetAddress(email.getfrom())
	        		 });
	         
	         //SetTo:to set the address of receiver of the mail
	         message.addRecipient(Message.RecipientType.TO,new InternetAddress(email.getto()));
	         
	         // SetSubject: set subject of email
	         message.setSubject(email.getsubject());
	         
	         // setText:used to set Email body
	         message.setText(email.getbody());	        
	         
	         Transport t = session.getTransport("smtp");
	         t.connect();
	         
	         // Send message
	         t.sendMessage(message,message.getAllRecipients());
	         
	         if(t!=null) t.close();
	         //Transport.send(message);
	         System.out.println("Sent message "+email.getid()+" successfully....");
	      }
		  catch (MessagingException e) 
		  {
	         System.out.println("Error:"+e.getMessage());
	      }
		
    }
}
