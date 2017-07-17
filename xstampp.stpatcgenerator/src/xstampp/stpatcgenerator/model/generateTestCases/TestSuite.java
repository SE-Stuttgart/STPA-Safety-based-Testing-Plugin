package xstampp.stpatcgenerator.model.generateTestCases;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestSuite {
	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Set<TestCase> getTestCase() {
        return testcases;
    }

    public void setTestcase(Set<TestCase> testcases) {
        this.testcases = testcases;
    }
    private int id;
     
    Set<TestCase> testcases = new HashSet<TestCase>();

    public void addTestCase(TestCase tc) {
        this.testcases.add(tc);
    }
}
