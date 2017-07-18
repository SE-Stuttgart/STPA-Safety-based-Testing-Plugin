package xstampp.stpatcgenerator.model.generateTestCases;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

import xstampp.stpatcgenerator.model.generateTestCases.RuntimeExecution.Execution;
import xstampp.stpatcgenerator.model.stateflow.chart.generateStatesTree.DataVariable;
import xstampp.stpatcgenerator.model.stateflow.chart.parse.Action;
import xstampp.stpatcgenerator.model.stateflow.chart.parse.StateTransition;

public class TestCasesGenerator {
	private Stack<Integer> path = new Stack<Integer>();   // the current path
    private Set<Integer> onPath = new HashSet<Integer>();
    private Stack<String> pathVertices = new Stack<String>();
    private String paths = "";
    private boolean oneTestSuit = true;
    private Set<TestSuite> testSuites = new HashSet<TestSuite>();
    private TestGraph graph;
    private TestConfigurations testconfig;
    private Set<Edge> visitedTransitions = new HashSet<Edge>();
    private Set<Vertex> visitedStates = new HashSet<Vertex>();
    private Set<Edge> conditonsTrue = new HashSet<Edge>();
    private Set<String> visitedTraceabilityMatrixs = new HashSet<String>();
    private String frequencySSR = "";
    private Execution exectuion = new Execution();
    private int testSteps =0;
    private int step = 0;
    public void initalTest() {
        exectuion.setDataTestModel(testconfig.getDataTestModel());

        testSuites = new HashSet<TestSuite>();
        visitedStates = new HashSet<Vertex>();
        visitedTransitions = new HashSet<Edge>();
        exectuion.initialVariables();
        testconfig.getStopcondition().setValue(0.0);
       
        unvisitedEdges(graph);
        unvisitNodes(graph);
        //  exectuion.generateRandomAllVaraibles();

    }

    public boolean isOneTestSuit() {
		return oneTestSuit;
	}

	public void setOneTestSuit(boolean oneTestSuit) {
		this.oneTestSuit = oneTestSuit;
	}

	public void RandomWalkByBFS() {
        int max = getMaxIndex();
        int min = getMinIndex();
//        int step = 0;
        initalTest();
        
        if (oneTestSuit) {
        	TestSuite ts = createTestSuite();
        	while (step < testconfig.getStepsOfTest()) {

                if (testconfig.getStopcondition().getValue() < 100.0) {
                    //random walk based on the BFS 
                    generateTestCases(graph, graph.getVertexByIndex(generateRandomSteps(max, min)), ts);
                    if (ts.getTestCase().size() >= 1) {
                        this.testSuites.add(ts);
                    }

                } else if (testconfig.getStopcondition().getValue() == 100.0) {
                    calCoverage();
                    break;
                }

                calCoverage();
                unvisitedEdges(graph);
                unvisitNodes(graph);
                step++;
            }
        } else {       	
        	while (step < testconfig.getStepsOfTest()) {
        		TestSuite ts = createTestSuite();
                if (testconfig.getStopcondition().getValue() < 100.0) {
                    //random walk based on the BFS 
                    generateTestCases(graph, graph.getVertexByIndex(generateRandomSteps(max, min)), ts);
                    if (ts.getTestCase().size() >= 1) {
                        this.testSuites.add(ts);
                    }

                } else if (testconfig.getStopcondition().getValue() == 100.0) {
                    calCoverage();
                    break;
                }

                calCoverage();
                unvisitedEdges(graph);
                unvisitNodes(graph);
                step++;
            }
        }
        
    }

