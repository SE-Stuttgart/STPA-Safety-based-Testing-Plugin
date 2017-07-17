package stpaverifier.model;

import java.util.ArrayList;
import java.util.List;

public class SpinTrail extends AbstractCounterexample{

	public final List<String> filter = new ArrayList<String>(){
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			this.add("bit");
			
		}
	};
	private ArrayList<String> content;
	private ArrayList<String> states;
	private ArrayList<String> variables;
	private ArrayList<String> localVars;
	private String trailEnd;
	
	public SpinTrail(String counterexample,String stringID, String property) {
		super(stringID, property);
		String[] buffer = counterexample.split("\n");
		content = new ArrayList<>();
		states = new ArrayList<>();
		variables = new ArrayList<>();
		localVars = new ArrayList<>();
		ArrayList<String> currentList = states;
		for (int i = 0; i < buffer.length; i++) {
			content.add(buffer[i].replaceAll("\t", "   "));
			if(buffer[i].contains("global vars")){
				currentList = variables;
			}else if(buffer[i].contains("local vars")){
				currentList = localVars;
			}else{
				if(!buffer[i].replaceAll("\t|\n|\r","").matches("bit.*|.*bit.*")){
					currentList.add(buffer[i].replaceAll("\t", "   "));
				}
				if(buffer[i].contains("trail ends")){
					trailEnd = buffer[i];
				}	
			}
		}
	}

	@Override
	public List<String> getLines() {
		return this.content;
	}
	public String[] getContent() {
		return this.content.toArray(new String[0]);
	}

	public ArrayList<String> getStates() {
		return this.states;
	}
	
	public ArrayList<String> getVariables() {
		return this.variables;
	}
	public ArrayList<String> getLocalVars() {
		return this.localVars;
	}
	public String getTrailEnd() {
		return this.trailEnd;
	}
	@Override
	public String getCounterexample() {
		String co= new String();
		for(String line: this.content){
			co = co.concat(line + System.lineSeparator());
		}
		return co;
	}
	


	@Override
	public String getProperty() {
		// TODO Auto-generated method stub
		return null;
	}
}
