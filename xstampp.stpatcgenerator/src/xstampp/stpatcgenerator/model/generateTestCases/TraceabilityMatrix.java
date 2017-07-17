package xstampp.stpatcgenerator.model.generateTestCases;

public class TraceabilityMatrix {
	private String tranID;
    private String STPA_SSRID;

    public String getTranID() {
        return tranID;
    }

    public void setTranID(String tranID) {
        this.tranID = tranID;
    }

    public TraceabilityMatrix(String tranID, String STPA_SSRID) {
        this.tranID = tranID;
        this.STPA_SSRID = STPA_SSRID;
    }

    public String getSTPA_SSRID() {
        return STPA_SSRID;
    }

    @Override
    public String toString() {
        return "TraceabilityMatrix{" + "tranID=" + tranID + ", STPA_SSRID=" + STPA_SSRID + '}';
    }

    public void setSTPA_SSRID(String STPA_SSRID) {
        this.STPA_SSRID = STPA_SSRID;
    }
}
