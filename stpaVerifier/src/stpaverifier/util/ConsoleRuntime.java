package stpaverifier.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Scanner;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.ui.console.IOConsole;
import org.eclipse.ui.console.IOConsoleOutputStream;

import stpaverifier.controller.IProperty;
import xstampp.ui.common.ProjectManager;

public class ConsoleRuntime extends Thread{

	/**
	 * the static boolean RUNTIME_UNLOGGED makes sure that the log for every execution
	 * gets a distinct header with the execution time
	 */
	private static boolean RUNTIME_UNLOGGED = true;
	private static String NUMBER_OF_LOGS="NUMBER_OF_LOGS";
	
	private ProcessBuilder builder;
	private IOConsole consoleOutput;
	private IProject logParentResource;
	private StringBuilder alternateBuffer;
	private Process checkProcess;
	private int state;
	private ICMDScanner scanner;
	private String inlineArg;
	
	/**
	 * If the logParentResource was defined with setLogParentResource(String)
     * than the process out stream is stored in a log file 
     * 
	 * @param builder the process
	 * @param out the IOConsole the output of the builder execution is written to, null if no console logging is wanted
	 * @param outString as an alternative to the console output one can give in a stringbuilder the output is written to
	 * 					</br><i>can be null</i>
	 * @param scanner the ICMDScanner implementation which should check the lines which are put to the stdout by the 
	 * 					command provided by the builder, can be <code>null</code>
	 */
	public ConsoleRuntime(ProcessBuilder builder,IOConsole out,StringBuilder outString,ICMDScanner scanner) {
		this.builder = builder;
		this.consoleOutput = out;
		this.alternateBuffer = outString;
		this.scanner = scanner;
		checkProcess = null;
	}

	public void setInlineCommand(String command){
		this.inlineArg = command;
	}
	public void cancel(){
		if(checkProcess!= null){
			checkProcess.destroy();
		}
		state = IProperty.STATE_CANCELED;
	}
	
    @Override
	public void run() {
    	//in case the try/catch encounters an error the Scanner must be made known so it can be closed
    	Scanner sc = null;
    	try {
        	String cmd = "";
        	builder.environment();
        	for(String arg:builder.command()){
        		cmd = cmd.concat(arg + " ");
        	}
        	ProjectManager.getLOGGER().debug("executing terminal command: " + cmd);
//    		builder.redirectErrorStream(true);
    		checkProcess = builder.start();
    		if(inlineArg != null){
    			checkProcess.getOutputStream().write(inlineArg.getBytes());
    			checkProcess.getOutputStream().flush();
    			checkProcess.getOutputStream().close();
    		}
    		InputStream stdOut =checkProcess.getInputStream();
    		/*
    		 * The scanner constantly scans the out stream of the process which can than be red by the program 
    		 */
        	sc = new Scanner(stdOut);
        	IOConsoleOutputStream consoleStream = null;
        	if(consoleOutput != null){
        		consoleStream = consoleOutput.newOutputStream();
        		consoleStream.write(cmd + "\n");
        	}
        	if(consoleOutput != null){
	            consoleOutput.setWaterMarks(29999, 30000);
	            consoleStream.write("\n");
        	}
        	String line;
        	state = IProperty.STATE_SUCCESS;
            while (sc.hasNext() && (state & IProperty.STATE_CANCELED) == 0) {
            	line =sc.nextLine();
            	if(this.scanner != null){
            		state = scanner.scanLine(line, state);
            	}
            	if(consoleOutput != null){
            		consoleStream.write(line + "\n");
            	}
            	if(alternateBuffer != null){
            		alternateBuffer.append(line);
            		alternateBuffer.append(System.lineSeparator());
            	}
            }
            if(consoleStream != null){
            	consoleStream.close();
            }
            /*
             * If the logParentResource was defined with setLogParentResource(String)
             * than the process out stream is stored in a log file 
             */
            if(getLogParentResource() != null){
            	StringBuilder logString = new StringBuilder();
            	/*
            	 * if the log file reaches a file size of more then a megabyte than its content is cmoved to a backup log
            	 * and the active log is marked as a continuation of the backup
            	 */
    			long mega = Long.rotateLeft(1L, 20);
    			IFile log = getLogParentResource().getFile(".log");
    			if(log.exists() && new File(log.getLocationURI()).length() >= mega){
    				String numberofLogs = log.getPersistentProperty(new QualifiedName(NUMBER_OF_LOGS,NUMBER_OF_LOGS));
    				int bak_number=1;
    				if(numberofLogs != null){
    					try{
    						bak_number = Integer.parseInt(numberofLogs);
    					}catch(NumberFormatException e){
    						//if no number is found than the backup which is created must bew the first
    					}
    				}
        			IFile backup_log = getLogParentResource().getFile(".bak_"+bak_number+".log");
        			log.setPersistentProperty(new QualifiedName(NUMBER_OF_LOGS,NUMBER_OF_LOGS), String.valueOf(bak_number++));
        			if(!backup_log.exists()){
        				backup_log.create(log.getContents(), true, null);
        			}
        			RUNTIME_UNLOGGED = true;
        			log.setContents((InputStream)null, 0, null);
        			logString.append("This file is a continuation of "+backup_log.getName());
    			}else if(!log.exists()){
        			RUNTIME_UNLOGGED = true;
    			}
    			
            	if(RUNTIME_UNLOGGED){
            		logString.append(System.lineSeparator());
            		logString.append("-----------STPA Verifier execution ");
	            	SimpleDateFormat format = new SimpleDateFormat(); 
	        		GregorianCalendar datum = new GregorianCalendar();
	        		logString.append(format.format(datum.getTime()));
	        		logString.append("-----------");
	        		logString.append(System.lineSeparator());
	            	RUNTIME_UNLOGGED=false;
            	}
            	logString.append(alternateBuffer.toString());
    			if(!log.exists()){
    				log.create(new ByteArrayInputStream(logString.toString().getBytes()), true, null);
    			}
    			else{
    				log.appendContents(new ByteArrayInputStream(logString.toString().getBytes()),
    									true,true, null);
    			}
    		}
            /*
             * since the while loop determines the runtime of the console process
             * the process instance is not allowed to run at this point so destroy() is
             * called just in case the loop was canceled
             */
            checkProcess.destroy();
    	} catch(OutOfMemoryError e){
			state = IProperty.STATE_CANCELED;

			checkProcess.destroy();
		} catch (Exception e) {
			state = IProperty.STATE_FAILED;
			if(checkProcess != null){
				checkProcess.destroy();
			}
		}
    	if(sc != null){
    		sc.close();
    	}
    }

	/**
	 * @return the state
	 */
	public int getReturn() {
		return this.state;
	}

	/**
	 * @return the logParentResource
	 */
	public IProject getLogParentResource() {
		return this.logParentResource;
	}

	/**
	 * @param logParentResource the logParentResource to set
	 */
	public void setLogParentResource(IProject logParentResource) {
		this.logParentResource = logParentResource;
	}

}