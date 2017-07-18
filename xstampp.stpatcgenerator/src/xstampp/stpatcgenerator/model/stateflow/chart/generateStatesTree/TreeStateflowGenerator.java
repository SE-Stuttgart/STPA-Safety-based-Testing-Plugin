package xstampp.stpatcgenerator.model.stateflow.chart.generateStatesTree;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import xstampp.stpatcgenerator.model.stateflow.chart.parse.Data;
import xstampp.stpatcgenerator.model.stateflow.chart.parse.ModelInformation;
import xstampp.stpatcgenerator.model.stateflow.chart.parse.ParseStateflowMain;
import xstampp.stpatcgenerator.model.stateflow.chart.parse.State;
import xstampp.stpatcgenerator.model.stateflow.chart.parse.Transition;

public class TreeStateflowGenerator {

    private ModelInformation dataModelStateflow = null;
    Tree tree = new Tree();
    private ParseStateflowMain parseStateflowMain;

    public TreeStateflowGenerator(ParseStateflowMain parseStateflowMain) {
        super();
        this.parseStateflowMain = parseStateflowMain;
    }

    public static boolean isAllEqual(String[] a) {
        for (int i = 1; i < a.length; i++) {
            if (!a[0].equals(a[i])) {
                return false;
            }
        }

        return true;
    }

    public String getTypeStateDecomposition(State s) {
        String type = null;
        String[] childTypes = null;
        for (int i = 0; i < s.getchildren().getStates().size(); i++) {
            if (s.isHasChildern()) {
                childTypes[i] = s.getchildren().getStates().get(i).getType();
            }
        }
        if (isAllEqual(childTypes)) {
            type = childTypes[0];
        }
        return type;
    }

    public String getTypeDecompsition(List<State> s) {
        String type = null;
        String[] childTypes = new String[s.size()];
        for (int i = 0; i < s.size(); i++) {
            childTypes[i] = s.get(i).getType();

        }
        if (isAllEqual(childTypes)) {
            type = childTypes[0];
        }
        return type;
    }

    public String generateRootParentID() {

        boolean isfound = false;
        if (parseStateflowMain == null) {
            return "Error";
        }
        List<Integer> statesIDs = parseStateflowMain.getStatesIDs();
        int randomID = statesIDs.size() + (int) (Math.random() * (statesIDs.size() * 10));
        String parentID = Integer.toString(randomID);
        for (Integer id : statesIDs) {
            if (id == randomID) {
                isfound = true;
            }
        }

        if (isfound == false) {
            parentID = Integer.toString(randomID + 10);
        } else {
            parentID = Integer.toString(statesIDs.size() + (int) (Math.random() * (statesIDs.size() * 10)));

        }
        return parentID;
    }

    public int traverseChildren(int size, StateNode root) {
        StateNode node = null;

        if (root.isHasChildren()) {
            for (StateNode child : root.getChildren()) {

                size += 1;

                size = traverseChildren(size, child);
            }
        }
        return size;
    }

    public int getSizeOfTree(StateNode root) {
        int size = 0;
        if (root != null) {
            size += traverseChildren(size, root);
        }
        return size;
    }

    public List<Transition> removeduplicatedTransitions(StateNode node) {
        List<Transition> temp = new ArrayList<Transition>();

        boolean isduplicated = false;
        if (node.getTransitions().size() > 0) {
            for (int i = 0; i < node.getTransitions().size() - 1; i++) {
                isduplicated = false;
                for (int j = 1; j < node.getTransitions().size(); j++) {
                    if (node.getTransitions().get(i).getSSID().equals(node.getTransitions().get(j).getSSID())) {
                        isduplicated = true;
                    }
                }
                if (isduplicated == false) {
                    temp.add(node.getTransitions().get(i));
                }
            }

        }
        return temp;
    }

    public void traverseChildren(StateNode root, State s) {
        StateNode node = null;
        if (root.isHasChildren()) {

            for (State child : s.getchildren().getStates()) {
                node = new StateNode();
                node.setName(getNameOfstate(child.getName()));
                node.setRoot(false);
                node.setId(child.getsSID());
               // System.out.println("State" + child.getName() + " type=" + child.getType());
                node.setTypeDecomposition(child.getType());
                node.setParentID(s.getsSID());
                root.addALLAcionts(child.getActions());
                node.setActions(child.getActions());
                if (child.getchildren().getStates().size() > 0) {

                    node.setHasChildren(true);
                    node.setSubModule(true);
                } else {
                    node.setHasChildren(false);
                     node.setSubModule(false);
                }

                if (child.getchildren().getTransitions().size() > 0) {
                    node.setHasTransitions(true);
                    node.setTransitions(child.getchildren().getTransitions());

                    // this objects are the same functions
                    node.addAllTransitions(child.getchildren().getTransitions());

                   

                } else if (!child.isHasChildern()) {
                    node.addAllTransitionsOfSingalNode(s.getChildren().getTransitions());
                }

                // traverseChildren(node, child);
                node.setExecutionOrder(child.getExecutionOrder());
                if (!node.getTypeDecomposition().equals("FUNC_STATE")) {
                    tree.addStateInList(node);
                    root.addChild(node);

                }
                tree.setTotalOfTransitions(node.getTransitions().size() + tree.getTotalOfTransitions());

                //System.out.println("total number of transitions" + node.getName() + node.getTransitions().size());
                traverseChildren(node, child);

            }
            // return node ;
        }
    }

