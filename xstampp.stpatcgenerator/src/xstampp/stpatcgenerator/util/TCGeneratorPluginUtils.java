package xstampp.stpatcgenerator.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.services.IServiceLocator;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;

import xstampp.stpatcgenerator.ui.editors.SMVTextEditor;
import xstampp.stpatcgenerator.ui.editors.TCGeneratorEditorInput;
import xstampp.ui.common.ProjectManager;

/**
 * this class provides useful static methods for the interaction with the plugin
 * 
 * @author Ting Luk-He
 * @since version 1.0.0
 * 
 */
public class TCGeneratorPluginUtils {
	private static final String workspacePath = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
	public static final String STATEFLOW_LOCATION_FILE = workspacePath + "/StateFlowFileLocation.txt";
	public static final String NUSMV_LOCATION_FILE = workspacePath + "/NuSMVLocation.txt";
	
	private static MessageConsole myConsole = findConsole("Console");
	private static MessageConsoleStream OUT = myConsole.newMessageStream();
	private static MessageConsoleStream ERROR = myConsole.newMessageStream();
	private static MessageConsoleStream WARNING = myConsole.newMessageStream();
	private static MessageConsoleStream INFO = myConsole.newMessageStream();
	// map to save the editor input
	private static Map<String, IEditorInput> inputMap = new HashMap<String, IEditorInput>();
	private static Map<String, IFile> smvInput = new HashMap<String, IFile>();

	public static MessageConsoleStream getOutputStream(){
		OUT.setColor(new Color(null, 0, 0, 0));
		return OUT;
	}
	
	public static MessageConsoleStream getErrorOS(){
		ERROR.setColor(new Color(null, 255, 0, 0));
		return ERROR;
	}
	
	public static MessageConsoleStream getWarningOS(){
		WARNING.setColor(new Color(null, 255, 204, 0));
		return WARNING;
	}
	
	public static MessageConsoleStream getInfoOS(){
		INFO.setColor(new Color(null, 0, 0, 255));
		return INFO;
	}
	
	
	public static Map<String, IEditorInput> getInputMap() {
		return inputMap;
	}

	public static void setInputMap(Map<String, IEditorInput> inputMap) {
		TCGeneratorPluginUtils.inputMap = inputMap;
	}
	
	
	public static Map<String, IFile> getSmvInput() {
		return smvInput;
	}

	public static void setSmvInput(Map<String, IFile> smvInput) {
		TCGeneratorPluginUtils.smvInput = smvInput;
	}

	public static String getCurrentTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		return "[" + dateFormat.format(cal.getTime()) + "]";
	}
	public static void showErrorDialog(String title, String message){
		Shell shell =  PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		MessageDialog.openError(shell, title, message);
	}
	public static void showWarningDialog(String title, String message){
		Shell shell =  PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		MessageDialog.openWarning(shell, title, message);
	}
	public static void showInfoDialog(String title, String message){
		Shell shell =  PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		MessageDialog.openInformation(shell, title, message);
	}
	
	public static DefaultTableModel saveTableContent(DefaultTableModel model){
		DefaultTableModel tModel = new DefaultTableModel();
    	String[] columnNames = new String[model.getColumnCount()];
    	for(int j = 0; j < model.getColumnCount(); j++){
    		columnNames[j] = model.getColumnName(j);
    	}
    	tModel.setColumnIdentifiers(columnNames);
    	if (model.getRowCount() > 0) {
		    for (int i = 0; i < model.getRowCount(); i++) {
		    	String[] rowData = new String[model.getColumnCount()];
		    	for(int j = 0; j < model.getColumnCount(); j++){
		    		rowData[j] = (String) model.getValueAt(i, j);
		    	}
		    	tModel.insertRow(i, rowData);
		    }
		}
    	return tModel;
	}
	
    public static void deleteTableContent(DefaultTableModel model){    	
    	if (model.getRowCount() > 0) {
		    for (int i = model.getRowCount() - 1; i > -1; i--) {
		    	model.removeRow(i);
		    }
		}
    }

	public static void clearGraph( Graph graph )
	{       
	    Object[] objects = graph.getConnections().toArray() ;           
	    for (int i = 0 ; i < objects.length; i++)
	    {
	        GraphConnection graCon = (GraphConnection) objects[i];
	        if(!graCon.isDisposed())
	            graCon.dispose();
	    }            

	    objects = graph.getNodes().toArray();       
	    for (int i = 0; i < objects.length; i++)
	    {
	        GraphNode graNode = (GraphNode) objects[i];
	        if(!graNode.isDisposed())
	            graNode.dispose();
	    }
	}