    public void RandomWalkByDFS() {
        int max = getMaxIndex();
        int min = getMinIndex();
//        int step = 0;
        initalTest();
        if (oneTestSuit){
        	TestSuite ts = createTestSuite();
        	while (step < testconfig.getStepsOfTest()) {

                int stateFrom = generateRandomSteps(max, min);
                int stateTo = generateRandomSteps(max, stateFrom);
                if (stateFrom == stateTo) {
                    stateFrom = generateRandomSteps(max, min);
                    stateTo = generateRandomSteps(max, stateFrom);

                }
                
                //random walk based on the DFS 

                if (testconfig.getStopcondition().getValue() < 100.0) {
                    //random walk based on the DFS 

                    generateTestCases(graph, stateFrom, stateTo, ts);
                    //  unvisitedEdges(graph);
                    // unvisitNodes(graph);

                    if (ts.getTestCase().size() >= 1) {
                        this.testSuites.add(ts);
                    }

                } else if (testconfig.getStopcondition().getValue() == 100.0) {
                    calCoverage();
                    break;
                }

                calCoverage();

                step++;

            }
        } else {
        	
        	while (step < testconfig.getStepsOfTest()) {

                int stateFrom = generateRandomSteps(max, min);
                int stateTo = generateRandomSteps(max, stateFrom);
                if (stateFrom == stateTo) {
                    stateFrom = generateRandomSteps(max, min);
                    stateTo = generateRandomSteps(max, stateFrom);

                }
                TestSuite ts = createTestSuite();
                //random walk based on the DFS 

                if (testconfig.getStopcondition().getValue() < 100.0) {
                    //random walk based on the DFS 

                    generateTestCases(graph, stateFrom, stateTo, ts);
                    //  unvisitedEdges(graph);
                    // unvisitNodes(graph);

                    if (ts.getTestCase().size() >= 1) {
                        this.testSuites.add(ts);
                    }

                } else if (testconfig.getStopcondition().getValue() == 100.0) {
                    calCoverage();
                    break;
                }

                calCoverage();

                step++;

            }
        }
        
    }

    public void RandomWalk() {
        int max = getMaxIndex();
        int min = getMinIndex();
//        int step = 0;
        testSteps=0;
        initalTest();
        if (oneTestSuit) {
        	TestSuite ts = createTestSuite();
        	while (step < testconfig.getStepsOfTest()) {

                int stateFrom = generateRandomSteps(max, min);
                int stateTo = generateRandomSteps(max, stateFrom);

                if (stateFrom > stateTo) {
                    int temp = stateFrom;
                    stateFrom = stateTo;
                    stateTo = temp;
                }
                
                //random walk based on the DFS 

                if (testconfig.getStopcondition().getValue() < 100.0) {
                    //random walk based on the DFS 
                    exectuion.generateRandomVaraibles();
                    generateTestCases(graph, stateFrom, stateTo, ts);
                    //unvisitedEdges(graph);
                    //unvisitNodes(graph);
                    if (ts.getTestCase().size() >= 1) {
                        this.testSuites.add(ts);
                    }

//                    TestSuite ts1 = createTestSuite();
//
//                    generateTestCases(graph, graph.getVertexByIndex(stateFrom), ts1);
//                    if (ts1.getTestCase().size() >= 1) {
//                        this.testSuites.add(ts1);
//                    }
                    generateTestCases(graph, graph.getVertexByIndex(stateFrom), ts);

                } else if (testconfig.getStopcondition().getValue() == 100.0) {
                    calCoverage();
                    break;
                }

                calCoverage();
                //unvisitedEdges(graph);
                //unvisitNodes(graph);
                step++;

            }
        } else {
        	
        	while (step < testconfig.getStepsOfTest()) {

                int stateFrom = generateRandomSteps(max, min);
                int stateTo = generateRandomSteps(max, stateFrom);

                if (stateFrom > stateTo) {
                    int temp = stateFrom;
                    stateFrom = stateTo;
                    stateTo = temp;
                }
                TestSuite ts = createTestSuite();
                //random walk based on the DFS 

                if (testconfig.getStopcondition().getValue() < 100.0) {
                    //random walk based on the DFS 
                    exectuion.generateRandomVaraibles();
                    generateTestCases(graph, stateFrom, stateTo, ts);
                    //unvisitedEdges(graph);
                    //unvisitNodes(graph);
                    if (ts.getTestCase().size() >= 1) {
                        this.testSuites.add(ts);
                    }

                    TestSuite ts1 = createTestSuite();

                    generateTestCases(graph, graph.getVertexByIndex(stateFrom), ts1);
                    if (ts1.getTestCase().size() >= 1) {
                        this.testSuites.add(ts1);
                    }

                } else if (testconfig.getStopcondition().getValue() == 100.0) {
                    calCoverage();
                    break;
                }

                calCoverage();
                //unvisitedEdges(graph);
                //unvisitNodes(graph);
                step++;

            }
        }
        
    }

