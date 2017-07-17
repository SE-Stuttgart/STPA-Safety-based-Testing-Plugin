package xstampp.stpatcgenerator.model.generateTestCases;

import java.util.ArrayList;
import java.util.List;

import xstampp.astpa.model.extendedData.RefinedSafetyRule;
import xstampp.stpatcgenerator.model.stateflow.chart.generateStatesTree.DataVariable;
import xstampp.stpatcgenerator.model.stateflow.chart.parse.StateTransition;

public class TestConfigurations {
	private List<DataVariable> dataTestModel ;
    private CoverageCriteria stopcondition;
    private List<RefinedSafetyRule> STPASoftwareSafetyRequirements = new ArrayList<RefinedSafetyRule>() ;
    private List<TraceabilityMatrix> traceabilitymatrix = new ArrayList<TraceabilityMatrix>() ;
    private List<StateTransition> transitions = new ArrayList<StateTransition>();
    public List<TraceabilityMatrix> getTraceabilitymatrix() {
        return traceabilitymatrix;
    }

    public void setTraceabilitymatrix(List<TraceabilityMatrix> traceabilitymatrix) {
        this.traceabilitymatrix = traceabilitymatrix;
    }

    public List<StateTransition> getTransitions() {
        return transitions;
    }

    public void setTransitions(List<StateTransition> transitions) {
        this.transitions = transitions;
    }
    
    public CoverageCriteria getStopcondition() {
        return stopcondition;
    }

    public void setStopcondition(CoverageCriteria stopcondition) {
        this.stopcondition = stopcondition;
    }

    public List<RefinedSafetyRule> getSTPASoftwareSafetyRequirements() {
        return STPASoftwareSafetyRequirements;
    }

    public void setSTPASoftwareSafetyRequirements(List<RefinedSafetyRule> STPASoftwareSafetyRequirements) {
        this.STPASoftwareSafetyRequirements = STPASoftwareSafetyRequirements;
    }
    
    public TestConfigurations() {
        this.coverages=new ArrayList<CoverageCriteria> ();
    }

    public List<DataVariable> getDataTestModel() {
        return dataTestModel;
    }

    public void setDataTestModel(List<DataVariable> dataTestModel) {
        this.dataTestModel = dataTestModel;
    }
     public int stepsOfTest=0;
     public List<CoverageCriteria> coverages ;

    public TestConfigurations(List<CoverageCriteria> coverages) {
        this.coverages = coverages;
    }

    public int getStepsOfTest() {
        return stepsOfTest;
    }

    public void setStepsOfTest(int stepsOfTest) {
        this.stepsOfTest = stepsOfTest;
    }

    public void addCoverageCriteria (CoverageCriteria type)
    {
        this.coverages.add(type);
    }
    public List<CoverageCriteria> getCoverages() {
        return coverages;
    }

    public void setCoverages(List<CoverageCriteria> coverages) {
        this.coverages = coverages;
    }
   
     private CoverageCriteria getStopCondition()
     {
       
         return stopcondition;
     }
    
     private void setStopCondition (CoverageCriteria stopcondition)
     {
         this.stopcondition=stopcondition; 
         
     }
}
