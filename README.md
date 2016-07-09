# Fast-mail-sending-framework
Reads e-mails from Mysql db and sends them over SMTP server efficiently

Folder Structure:-

1.) multiple_mail_send__final->Contains src of Final working code(Which actually sends email).I have used Mandrill smtp mail server.If you come across any doubts 
	regarding proper delivery of mails,you can visit "Mandrill.com" with "username->Your mandrill username  password->"Your mandrill password" to see mail sending 
	statistics.

2.) multiple_mail_send_test->Contains test code that i used to measure execution speed assuming Mail-sending process as a "3-second" process.Mandrill and any 
	other smtp server restricts us by setting a limit on (no. of email sends)/hour.To test this application,on a large no. of rows and make it a 
	connectivity independent(mail-sending depends on ones connectivity) application to optimize it,this code was used.
	
3.) External JARs used-Used 2 JARs,Javamail Jar and mysqlJava connector JAR.

4.) db_creation-Sql statements to create a sample email database.

5.) Optimization steps-Techniques used to speed up this application.

These are few important points before running this application:-

1.) Javamail API doesn't support Email-sending on Proxy server.So,This application will not work in a proxy server.

2.) I am using "phpmyadmin" as database service.Install Xampp for windows.Once installed,open Xampp control panel and turn apache and Mysql "on".
	then go to "localhost/phpmyadmin/" to see Phpmyadmin UI.You can then execute queries there to create tables.
	
3.) You can decrease no. of threads in thread pool if incase program execution gets stuck.1700 thread was a rough estimate which i used,as obtained
	from test code.I have not tried sending huge no. of actual emails due to Mandrill mail-sending restrictions.

Assumptions made:-

1.) Email address provided are valid.

2.) Whenever a new mail insertion is done in DB,id of new mail is incremented by 1(auto increment on id).