    public void unvisitedEdges(TestGraph G) {
        for (Edge e : G.getEdges()) {
            e.setVisited(false);
        }
    }

    public int getMaxIndex() {
        int max = graph.getVertices().get(0).getIndex();
        for (Vertex v : graph.getVertices()) {

            if (v.getIndex() > max) {
                max = v.getIndex();
            }

        }
        return max;
    }

    public int getTestSuiteSize() {
        return this.testSuites.size();
    }

    public int getTotalTestCases() {
        int total = 0;
        for (TestSuite t : this.testSuites) {
            for (TestCase tc : t.getTestCase()) {
                total += 1;
            }
        }
        return total;
    }

    public int getMinIndex() {
        int min = graph.getVertices().get(0).getIndex();
        for (Vertex v : graph.getVertices()) {

            if (v.getIndex() < min) {
                min = v.getIndex();
            }

        }
        return min;
    }

    public double getStopCoverage() {
        return testconfig.getStopcondition().getValue();
    }

    public int getSizeOFTransitionSTPASSR() {
        Set<String> SSR = new HashSet<>();
        for (TraceabilityMatrix tm : testconfig.getTraceabilitymatrix()) {
            SSR.add(tm.getSTPA_SSRID());
        }
        return SSR.size();
    }

    public void calCoverage() {
        DecimalFormat df = new DecimalFormat("####0.00");
        if (this.visitedStates.size() > 0) {

            double coverageState = ((double) visitedStates.size() / graph.getVertices().size()) * 100;
            double coverageTransition = ((double) visitedTransitions.size() / graph.getEdges().size()) * 100;
            // double coverageSTPASRR = ((double) visitedTraceabilityMatrixs.size() / testconfig.getTraceabilitymatrix().size()) * 100;
            double coverageSTPASRR = ((double) visitedTraceabilityMatrixs.size() / getSizeOFTransitionSTPASSR()) * 100;
            for (CoverageCriteria c : this.testconfig.getCoverages()) {
                if (c.getType() == CoverageCriteria.CoverageType.Statescoverage) {
                    c.setValue(Double.valueOf(df.format(coverageState)));
                    c.setNumerator(visitedStates.size());
                    c.setDenominator(graph.getVertices().size());
                } else if (c.getType() == CoverageCriteria.CoverageType.TransitionsCoverage) {
                    c.setValue(Double.valueOf(df.format(coverageTransition)));
                    c.setNumerator(visitedTransitions.size());
                    c.setDenominator(graph.getEdges().size());
                } else if (c.getType() == CoverageCriteria.CoverageType.ALLSTPARequirmentsCoverage && visitedTraceabilityMatrixs.size() > 0) {

                    c.setValue(Double.valueOf(df.format(coverageSTPASRR)));
                    c.setNumerator(visitedTraceabilityMatrixs.size());
                    c.setDenominator(getSizeOFTransitionSTPASSR());
                }
            }

        }
    }

    public Set<Edge> getConditonsTrue() {
        return conditonsTrue;
    }

    public void setConditonsTrue(Set<Edge> conditonsTrue) {
        this.conditonsTrue = conditonsTrue;
    }

    public Set<String> getVisitedTraceabilityMatrixs() {
        return visitedTraceabilityMatrixs;
    }

    public void setVisitedTraceabilityMatrixs(Set<String> visitedTraceabilityMatrixs) {
        this.visitedTraceabilityMatrixs = visitedTraceabilityMatrixs;
    }

    public String getFrequencySSR() {
        return frequencySSR;
    }

    public void setFrequencySSR(String frequencySSR) {
        this.frequencySSR = frequencySSR;
    }

    public Set<String> getTraceabilityMatrixs() {
        return visitedTraceabilityMatrixs;
    }

    public void setTraceabilityMatrixs(Set<String> traceabilityMatrixs) {
        this.visitedTraceabilityMatrixs = traceabilityMatrixs;
    }

    public Execution getExectuion() {
        return exectuion;
    }

    public void setExectuion(Execution exectuion) {
        this.exectuion = exectuion;
    }

    public int generateRandomSteps(int max, int min) {
        Random rand = new Random(System.nanoTime());

        int randomNum = rand.nextInt(((max - min) + 1) + min);

        return randomNum;
    }

