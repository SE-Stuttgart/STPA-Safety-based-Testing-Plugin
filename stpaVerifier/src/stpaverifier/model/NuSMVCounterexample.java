package stpaverifier.model;

import java.util.ArrayList;
import java.util.List;


public class NuSMVCounterexample extends AbstractCounterexample {
	private final String counterexample;
	private List<List<String>> entryList;
	public NuSMVCounterexample(String counterexample,String stringID, String property) {
		super(stringID, property);
		this.counterexample = counterexample;
		entryList = new ArrayList<>();
		for(String line : counterexample.split("\n")){
			List<String> tmp= new ArrayList<String>();
			for(String entry: line.split("\t")){
				tmp.add(entry);
			}
			entryList.add(tmp);
		}
	}
	
	public String getCounterexample() {
		return this.counterexample;
	}
	
	/* (non-Javadoc)
	 * @see stpaverifier.ui.views.utils.ICounterexample#getContent()
	 */
	@Override
	public String[] getContent() {
		return counterexample.split("\\n");
	}

	@Override
	public List<String> getLines() {
		ArrayList<String> list= new ArrayList<>();
		for(String line :getContent()){
			list.add(line);
		}
		return list;
	}

	public List<List<String>> getEntryList() {
		return this.entryList;
	}
}
