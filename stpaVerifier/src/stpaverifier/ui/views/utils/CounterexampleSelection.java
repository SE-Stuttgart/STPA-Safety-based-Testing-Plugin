package stpaverifier.ui.views.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredSelection;

import stpaverifier.model.ICounterexample;

public class CounterexampleSelection implements IStructuredSelection {
	private List<ICounterexample> exampleList;
	public CounterexampleSelection() {
		exampleList = new ArrayList<>();
	}

	public boolean addExample(ICounterexample example){
		if(example != null){
			return exampleList.add(example);
		}
		return false;
	}
	
	public List<ICounterexample> getExampleList() {
		return this.exampleList;
	}
	@Override
	public boolean isEmpty() {
		return exampleList.isEmpty();
	}

	@Override
	public Object getFirstElement() {
		if(!exampleList.isEmpty()){
			return exampleList.get(0);
		}
		return null;
	}

	@Override
	public Iterator<ICounterexample> iterator() {
		return exampleList.iterator();
	}

	@Override
	public int size() {
		return exampleList.size();
	}

	@Override
	public Object[] toArray() {
		return exampleList.toArray();
	}

	@Override
	public List<ICounterexample> toList() {
		return exampleList;
	}

}
