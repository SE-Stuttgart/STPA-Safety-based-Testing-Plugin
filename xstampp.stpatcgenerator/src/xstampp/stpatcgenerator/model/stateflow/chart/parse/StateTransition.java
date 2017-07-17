package xstampp.stpatcgenerator.model.stateflow.chart.parse;


public class StateTransition {

    private String SSID;

    public String getSSID() {
        return SSID;
    }

    public void setSSID(String sSID) {
        SSID = sSID;
    }

    private String eventInput;
    private String condition;
    private String transition_Action;
    private String fullTransition; // eventInput [condition]/transition_Action
    private String source;
    private String destination;
    private boolean isDefault = false;

    public String getEventInput() {
        return eventInput;
    }

    public void setEventInput(String eventInput) {
        this.eventInput = eventInput;
    }

    public String getFullTransition() {
        return fullTransition;
    }

    public void setFullTransition(String fullTransition) {
        this.fullTransition = fullTransition;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void checkIsDefaultTransition() {
        if (this.getSource() == null) {
            this.setDefault(true);
            
        }
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getEvent() {
        if (eventInput.equals("")) {
            eventInput = null;
        }
        return eventInput;
    }

    public void setEvent(String event) {
        this.eventInput = event;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getTransition_Action() {
        return transition_Action;
    }

    public void setTransition_Action(String transition_Action) {
        this.transition_Action = transition_Action;
    }

    public void setTransition(String transition) {
        extractInfoTransition(transition);
        this.fullTransition = transition;
    }

    @Override
    public String toString() {
        return "StateTransition [SSID=" + SSID + ", source=" + source + ", eventInput=" + eventInput + ", condition="
                + condition + ", transition_Action=" + transition_Action + ", fullTransition=" + fullTransition
                + ", destination=" + destination + "]";
    }

    public String getTransition() {
        return eventInput + " [" + condition + "]" + "/" + transition_Action;
    }

    public void extractInfoTransition(String transition) {

        try {
            if (transition != null) {
                if (transition.contains("[")) {
                    String event = transition.substring(0, transition.indexOf("["));
                    if (event.length() > 0) {
                        setEvent(event);
                    } else {
                        setEvent(null);
                    }
                } else {
                    setEvent(null);
                }

                if (transition.contains("/")) {
                    String action = transition.substring(transition.indexOf("[") + 1,
                            transition.length() - transition.indexOf("["));
                    if (action.length() > 0) {
                        setTransition_Action(action);
                    } else {
                        setTransition_Action(null);
                    }

                } else {
                    setTransition_Action(null);
                }

                if (transition.contains("[") && transition.contains("]")) {
                    String condition = transition.substring(transition.indexOf("[") + 1, transition.indexOf("]"));
                    if (condition.length() > 0) {
                        setCondition(condition);
                    } else {
                        setCondition(null);
                    }
                } else {
                    setCondition(null);
                   // Logfile.wirteToLog("Could not parse the transition name" + transition);
                }
            }
        } catch (java.lang.StringIndexOutOfBoundsException e) {
            //Logfile.wirteToLog("Could not parse the transition name" + transition);
            //Logfile.wirteToLog(e.getMessage());
        }
    }
}

