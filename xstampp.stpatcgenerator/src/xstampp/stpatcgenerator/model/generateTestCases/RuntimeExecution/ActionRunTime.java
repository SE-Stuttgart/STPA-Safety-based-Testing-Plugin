package xstampp.stpatcgenerator.model.generateTestCases.RuntimeExecution;

public class ActionRunTime {
	//an action of the edge. The action is javascript code that will execute when the edge is executed.
    private String action;
    
    private String script;
    
    private String result;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