    public TestCasesGenerator(TestGraph graph, TestConfigurations testconfig) {
        this.graph = graph;
        this.testconfig = testconfig;

    }

    public TestGraph getGraph() {
        return graph;
    }

    public void setGraph(TestGraph graph) {
        this.graph = graph;
    }

    public TestConfigurations getTestconfig() {
        return testconfig;
    }

    public void setTestconfig(TestConfigurations testconfig) {
        this.testconfig = testconfig;
    }

    public Set<Edge> getVisitedTransitions() {
        return visitedTransitions;
    }

    public void setVisitedTransitions(Set<Edge> visitedTransitions) {
        this.visitedTransitions = visitedTransitions;
    }

    public Set<Vertex> getVisitedStates() {
        return visitedStates;
    }

    public void setVisitedStates(Set<Vertex> visitedStates) {
        this.visitedStates = visitedStates;
    }

    public Stack<Integer> getPath() {
        return path;
    }

    public void setPath(Stack<Integer> path) {
        this.path = path;
    }

    public Set<Integer> getOnPath() {
        return onPath;
    }

    public void setOnPath(Set<Integer> onPath) {
        this.onPath = onPath;
    }

    public Stack<String> getPathVertices() {
        return pathVertices;
    }

    public void setPathVertices(Stack<String> pathVertices) {
        this.pathVertices = pathVertices;
    }

    public String getPaths() {
        return paths;
    }

    public void setPaths(String paths) {
        this.paths = paths;
    }

    public Set<TestSuite> getTestSuites() {
        return testSuites;
    }

    public void setTestSuites(Set<TestSuite> testSuites) {
        this.testSuites = testSuites;
    }

    public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public TestGraph getG() {
        return g;
    }

    public void setG(TestGraph g) {
        this.g = g;
    }
    private TestGraph g;

    public void setTestGraph(TestGraph g) {
        this.g = g;
    }

    public boolean isAction(Vertex from, String var) {
        boolean isaction = false;

        for (Action ac : from.getData().getActions()) {
            if (ac.getName().equals(var)) {
                isaction = true;
                break;
            }
        }
        return isaction;
    }

    public String getScopeOfAction(String action) {
        String str = "";
        for (DataVariable var : exectuion.getDataTestModel()) {
            if (var.getName().equals(action)) {
                str = var.getScope();
                break;
            }
        }
        return str;
    }

    public Map<String, Integer> getFrequncyofSTPASSR(String s) {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        String[] SSR = s.split(",");
        for (int i = 0; i < SSR.length; i++) {
            String c = SSR[i];
            if (c != null && !c.equals(",")) {
                Integer val = map.get(c);
                if (val != null) {
                    map.put(c, new Integer(val + 1));
                } else {
                    map.put(c, 1);
                }
            }
        }
        System.out.println("map size"  + map.size());
        return map;
    }

    public void CreateTestCase(Vertex from, Vertex to, TestSuite ts, String tran, String tranID, boolean results) {
        TestCase tc = new TestCase();
        tc.setId(ts.getTestCase().size() + 1);
        String pre = new String();
        String post = new String();
        tc.setRelatedSTPASSR(getSTPAConstraintID(tranID));
        for (Action ac : from.getData().getActions()) {
            if (!getScopeOfAction(ac.getName()).equals("INPUT_DATA") ) {
                pre += "\n" + ac.getName() + "=" + ac.getValue();
            }

        }
        //add the variabels data of test model as input;

         for (DataVariable var : exectuion.getDataTestModel()) {
            if (var.getScope().equals("INPUT_DATA") && !isAction(from, var.getName())) {
                pre += "\n *" + var.getName() + "=" + var.getValue();
            }

        } 
        pre += "\n state=" + from.getName();
        /* if (tran != null) {
              tc.setPreConditions(pre + "\n" + tran);
          } else {
              tc.setPreConditions(pre + "\n");
        }*/
        tc.setPreConditions(pre);
        if (results == false) {
            exectuion.updateEntryAction(to);
        }
        for (Action c : to.getData().getActions()) {
            if (getScopeOfAction(c.getName()).equals("OUTPUT_DATA")) {
                post += "\n  " + c.getName() + "=" + c.getValue();
            }

        }
        /*if (post.length() <= 0) {
            for (Action c : to.getData().getActions()) {

                post += "\n  " + c.getName() + "=" + c.getValue();

            } 

        } */
        post += "\n state=" + to.getName();
        tc.setPostConditions(post);
        tc.setSuiteId(ts.getId());
        tc.setTransitionConditon(tran);
        tc.setTranID(tranID);
        ts.addTestCase(tc);
        System.out.println(  tc.toString());
    }

