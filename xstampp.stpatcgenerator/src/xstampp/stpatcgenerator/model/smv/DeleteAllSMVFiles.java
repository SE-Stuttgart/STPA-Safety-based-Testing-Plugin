package xstampp.stpatcgenerator.model.smv;

import java.io.*;

public class DeleteAllSMVFiles {
	private static final String FILE_DIR = "./";
	   private static final String FILE_TEXT_EXT = ".smv";

		
	   public void deleteFile( ){
			
	   String folder =FILE_DIR;
	    String ext=FILE_TEXT_EXT;
		 GenericExtFilter filter = new GenericExtFilter(ext);
	     File dir = new File(folder);
		
	     //list out all the file name with .txt extension
	     String[] list = dir.list(filter);
		     
	     if (list.length == 0) return;

	     File fileDelete;
		    
	     for (String file : list){
	   	String temp = new StringBuffer(FILE_DIR)
	                      .append(File.separator)
	                      .append(file).toString();
	    	fileDelete = new File(temp);
	    	boolean isdeleted = fileDelete.delete();
	    	System.out.println("file : " + temp + " is deleted : " + isdeleted);
	     }
	   }
	  
	   //inner class, generic extension filter 
	   public class GenericExtFilter implements FilenameFilter {
		
	       private String ext;
		
	       public GenericExtFilter(String ext) {
	         this.ext = ext;             
	       }
		       
	       public boolean accept(File dir, String name) {
	         return (name.endsWith(ext));
	       }
	    }
}
