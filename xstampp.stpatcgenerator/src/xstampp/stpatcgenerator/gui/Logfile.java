package xstampp.stpatcgenerator.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Logfile {
	 private static  List<String> messages =new ArrayList<String>();
     public static void addMessage (String a)
     {
         Logfile.messages.add(a);
         
     }
     
     public static List<String> getMessages ()
     {
         return messages;
     }
	public static void wirteToLog (String s)
	{
		Logger logger = Logger.getLogger("MyLog");  
	    FileHandler fh;  

	    try {  

	        // This block configure the logger with handler and formatter  
	        fh = new FileHandler("Logs.log");  
	        logger.addHandler(fh);
	        SimpleFormatter formatter = new SimpleFormatter();  
	        fh.setFormatter(formatter);  

	        // the following statement is used to log any messages  
	       // logger.info(s);  

	    } catch (SecurityException e) {  
	        e.printStackTrace();  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }  

	   
	}
}
