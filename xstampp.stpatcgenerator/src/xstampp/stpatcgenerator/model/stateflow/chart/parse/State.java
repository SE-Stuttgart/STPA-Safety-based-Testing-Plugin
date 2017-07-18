package xstampp.stpatcgenerator.model.stateflow.chart.parse;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.ui.internal.themes.WorkbenchThemeManager.Events;

import xstampp.stpatcgenerator.model.stateflow.chart.parse.Action.ActionType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)

public class State {

    @XmlAttribute(name = "SSID")
    private String sSID;
    private String name;
    private String type;
    private String ParentID;
    private Boolean isRoot;
    private List<Action> Actions = new ArrayList<Action>();

    public Children getChildren() {
        return children;
    }

    public void addEntryDuringExitAction(String t, Action.ActionType type, String name) {
        Action action = null;
        String event = t.substring(t.indexOf(":"), t.length());
        if (event != null) {
            String[] actionsStr = event.split(",");
            for (int i = 0; i < actionsStr.length; i++) {
                String str[] = actionsStr[i].split("=");
                if (str.length > 0) {
                    action = new Action();
                    action.setName(str[0].replace(":", "").replaceAll("^\\s+", "").replaceAll("\\s+$", ""));
                   
                    action.setFunction(str[1]);
                     
                    action.setSource(name);
                    action.setType(type);
                    Actions.add(action);
                    //System.out.println(action.toString());
                }

            }
        }

    }

    public void addActions(String t, String name) {
        if (this.Actions != null && t.length() > 0) {

            if (t.contains(";")) {
                String[] eventsStr = t.split(";");
                for (int i = 0; i < eventsStr.length; i++) {
                    if (eventsStr[i].contains("en:") || eventsStr[i].contains("en :")
                            || eventsStr[i].contains("entry :") || eventsStr[i].contains("entry:")) {

                        addEntryDuringExitAction(eventsStr[i], ActionType.Entry, name);
                        // combined action events of states during, entry or
                        
                        // exit, entry..;

                    } else if (eventsStr[i].contains("du:") || eventsStr[i].contains("du :")
                            || eventsStr[i].contains("during :") || eventsStr[i].contains("during:")) {

                        addEntryDuringExitAction(eventsStr[i], ActionType.During, name);

                    } else if (eventsStr[i].contains("ex:") || eventsStr[i].contains("ex :")
                            || eventsStr[i].contains("exit :") || eventsStr[i].contains("exit:")) {

                        addEntryDuringExitAction(eventsStr[i], ActionType.Exit, name);

                    }
                }
            }
        }

    }

    public List<Action> getActions() {
        return Actions;
    }

    public void setActions(List<Action> actions) {
        this.Actions = actions;
    }

    // public Children getChildren() {
    // return children;
    // }
    public void setChildren(Children children) {
        this.children = children;
    }

    private List<StateTransition> inputTransition;
    private List<StateTransition> outputTransition;

    public List<StateTransition> getInputTransition() {

        return inputTransition;
    }

    public void setInputTransition(List<StateTransition> inputTransition) {
        this.inputTransition = inputTransition;
    }

    public List<StateTransition> getOutputTransition() {
        return outputTransition;
    }

    public void setOutputTransition(List<StateTransition> outputTransition) {
        this.outputTransition = outputTransition;
    }

    @XmlElement(name = "Children")

    private Children children = new Children();

    @XmlElement(name = "P")
    private List<P> p = new ArrayList<P>();

    public void setchildren(Children childern) {
        this.children = childern;
        if (childern.getP() != null) {
            this.setHasChildern(true);
        }

    }

    public Children getchildren() {
        return children;
    }

    public void getchildren(Children child) {
        this.children = child;
    }

    public State(String sSID, String name, String type, String parentID, Boolean isRoot, Children children, List<P> p,
            boolean hasChildern) {

        this.sSID = sSID;
        this.name = name;
        this.type = type;
        ParentID = parentID;
        this.isRoot = isRoot;
        this.children = children;
        this.p = p;
        this.hasChildern = hasChildern;
    }

    public State(String sSID, Children children, List<P> p) {

        this.sSID = sSID;
        this.children = children;
        this.p = p;
    }

    public List<P> getP() {
        return p;
    }

    public void setP(List<P> p) {
        this.p = p;
    }

    public String getsSID() {
        return sSID;
    }

    public void setsSID(String sSID) {
        this.sSID = sSID;
    }

    public String getName() {
        String name = "-1";
        if (this.p != null) {
            for (P plist : p) {
                if (plist.getName().equals("labelString")) {
                    String[] n = plist.getValue().split("\n");
                    name = n[0];
                    if (plist.getValue() != null) {
                        String str = plist.getValue().substring(name.length(), plist.getValue().length());
                        addActions(str, name);
                      
                                
                    }
                }
            }
        }

        return name;

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {

        String type = "-1";
        if (this.p != null) {
            for (P plist : p) {
                if (plist.getName().equals("type")) {

                    type = plist.getValue();
                }
            }
        }

        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExecutionOrder() {
        String executionOrder = "-1";
        if (this.p != null) {
            for (P plist : p) {
                if (plist.getName().equals("executionOrder")) {
                    executionOrder = plist.getValue();
                }
            }
        }
        return executionOrder;
    }

    public String getParentID() {
        return ParentID;
    }

    public void setParentID(String parentID) {
        ParentID = parentID;
    }

    public Boolean getIsRoot() {
        return isRoot;
    }

    public void setIsRoot(Boolean isRoot) {
        this.isRoot = isRoot;
    }

    public boolean isHasChildern() {
       return hasChildern;
    }

    public void setHasChildern(boolean hasChildern) {
        this.hasChildern = hasChildern;
    }

    private boolean hasChildern=false;

    @Override
    public String toString() {
        return "State [ParentID= " + getParentID() + " sSID=" + getsSID() + ", name=" + getName() + ", type="
                + getType() + " ExecutionOrder= " + getExecutionOrder() + "]";
    }

    public State() {
        this.children = new Children();
        this.p = new ArrayList<P>();
    }

}

