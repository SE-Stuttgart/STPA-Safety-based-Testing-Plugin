package xstampp.stpatcgenerator.model.stateflow.chart.parse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Action {
	
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isIsVisted() {
        return isVisted;
    }

    public void setIsVisted(boolean isVisted) {
        this.isVisted = isVisted;
    }
    
    public enum ActionType {
		Entry(1), During(2), Exit(3);

		private int value;

		private ActionType(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

	}
 	@Override
	public String toString() {
		return "Action [name=" + name + ", function=" + function + ", source=" + getSource()   + ", type=" + type + "]";
	}

	private String name;
	private String function; // +1;
	private String source;
	private ActionType type;
   private boolean isVisted;
	public ActionType getType() {
		return type;
	}

	public Action() {
		super();
		
		this.isVisted=false;
	}

	public boolean isVisted() {
		return isVisted;
	}

	public void setVisted(boolean isVisted) {
		this.isVisted = isVisted;
	}

	public void setType(ActionType type) {
		this.type = type;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	 
	
	 
}