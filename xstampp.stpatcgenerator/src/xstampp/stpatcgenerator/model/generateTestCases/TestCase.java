package xstampp.stpatcgenerator.model.generateTestCases;
/**
*
* @author asimabdulkhaleq
*/
public class TestCase {
	private int id;
    private String preConditions;
    private int suiteId;
    
    private String TranID;

    public String getTranID() {
        return TranID;
    }

    public void setTranID(String TranID) {
        this.TranID = TranID;
    }
    
    public String getTransitionConditon() {
        return transitionConditon;
    }

    public void setTransitionConditon(String transitionConditon) {
        this.transitionConditon = transitionConditon;
    }
    private String testCasesSequences;
    private String postConditions;
    private String relatedSTPASSR ;//STPASoftwareSafetyRequirements;
    private String transitionConditon;

    public int getSuiteId() {
        return suiteId;
    }

    public void setSuiteId(int suiteId) {
        this.suiteId = suiteId;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPreConditions() {
        return preConditions;
    }

    public void setPreConditions(String preConditions) {
        this.preConditions = preConditions;
    }

    public String getTestCasesSequences() {
        return testCasesSequences;
    }

    public void setTestCasesSequences(String testCasesSequences) {
        this.testCasesSequences = testCasesSequences;
    }

    public String getPostConditions() {
        return postConditions;
    }

    public void setPostConditions(String postConditions) {
        this.postConditions = postConditions;
    }

    public String getRelatedSTPASSR() {
        return relatedSTPASSR;
    }

    public void setRelatedSTPASSR(String relatedSTPASSR) {
        this.relatedSTPASSR = relatedSTPASSR;
    }

    @Override
    public String toString() {
        return "TestCase{" + "id=" + id + ", preConditions= {" + preConditions + "}, testCasesSequences=" + testCasesSequences + ", postConditions={" + postConditions + "}, relatedSTPASSR=" + relatedSTPASSR + '}';
    }
}
