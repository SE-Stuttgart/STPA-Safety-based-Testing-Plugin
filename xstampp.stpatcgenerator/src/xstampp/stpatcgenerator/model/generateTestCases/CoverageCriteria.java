package xstampp.stpatcgenerator.model.generateTestCases;

public class CoverageCriteria {
	public enum CoverageType {
        Statescoverage(1),
        TransitionsCoverage(2),
        ALLSTPARequirmentsCoverage(3);

        final int accepting;

        CoverageType(int accepting) {
            this.accepting = accepting;
        }
    }

    public String getDivision ()
    {
        return numerator + "/" + denominator;
    }
    private int numerator ;

    public int getNumerator() {
        return numerator;
    }

    public void setNumerator(int numerator) {
        this.numerator = numerator;
    }

    public int getDenominator() {
        return denominator;
    }

    public void setDenominator(int denominator) {
        this.denominator = denominator;
    }
    private int denominator;
    
    public CoverageType getType() {
        return type;
    }

    public CoverageCriteria(CoverageType type) {
        this.type = type;
    }

    public void setType(CoverageType type) {
        this.type = type;
    }
    
    private CoverageType type;

    @Override
    public String toString() {
        return "CoverageCriteria{" + "numerator=" + numerator + ", denominator=" + denominator + ", type=" + type + ", value=" + value + '}';
    }
    
    private double value=0;

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
