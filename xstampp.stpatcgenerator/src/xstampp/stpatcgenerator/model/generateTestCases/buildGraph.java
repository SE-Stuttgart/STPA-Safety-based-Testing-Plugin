package xstampp.stpatcgenerator.model.generateTestCases;

import xstampp.stpatcgenerator.model.stateflow.chart.parse.StateTransition;
import xstampp.stpatcgenerator.model.stateflow.fsm.EFSM;
import xstampp.stpatcgenerator.model.stateflow.fsm.StateEFSM;

public class buildGraph {
	TestGraph graph;

    public TestGraph getGraph() {
        return graph;

//JGraphAdapterDemo applet = new JGraphAdapterDemo();
        // applet.init();
        // JFrame frame = new JFrame();
        //frame.getContentPane().add(applet);
        //frame.setTitle("Graphflow of Extended finite state machine ");
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.pack();
        //frame.setVisible(true);
    }

// -----------------------------------------------------------------
    public void generateGraph(EFSM fsm) {
        // create nodes
        graph = new TestGraph();
        int i = 0;

        for (StateEFSM s : fsm.getFsmGstates()) {

            
                  
                Vertex scr = new Vertex(s.getName(), s, Integer.toString(i));
                scr.setData(s);
                 
                scr.setMarkState(Integer.parseInt(s.getId()));
                    
                graph.addvertex(scr, i);

                ++i;
                  
             
        }

        // add undirected edges
        graph.initMatrix();

        int j = 0;
        for (StateTransition t : fsm.getfsmTruthTable()) {
            
            if (t.getSource() != null && t.getDestination() != null) {
                StateEFSM source = fsm.getstateFSM(t.getSource());
                                 
                StateEFSM destination = fsm.getstateFSM(t.getDestination());
                if ( source != null && destination != null) {
                    Vertex scr = new Vertex(source.getName(), source, Integer.toString(j));
                    scr.setData(source);
                    scr.setMarkState(Integer.parseInt(source.getId()));

                    Vertex ds = new Vertex(destination.getName(), destination, Integer.toString(j));
                    ds.setData(destination);
                    ds.setMarkState(Integer.parseInt(destination.getId()));
                    
                    Edge edge = new Edge(scr, ds, t.getCondition(), t.getSSID());
                    edge.setVisited(false);
                    graph.addEdge(edge);
                }
            }
            j++;
        }

    }
}