    public TestSuite createTestSuite() {
        TestSuite ts = new TestSuite();
        ts.setId(this.testSuites.size() + 1);
        return ts;
    }

    public String getAllPathsAsString() {
        return paths;
    }

    // this method return true if the edge has linked to STPA safety requriements
    public boolean hasTracability(String transitionId) {
        boolean isEdgeTracability = false;
        for (TraceabilityMatrix tc : testconfig.getTraceabilitymatrix()) {

            if (tc.getTranID().equals(transitionId)) {
                isEdgeTracability = true;
                break;
            }

        }
        return isEdgeTracability;
    }

    //This method returns all the STPA safety requriements which have a link to the transition of fsm
    public String getSTPAConstraintID(String transitionId) {
        String id = "";
        for (TraceabilityMatrix tc : testconfig.getTraceabilitymatrix()) {
            if (tc.getTranID().equals(transitionId) && transitionId != null) {
                //  System.out.println("tc" +tc.getTranID()+"ssr " + tc.getSTPA_SSRID());
            	id += tc.getSTPA_SSRID() + ",";            		
            }
        }
        if (id.equals("")) {
            if (id.endsWith(",")) {
                id = id.substring(0, id.length() - 1);
            }
        }
        if (id != null && id.length() > 0 && id.charAt(id.length()-1)==',') {
            id = id.substring(0, id.length()-1);
        }
        frequencySSR += "," + id;
        
        return id;
    }

//This method returns all the STPA safety requriements which have a link to the transition of fsm
    public void UpdateALLSTPASRCoverage(String transitionId) {
        for (TraceabilityMatrix tc : testconfig.getTraceabilitymatrix()) {
            if (tc.getTranID().equals(transitionId) && transitionId != null) {
                //System.out.println("tc" +tc.getTranID()+"ssr " + tc.getSTPA_SSRID());

                visitedTraceabilityMatrixs.add(tc.getSTPA_SSRID());
            }

        }
    }
    //This method return the ID of transiton from the Tracability matrix

    public String getTransitionID(String tran) {
        String IdTran = null;
        for (StateTransition st : testconfig.getTransitions()) {

            if (st.getCondition().equals(tran)) {
                IdTran = st.getSSID();
                break;
            }

        }
        return IdTran;

    }