    public String getNameOfstate(String name) {

        StringTokenizer st = new StringTokenizer(name, "\n");
        String n = st.nextToken();

        return n;
    }

    public boolean checkIfStatehasSubModule(List<State> states) {
        boolean hasSubModule = false;
        for (State s : states) {
            if (s.isHasChildern()) {
                hasSubModule = true;
            }
        }
        return hasSubModule;
    }

    public String identifyTypeOFRoot(List<State> states) {
        int numberOR = 0;
        int numberAND = 0;
        boolean sametype = false;
        for (State s : states) {
            if (s.getType().equals("OR_STATE")) {
                numberOR++;
            } else if (s.getType().equals("AND_STATE")) {
                numberAND++;
            }

        }
        if (numberOR == states.size()) {
            return "OR_STATE";
        } else if (numberAND == states.size()) {
            return "AND_STATE";
        } else {
            return "OR_STATE";
        }

    }

    public Tree generateTree(ModelInformation dataStateflowModel) {

        this.dataModelStateflow = dataStateflowModel;
        // Tree tree = new Tree();
        StateNode root = new StateNode();
        StateNode node;
        String type = "OR_STATE";
        int sizeOfTree = 0;
        // creat root node;
        try {

            root.setRoot(true);
            root.setName("root");

            List<State> states = dataStateflowModel.getStateflow().getMachine().getChildrenchart().getChart().getRoot()
                    .getStates();
            
             
            
            List<Data> datavariables = dataStateflowModel.getStateflow().getMachine().getChildrenchart().getChart().getRoot().getData();
            List<Transition> rootTransitions = dataStateflowModel.getStateflow().getMachine().getChildrenchart()
                    .getChart().getRoot().getTransitions();

            // in case that the chart has only one state in the root
            if (states.size()== 1) {
                root.setName( states.get(0).getName() );
                root.setRoot(true);
                root.setParentID(null);
                
                root.setId(states.get(0).getsSID());
                root.setActions(states.get(0).getActions());
                root.setTypeDecomposition(states.get(0).getType());
                root.addALLAcionts(states.get(0).getActions());
                rootTransitions = states.get(0).getChildren().getTransitions();
                root.setTransitions(rootTransitions);
                states = states.get(0).getChildren().getStates();

            }else {
            root.setId(generateRootParentID());
            }
            // parse the sub-states at level 0
            if (states.size() > 0) {
                root.setHasChildren(true);
            } else {
                root.setHasChildren(false);
            }

            for (State s : states) {

                node = new StateNode();
                node.setName(getNameOfstate(s.getName()));
                node.setRoot(false);
                node.setParentID(root.getId());
                node.setId(s.getsSID());
                node.setHasChildren(false);
                node.setActions(s.getActions());
                node.setTypeDecomposition(s.getType());
                root.addALLAcionts(s.getActions());
                if (s.getChildren().getStates().size() > 0) {
                    s.setHasChildern(true);
                    node.setHasChildren(true);
                    node.setSubModule(true);
                    
                } else {
                    s.setHasChildern(false);
                    node.setHasChildren(false);
                    node.setSubModule(false);
                }

                if (s.isHasChildern()) {

                    node.setHasChildren(true);
                    if (s.getchildren().getTransitions().size() > 0) {

                        node.setTransitions(s.getchildren().getTransitions());

                        node.addAllTransitions(s.getchildren().getTransitions());
                        node.setHasTransitions(true);
                        node.setSubModule(true);

                    }

                    tree.addStateInList(node);
                    traverseChildren(node, s);

                } else if (!s.isHasChildern()) {

                    tree.addStateInList(node);
                    node.addAllTransitionsOfSingalNode(rootTransitions);
                }

                node.setExecutionOrder(s.getExecutionOrder());
                if (!node.getTypeDecomposition().equals("FUNC_STATE")) {

                    root.addChild(node);
                }

            }

            if (rootTransitions.size() > 0) {
                root.setHasTransitions(true);

            }

            root.setTransitions(rootTransitions);
            root.addAllTransitions(rootTransitions);

            root.setTypeDecomposition(identifyTypeOFRoot(states));

            root.setSubModule(checkIfStatehasSubModule(states));

            // tree.addStateInList(root);
            tree.setTotalOfTransitions(root.getTransitions().size() + tree.getTotalOfTransitions());
            tree.setRoot(root);

            tree.addAllDataVariables(datavariables);
            tree.addAllActionStates(root);
            tree.setSize(getSizeOfTree(root));

            //System.out.println("Print Tree");
             
              
            
          //  tree.printTruthTable(tree.getTruthTable());
             
        } catch (NullPointerException e) {
            tree = null;
        }

        return tree;
    }
}
