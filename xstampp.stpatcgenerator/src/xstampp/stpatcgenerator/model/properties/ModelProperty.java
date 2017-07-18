package xstampp.stpatcgenerator.model.properties;

import java.util.List;
import java.util.UUID;

import xstampp.stpatcgenerator.controller.IProperty;
//import xstampp.stpatcgenerator.model.AbstractCounterexample;

public class ModelProperty implements IProperty {
	public static final String NULL_SID="ModelCheckingProperty"; 
	public static final UUID NULL_UUID = UUID.randomUUID();
	@Override
	public String getsFormular(boolean useSpin, boolean forExport) {
		return NULL_SID;
	}

	@Override
	public String getsID() {
		return NULL_SID;
	}

	@Override
	public int getState() {
		return 0;
	}

	@Override
	public UUID getUUID() {
		return NULL_UUID;
	}

	@Override
	public boolean useProperty() {
		return true;
	}

	@Override
	public boolean isLocked() {
		return true;
	}

	@Override
	public int compareTo(IProperty o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumber() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setMessage(String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getMessage() {
		return "";
	}

//	@Override
//	public void setCounterexample(AbstractCounterexample example) {
//		// TODO Auto-generated method stub
//		
//	}

//	@Override
//	public AbstractCounterexample getCounterexample() {
//		return new AbstractCounterexample("", "") {
//			
//			@Override
//			public String[] getContent() {
//				return new String[0];
//			}
//
//			@Override
//			public String getStringID() {
//				return new String();
//			}
//
//			@Override
//			public String getProperty() {
//				return new String();
//			}
//
//			@Override
//			public String getCounterexample() {
//				// TODO Auto-generated method stub
//				return null;
//			}
//
//			@Override
//			public List<String> getLines() {
//				// TODO Auto-generated method stub
//				return null;
//			}
//		};
//	}

}
