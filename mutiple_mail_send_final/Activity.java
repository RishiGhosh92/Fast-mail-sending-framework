/*
 * This is a dummy Class which is Used for Thread pooling using ExecutorService
 * DBread and Sendmail class inherit this Class.This is done to make a thread pool of
 * Activity objects.These objects can perform both DBread and Mailsend operations.
 */
public class Activity implements Runnable
{

	@Override
	public void run() {
		
		
	}

}