//	private static List<Job> unfinishedJobs;
//	private TCGeneratorPluginUtils() {
//		unfinishedJobs = new ArrayList<>();
//	}
//	
//	public static void listJob(Job job){
//		if(unfinishedJobs == null){
//			unfinishedJobs = new ArrayList<>();
//		}
//		unfinishedJobs.add(job);
//		job.addJobChangeListener(new JobChangeAdapter() {
//			
//			@Override
//			public void done(IJobChangeEvent event) {
//				unfinishedJobs.remove(event.getJob());
//			}
//		});
//	}
//	
//	public static List<Job> getUnfinishedJobs(){
//		if(unfinishedJobs == null){
//			return new ArrayList<>();
//		}
//		return unfinishedJobs;
//	}
	
	/**
	 * Executes a registered command without command values
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param commandId
	 *            the id under which the command is registered in the plugin
	 * @return the command return value or null if non/ the command was not
	 *         executed
	 */
	public static Object executeCommand(String commandId) {
		if(commandId == null){
			return false;
		}
		IServiceLocator serviceLocator = PlatformUI.getWorkbench();
		ICommandService commandService = (ICommandService) serviceLocator
				.getService(ICommandService.class);
		Command command = commandService.getCommand(commandId);
		if (command != null) {
			try {
				return command.executeWithChecks(new ExecutionEvent());
			} catch (ExecutionException | NotDefinedException|NullPointerException
					| NotEnabledException | NotHandledException e) {
				ProjectManager.getLOGGER().error(
						"Command " + commandId + " does not exist"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		} else {
			ProjectManager.getLOGGER().error(
					"Command " + commandId + " does not exist"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return null;
	}

	/**
	 * Executes a registered command with command values
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param commandId
	 *            the id under which the command is registered in the plugin
	 * @param params
	 *            a map containing values like
	 *            <code> 'ParameterName,value' </code>
	 * @return the command return value or null if non/ the command was not
	 *         executed
	 */
	public static Object executeParaCommand(String commandId,
			Map<String, String> params) {
		IServiceLocator serviceLocator = PlatformUI.getWorkbench();
		ICommandService commandService = (ICommandService) serviceLocator
				.getService(ICommandService.class);
		IHandlerService handlerService = (IHandlerService) serviceLocator
				.getService(IHandlerService.class);
		Command command = commandService.getCommand(commandId);
		if (command == null) {
			ProjectManager.getLOGGER().debug(commandId + " is no valid command id");
			return false;
		}
		ParameterizedCommand paraCommand = ParameterizedCommand
				.generateCommand(command, params);
		if(paraCommand == null){
			ProjectManager.getLOGGER().debug("One of: "+params.toString()+ " is no valid parameter id");
			return false;
		}
		try {
			return handlerService.executeCommand(paraCommand, null);

		} catch (ExecutionException | NotDefinedException
				| NotEnabledException | NotHandledException e) {
			Logger.getRootLogger().error(
					"Command " + commandId + " does not exist"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		return true;
	}
	
	private static MessageConsole findConsole(String name) {
	      ConsolePlugin plugin = ConsolePlugin.getDefault();
	      IConsoleManager conMan = plugin.getConsoleManager();
	      IConsole[] existing = conMan.getConsoles();
	      for (int i = 0; i < existing.length; i++){
	    	  if (name.equals(existing[i].getName()))
		            return (MessageConsole) existing[i];
	      }	         
	      //no console found, so create a new one
	      MessageConsole myConsole = new MessageConsole(name, null);
	      conMan.addConsoles(new IConsole[]{myConsole});
	      return myConsole;
	}
	
	public static void hideView(String viewId) {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
			    .getActivePage();	
		try{
			if(page != null){
				IViewPart part = page.findView(viewId);
				if(part != null){
					page.hideView(part);
					System.out.println("Hide View: " + viewId);
				}			
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		
			
			
	}
	
	public static void showView(String viewId) {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
			    .getActivePage();
		if(page != null)
			try {
				page.showView(viewId);
				System.out.println("Show View: " + viewId);
			} catch (PartInitException e) {
				e.printStackTrace();
			}
	}

	public static void openEditor(ExecutionEvent event, String editorId, TCGeneratorEditorInput input) {
		// get the page
        IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
        IWorkbenchPage page = window.getActivePage();
        try {
			page.openEditor(input, editorId);
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Overwrite the file location in relevant log file
	 * @param filePath
	 * @param location
	 */
	public static void writeLocationToFile(String filePath, String location){

		File myFoo = new File(filePath);
		FileWriter fooWriter;
		try {
			fooWriter = new FileWriter(myFoo, false);
			fooWriter.write(location);
			fooWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 	 		
	}
	/**
	 * read the last called file path from log file
	 * @param filePath
	 * @return
	 */
	public static String readLocationFromFile(String filePath){
		FileReader fr;
		String s = "";
		try {
			fr = new FileReader(filePath);
			if (fr != null){
				BufferedReader br = new BufferedReader(fr); 
				s = br.readLine(); 			
				fr.close(); 
			}			
		} catch (FileNotFoundException e) {
			new File(filePath);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		return s;
		
	}

}