    // Generate test case by using DFS algorithms
    public void generateTestCases(TestGraph G, int v, int t, TestSuite ts) {

        // add node v to current path from s
        path.push(v);
        onPath.add(v);
        pathVertices.push(G.getVertexName(v));
        // found path from s to t - currently prints in reverse order because of stack
        if (v == t) {
            //System.out.println(path);
            //System.out.println(pathVertices);
            paths += pathVertices + " \n";

        } // consider all neighbors that would continue path with repeating a node
        else {

            Vertex from = G.getVertexByIndex(v);

            for (int w : G.getNeighbors(v)) {
                exectuion.generateRandomVaraibles();
                Vertex to = G.getVertexByIndex(w);
                if (from != null && to != null) {

                    //String script = "var obj = new Object(); obj.getvalue = function (" + parameters + ") { " + funtion + " ;  }";
                    if (!onPath.contains(w)) {

                        Edge edge = G.getEdge(v, w);
                        if (edge != null) {
                            if (!edge.isVisited()) {
                                Map<String, String> transition = G.getTransitionCondition(from, to);

                                if (transition != null) {
                                    String id = transition.keySet().iterator().next();
                                    String tran = transition.values().iterator().next();

                                    // ececute the transiitoncondition between from and to states and update the reuslts of the entry actions.
                                    boolean result = exectuion.doTransition(from, tran, to);
                                    if (result) {

                                        CreateTestCase(from, to, ts, tran, id, result);
                                        visitedTransitions.add(G.getEdge(v, w));
                                        visitedStates.add(from);
                                        visitedStates.add(to);

                                        if (hasTracability(id) && id != null) {
                                            // update the coverage of STPA safety requriements have a link to this edge 

                                            UpdateALLSTPASRCoverage(id);
                                        }

                                        //if (G.getEdge(v, w) != null) {
                                        //    visitedTransitions.add(G.getEdge(v, w));
                                        // }
                                        G.getEdge(v, w).setVisited(true);

                                    } else if (!result) {

                                        CreateTestCase(from, from, ts, tran, id, result);
                                        visitedTransitions.add(G.getEdge(v, w));
                                        visitedStates.add(from);

                                        if (hasTracability(id) && id != null) {
                                            // update the coverage of STPA safety requriements have a link to this edge 

                                            UpdateALLSTPASRCoverage(id);
                                        }

                                        //if (G.getEdge(v, w) != null) {
                                        //    visitedTransitions.add(G.getEdge(v, w));
                                        // }
                                        G.getEdge(v, w).setVisited(true);
                                    }

                                }

                            }

                        }

                      //  generateTestCases(G, w, t, ts);
                    }
                }
            }
            if (ts.getTestCase().size() > 0) {
                this.testSuites.add(ts);
            }
            
        }

        // done exploring from v, so remove from path
        path.pop();
        pathVertices.pop();
        onPath.remove(v);
        //unvisitNodes(G);
        //unvisitedEdges(G);
        testSteps ++;
        if (testSteps >= testconfig.stepsOfTest) {
            
        }
            
    }

    private void unvisitNodes(TestGraph G) {
        for (Vertex n : G.getVertices()) {
            n.setVisit(false);
        }
    }  // end of unvisitNodes()

    private Vertex getUnvisitedAdjNode(TestGraph G, Vertex n) // find a node adjacent to n that has not been visited
    {
        int index = G.indexOf(n);
        for (int j = 0; j < G.getVertices().size(); j++) {   // scan the index row of adjMatrix[]
            if ((G.getAdjMatrix()[index][j] == 1) && (!(G.getVertices().get(j)).isVisited())) // j is adjacent and not yet visited
            {
                return G.getVertices().get(j);
            }
        }
        return null;
    }  // end of getUnvisitedAdjNode()

    public void generateTestCases(TestGraph G, Vertex startNode, TestSuite ts) // breadth-first search of graph starting at startNode
    {
        LinkedList<Vertex> q = new LinkedList<Vertex>();  // acts as a queue

        q.add(startNode);

        startNode.setVisit(true);

        while (!q.isEmpty()) {

            Vertex n = q.remove();
            Vertex adj = null;

            while ((adj = getUnvisitedAdjNode(G, n)) != null) {
                exectuion.generateRandomVaraibles();
                adj.setVisit(true);
                int v = G.indexOf(n);
                int w = G.indexOf(adj);
                Edge edge = G.getEdge(v, w);
                if (edge != null) {
                    if (!edge.isVisited()) {
                        Map<String, String> transition = G.getTransitionCondition(n, adj);
                        if (transition != null) {

                            String id = transition.keySet().iterator().next();
                            String tran = transition.values().iterator().next();
                            boolean result = exectuion.doTransition(n, tran, adj);
                            if (result) {

                                CreateTestCase(n, adj, ts, tran, id, result);
                                visitedStates.add(n);
                                visitedTransitions.add(G.getEdge(v, w));
                                visitedStates.add(adj);

                                if (hasTracability(id) && id != null) {
                                    // update the coverage of STPA safety requriements have a link to this edge 

                                    UpdateALLSTPASRCoverage(id);
                                }

                            } else if (!result) {

                                CreateTestCase(n, n, ts, tran, id, result);
                                visitedStates.add(n);
                                visitedTransitions.add(G.getEdge(v, w));

                                if (hasTracability(id) && id != null) {
                                    // update the coverage of STPA safety requriements have a link to this edge 

                                    UpdateALLSTPASRCoverage(id);
                                }
                            }

                        }
                    }
                    G.getEdge(v, w).setVisited(true);
                }

                q.add(adj);

            }
        }
        unvisitNodes(G);
        unvisitedEdges(G);

    }
}
