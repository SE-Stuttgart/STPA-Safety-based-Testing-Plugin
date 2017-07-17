package xstampp.stpatcgenerator.model.stateflow.chart.generateStatesTree;

public class DataVariable {
	private String SSID;
    private String name;
    private String type;
    private String scope;
    private String method;//type of method in Simulink (double or inherit)
    private String initalvalue;
    private String maximumValue;
    private String minimumValue;
    private String value="0";

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String primative) {
        this.method = primative;
    }

    public String getInitalvalue() {
        return initalvalue;
    }

    public void setInitalvalue(String initalvalue) {
        this.initalvalue = initalvalue;
    }

    public String getMaximumValue() {
        return maximumValue;
    }

    public void setMaximumValue(String maximumValue) {
        this.maximumValue = maximumValue;
    }

    public String getMinimumValue() {
        return minimumValue;
    }

    public void setMinimumValue(String minimumValue) {
        this.minimumValue = minimumValue;
    }

    public String getSSID() {
        return SSID;
    }

    public void setSSID(String sSID) {
        SSID = sSID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public DataVariable(String sSID, String name, String type, String scope) {
        super();
        SSID = sSID;
        this.name = name;
        this.type = type;
        this.scope = scope;
    }

    @Override
    public String toString() {
        return "DataVariable{" + "SSID=" + SSID + ", name=" + name + ", type=" + type + ", scope=" + scope + ", method=" + method + ", initalvalue=" + initalvalue + ", maximumValue=" + maximumValue + ", minimumValue=" + minimumValue + '}';
    }

    public DataVariable() {
        super();
    }
}
