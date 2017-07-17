package xstampp.stpatcgenerator.model.generateTestCases;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import xstampp.stpatcgenerator.model.stateflow.fsm.EFSM;
import xstampp.stpatcgenerator.model.stateflow.fsm.StateEFSM;

public class testModel {
	 public testModel() {
	    }
	    private StateEFSM currentState = null;
	    private StateEFSM targetState = null;
	    private EFSM eFSM = null;
	    private int testSteps = 0;
	    CoverageCriteria coverageCriteria;
	    List<TestCase> testCases;

	    public testModel(StateEFSM currentState, EFSM eFSM, CoverageCriteria cc, int testSteps) {

	        this.currentState = currentState;
	        this.eFSM = eFSM;
	        this.coverageCriteria = cc;
	        this.testSteps = testSteps;
	        this.testCases = new ArrayList<TestCase>();
	    }

	    private double speed;
	    private double maxSpeed = 120.0;
	    private double minSpeed = 1;
	    private boolean Switch = false;
	    private String preConditons = "";
	    private String postConditions = "";
	    private String input = "";
	    private String path = "";
	    private int Clock;
	    private int maxClock = 1;
	    private int minClock = 4;

	    //***********//
	    public void init() {
	        this.speed = minSpeed;
	        this.Switch = false;
	        this.Clock = minClock;
	       // this.currentState = eFSM.getInitialState();
	        preConditons = "";
	        postConditions = "";
	        path = "";

	    }
	    public static boolean getRandomBoolean() {
	       Random r = new Random(System.nanoTime());
	        return r.nextBoolean();
	       //I tried another approaches here, still the same result
	   }
	    private double randomDouple(double rangeMin, double rangeMax) {
	        Random r = new Random(System.nanoTime());
	        return rangeMin + (rangeMax - rangeMin) * r.nextDouble();

	    }

	    private int randomInt(int minimum, int maximum) {
	        Random r = new Random(System.nanoTime());
	        return minimum + (int) (r.nextInt() * maximum);
	    }

	    public void rest() {
	        speed = randomDouple(maxSpeed, minClock);
	        Switch = getRandomBoolean ();
	        Clock = randomInt(maxClock, minClock);

	    }

	    public void tran(StateEFSM target) {
	        currentState = target;
	    }

	    //************************************//
	    public StateEFSM getCurrentState() {

	        return this.currentState;

	    }

	    public List<TestCase> getTestCases() {
	        return this.testCases;
	    }

	    public void addTestCase(TestCase tc) {
	        this.testCases.add(tc);
	    }

	    public void parseEFSM() {
	        do {
	            testSteps--;
	            targetState = currentState;
	            if (currentState.getName().equals("start")) {
	                if (currentState.getName().equals("start") && true) {
	                    currentState = eFSM.getstateFSM("10");
	                    speed = 0;
	                    preConditons = " state= " + targetState.getName() + "true";
	                    postConditions = " state= " + currentState.getName();
	                    input = " speed=0 ";
	                     

	                }
	            }
	            if (currentState.getName().equals("Off")) {
	                if (currentState.getName().equals("Off") && Switch) {
	                    currentState = eFSM.getstateFSM("2");
	                    speed = 0;
	                    preConditons = " state= " + targetState.getName() + " Switch= " + Switch;
	                    postConditions = " state= " + currentState.getName();
	                    input = " speed= " + speed;

	                }
	            }
	            
	            if (currentState.getName().equals("One")) {
	                if (currentState.getName().equals("One") && !Switch) {
	                    currentState = eFSM.getstateFSM("10");
	                    speed = 0;
	                    preConditons = "state=" + targetState.getName() + "switch=" + Switch;
	                    postConditions = "state=" + currentState.getName();
	                    input = " speed= " + speed;

	                } else if (currentState.getName().equals("One") && Clock == 1) {
	                    currentState = eFSM.getstateFSM("3");
	                    speed = 1;
	                    preConditons = "state=" + targetState.getName() + "Clock=" + Clock;
	                    postConditions = "state=" + currentState.getName();
	                    input = " speed= " + speed;
	                }
	            }
	            if (currentState.getName().equals("two")) {
	                if (currentState.getName().equals("two") && !Switch) {
	                    currentState = eFSM.getstateFSM("10");
	                    speed = 0;
	                    preConditons = " state= " + targetState.getName() + "switch=" + Switch;
	                    postConditions = " state= " + currentState.getName();
	                    input = " speed= " + speed;

	                } else if (currentState.getName().equals("two") && Clock == 2) {
	                    currentState = eFSM.getstateFSM("4");
	                    speed = 3;
	                    preConditons = " state= " + targetState.getName() + "Clock=" + Clock;
	                    postConditions = " state= " + currentState.getName();
	                    input = " speed= " + speed;
	                }
	            }
	            if (currentState.getName().equals("three")) {
	                if (currentState.getName().equals("three") && !Switch) {
	                    currentState = eFSM.getstateFSM("10");
	                    speed = 1;
	                    preConditons = " state= " + targetState.getName() + " Switch= " + Switch;
	                    postConditions = "state=" + currentState.getName();
	                    input = " speed= " + speed;
	                } else if (currentState.getName().equals("three") && Clock == 3) {
	                    currentState = eFSM.getstateFSM("7");
	                    speed = 4;
	                    preConditons = " state= " + targetState.getName() + "Clock=" + Clock;
	                    postConditions = " state= " + currentState.getName();
	                    input = " speed= " + speed;
	                }
	            }
	            if (currentState.getName().equals("four")) {
	                if (currentState.getName().equals("four") && !Switch) {
	                    currentState = eFSM.getstateFSM("10");
	                    speed = 0;
	                    preConditons = " state= " + targetState.getName() + " Switch= " + Switch;
	                    postConditions = " state= " + currentState.getName();
	                    input = " speed= " + speed;
	                } else if (currentState.getName().equals("four") && Clock == 4) {
	                    currentState = eFSM.getstateFSM("2");
	                    speed = 1;
	                }
	            }
	            
	            path = " @ " + preConditons + " # " + input + " -> " + postConditions;
	            if (currentState.getIsFinal()) {
	                System.out.println("testModel.generateTestCases.testModel.parseEFSM()");

	                init();

	            }

	            TestCase tc = new TestCase();
	            int id = this.testCases.size() + 1;
	            tc.setId( id );
	            tc.setPreConditions(preConditons);
	            tc.setPostConditions(postConditions);
	            //tc.setTestCasesSequences(path);
	            this.testCases.add(tc);
	            rest();
	        } while (testSteps > 0);
	    }
}
