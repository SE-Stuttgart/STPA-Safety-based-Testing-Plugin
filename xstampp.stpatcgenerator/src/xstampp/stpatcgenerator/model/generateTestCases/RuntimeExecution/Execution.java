package xstampp.stpatcgenerator.model.generateTestCases.RuntimeExecution;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import java.util.logging.Level;
import java.util.logging.Logger;

import xstampp.stpatcgenerator.model.generateTestCases.Vertex;
import xstampp.stpatcgenerator.model.stateflow.chart.generateStatesTree.DataVariable;
import xstampp.stpatcgenerator.model.stateflow.chart.parse.Action;

public class Execution {
	private ScriptEngineManager manager;
	private ScriptEngine engine;
	boolean result;

	public List<DataVariable> getDataTestModel() {
		return dataTestModel;
	}

	public void setDataTestModel(List<DataVariable> dataTestModel) {
		this.dataTestModel = dataTestModel;
	}

	public List<DataVariable> dataTestModel = new ArrayList<DataVariable>();

	private String BigDecimal(String function) {
		throw new UnsupportedOperationException("Not supported yet."); // To
																		// change
																		// body
																		// of
																		// generated
																		// methods,
																		// choose
																		// Tools
																		// |
																		// Templates.
	}

	public enum ExectuionResultsType {
		Correct(1), Failure(2), Exception(3);

		final int accepting;

		ExectuionResultsType(int accepting) {
			this.accepting = accepting;
		}
	}

	public ScriptEngineManager getManager() {
		return manager;
	}

	public void setManager(ScriptEngineManager manager) {
		this.manager = manager;
	}

	public ScriptEngine getEngine() {
		return engine;
	}

	public void setEngine(ScriptEngine engine) {
		this.engine = engine;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public ExectuionResultsType getResultstype() {
		return resultstype;
	}

	public void setResultstype(ExectuionResultsType resultstype) {
		this.resultstype = resultstype;
	}

	private ExectuionResultsType resultstype;

	public Execution() {
		this.manager = new ScriptEngineManager();
		this.engine = manager.getEngineByExtension("JavaScript");

	}

	private void writeExectutionTracInFile(String str) {

		String strFile = "trace.txt";

		// this.setFilename(SMVfile);
		try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(strFile, true)))) {

			out.println(str);

			// more code
		} catch (IOException e) {

			System.out.println(e.getMessage());
		}
	}

	public Object ExecutScriptAsDouble(String parameters, Object[] values, String funtion) {

		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("JavaScript");

		// evaluate JavaScript code that defines a function with one parameter
		String script = "function getvalue (" + parameters + ") { " + funtion + " ;  }";

		Object value = 0;
		try {
			engine.eval(script);

			// create an Invocable object by casting the script engine object
			// Invocable inv = (Invocable) engine;
			// get Runnable interface object
			// Object value = inv.invokeMethod(obj, "getvalue", values);
			value = engine.eval("getvalue(" + getValues(values) + ");");
			DecimalFormat df = new DecimalFormat("####0.00");
			value = df.format(value);
			writeExectutionTracInFile(script);
			writeExectutionTracInFile("\n");
			writeExectutionTracInFile("Values=" + getValues(values));
			writeExectutionTracInFile("\n");
			writeExectutionTracInFile("Results =" + value);
			writeExectutionTracInFile("------------------------------\n");

		} catch (ScriptException ex) {
			Logger.getLogger(Execution.class.getName()).log(Level.SEVERE, null, ex);
		}

		// expose object defined in the script to the Java application
		return value;
	}

	public Boolean ExecutScriptAsBoolean(String parameters, Object[] values, String funtion) throws Exception {

		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("JavaScript");

		// evaluate JavaScript code that defines a function with one parameter
		String script = "var obj = new Object(); obj.getvalue = function (" + parameters + ") { " + funtion + " ;  }";
		engine.eval(script);

		// expose object defined in the script to the Java application
		Object obj = engine.get("obj");

		// create an Invocable object by casting the script engine object
		Invocable inv = (Invocable) engine;

		// get Runnable interface object
		boolean value = (boolean) inv.invokeMethod(obj, "getvalue", values);
		return value;
	}

	public Object ExecutScriptASInteger(String parameters, Object[] values, String funtion) {

		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("JavaScript");

		// evaluate JavaScript code that defines a function with one parameter
		String script = "function getvalue (" + parameters + ") { " + funtion + " ;  }";

		Object value = 0;
		try {
			engine.eval(script);

			// create an Invocable object by casting the script engine object
			// Invocable inv = (Invocable) engine;
			// get Runnable interface object
			// Object value = inv.invokeMethod(obj, "getvalue", values);
			value = engine.eval("getvalue(" + getValues(values) + ");");

			writeExectutionTracInFile(script);
			writeExectutionTracInFile("\n");
			writeExectutionTracInFile("Values=" + getValues(values));
			writeExectutionTracInFile("\n");
			writeExectutionTracInFile("Results =" + value);
			writeExectutionTracInFile("------------------------------\n");

		} catch (ScriptException ex) {
			Logger.getLogger(Execution.class.getName()).log(Level.SEVERE, null, ex);
		}

		// expose object defined in the script to the Java application
		return value;
	}

	public Object ExecutScript(String parameters, Object[] val, String funtion) {

		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("JavaScript");

		// evaluate JavaScript code that defines a function with one parameter
		String script = "var obj = new Object(); obj.getvalue = function (" + parameters + ") { "
				+ funtion.replace("~", "!") + " ;  }";
		Object obj = null;
		try {
			engine.eval(script);

			// expose object defined in the script to the Java application
			obj = engine.get("obj");

			// create an Invocable object by casting the script engine object
			Invocable inv = (Invocable) engine;

			// get Runnable interface object
			obj = inv.invokeMethod(obj, "getvalue", val);

			writeExectutionTracInFile(script);
			writeExectutionTracInFile("\n");
			writeExectutionTracInFile("Values=" + getValues(val));
			writeExectutionTracInFile("\n");
			writeExectutionTracInFile("Results =" + obj.toString());
			writeExectutionTracInFile("------------------------------\n");

		} catch (ScriptException ex) {
			Logger.getLogger(Execution.class.getName()).log(Level.SEVERE, null, ex);
		} catch (NoSuchMethodException ex) {
			Logger.getLogger(Execution.class.getName()).log(Level.SEVERE, null, ex);
		}

		return obj;
	}

	public String getValues(Object[] values) {
		String str = "";
		for (Object v : values) {
			str += v.toString() + ",";
		}
		if (str.endsWith(",")) {
			str = str.substring(0, str.length() - 1);
		}
		return str;
	}

	public static boolean getRandomBoolean() {
		Random r = new Random(System.nanoTime());
		return Math.random() < 0.5;
		// I tried another approaches here, still the same result
	}

	private double randomDouple(double minimum, double maximum) {
		Random r = new Random(System.nanoTime());
		double results = (minimum + r.nextDouble() * (maximum - minimum));
		DecimalFormat df = new DecimalFormat("####0.00");
		results = Double.parseDouble(df.format(results));
		return results;

	}

	private int randomInt(int minimum, int maximum) {
		Random r = new Random(System.nanoTime());
		return r.nextInt((maximum - minimum) + 1) + minimum;
	}

	// This method generate random values for variables;
	public void generateRandomAllVaraibles() {

		for (DataVariable var : dataTestModel) {
			if (!var.getType().equals("INPUT_DATA")) {
				if (var.getType().equals("boolean") || var.getType().equals("Boolean")) {
					var.setValue(String.valueOf(getRandomBoolean()));

				} else if (var.getType().equals("int")) {
					if (var.getMinimumValue() != null && var.getMinimumValue().length() > 0
							&& var.getMaximumValue() != null && var.getMaximumValue().length() > 0) {
						var.setValue(String.valueOf(randomInt(Integer.parseInt(var.getMinimumValue()),
								Integer.parseInt(var.getMaximumValue()))));
					} else {
						var.setValue(String.valueOf(randomInt(0, 100)));
					}
				} else if (var.getType().equals("double")) {
					if (var.getMinimumValue() != null && var.getMinimumValue().length() > 0
							&& var.getMaximumValue() != null && var.getMaximumValue().length() > 0) {
						var.setValue(String.valueOf(randomDouple(Double.parseDouble(var.getMinimumValue()),
								Double.parseDouble(var.getMaximumValue()))));
					} else {
						var.setValue(String.valueOf(randomDouple(0.0, 100.0)));
					}
				} else if (var.getType().equals("Enum")) {
					var.setValue(var.getValue());
					// System.out.println("Enum: var.getValue() = " +
					// var.getValue());
				}

			}
		}
	}

	// This method generate random values for variables;
	public void generateRandomVaraibles() {

		for (DataVariable var : dataTestModel) {
			if (!var.getType().equals("LOCAL_DATA") && !var.getType().equals("OUTPUT_DATA")) {
				if (var.getType().equals("boolean") || var.getType().equals("Boolean")) {
					var.setValue(String.valueOf(getRandomBoolean()));

				} else if (var.getType().equals("int")) {
					if (var.getMinimumValue() != null && var.getMinimumValue().length() > 0
							&& var.getMaximumValue() != null && var.getMaximumValue().length() > 0) {
						var.setValue(String.valueOf(randomInt(Integer.parseInt(var.getMinimumValue()),
								Integer.parseInt(var.getMaximumValue()))));
					} else {
						var.setValue(String.valueOf(randomInt(0, 100)));
					}
				} else if (var.getType().equals("double")) {
					if (var.getMinimumValue() != null && var.getMinimumValue().length() > 0
							&& var.getMaximumValue() != null && var.getMaximumValue().length() > 0) {
						var.setValue(String.valueOf(randomDouple(Double.parseDouble(var.getMinimumValue()),
								Double.parseDouble(var.getMaximumValue()))));
					} else {
						var.setValue(String.valueOf(randomDouple(0.0, 100.0)));
					}
				}

			}
		}
	}
	// this method execut the transition conditions;

	private boolean executionCondition(String condition) {

		boolean result = false;
		if (condition != null) {
			try {
				result = Boolean.parseBoolean(ExecutScript(getScriptOfParameters(), getScriptOfValuesAsObjects(),
						"return (" + condition + ")").toString());

			} catch (Exception e) {
				setResultstype(resultstype.Failure);
			}
		}
		return result;
	}

	// This method will return the type of variable into the entry actions:
	public String getActionVariableType(String actionName) {
		String str = null;
		for (DataVariable var : dataTestModel) {

			if (var.getName().equals(actionName)) {
				str = var.getType();
			}
		}

		return str;
	}
	// this method generate a list of parameters as doubles

	public Object[] getScriptOfValuesAsInt() {
		ArrayList valueDouble;
		valueDouble = new ArrayList<Integer>();

		for (DataVariable var : dataTestModel) {
			if (var.getType().equals("double") || var.getType().equals("int")) {

				if (var.getValue() != null && !var.getValue().equals("")) {

					valueDouble.add(var.getValue());

				} else {
					valueDouble.add(0);
				}
			}

		}
		Object[] values = new Object[valueDouble.size()];
		int i = 0;
		for (Object d : valueDouble) {
			if (d == null) {
				d = 0;
			}
			values[i] = d;

			i++;
		}
		return values;

	}

	// this method generate a list of parameters as doubles
	public Object[] getScriptOfValuesAsDouble() {
		ArrayList valueDouble;
		valueDouble = new ArrayList<Double>();

		for (DataVariable var : dataTestModel) {
			if (var.getType().equals("double") || var.getType().equals("int")) {

				if (var.getValue() != null && !var.getValue().equals("") && !var.getValue().equals("NaN")) {

					valueDouble.add(var.getValue());

				} else {
					valueDouble.add(0);
				}
			}

		}
		Object[] values = new Object[valueDouble.size()];
		int i = 0;
		for (Object d : valueDouble) {
			if (d == null) {
				d = 0.0;
			}

			values[i] = d;

			i++;
		}
		return values;

	}
	// this method generate the parametes which will pass into the funtion

	public Object[] getScriptOfValuesAsObjects() {
		Object[] values = new Object[dataTestModel.size()];
		int i = 0;
		for (DataVariable var : dataTestModel) {
			values[i] = var.getValue();
			i++;
		}

		return values;
	}

	// this method generate the parametes which will pass into the funtion
	public String getScriptOfValues() {
		String str = "";

		for (DataVariable var : dataTestModel) {
			str += var.getValue() + ",";
		}
		if (str.endsWith(",")) {
			str = str.substring(0, str.length() - 1);
		}
		return str;
	}

	// this method generate the parametes which will pass into the funtion
	public String getScriptOfParametersDouble() {
		String str = "";

		for (DataVariable var : dataTestModel) {
			if (var.getType().equals("double") || var.getType().equals("int")) {
				str += var.getName() + ",";
			}
		}
		if (str.endsWith(",")) {
			str = str.substring(0, str.length() - 1);
		}
		return str;
	}

	// this method generate the parametes which will pass into the funtion
	public String getScriptOfParameters() {
		String str = "";

		for (DataVariable var : dataTestModel) {
			str += var.getName() + ",";
		}
		if (str.endsWith(",")) {
			str = str.substring(0, str.length() - 1);
		}
		return str;
	}

	// This method execute all the entry action funtions of the source state of
	// transition condition.
	public void updateEntryAction(Vertex from) {
		String script = "";
		for (Action action : from.getData().getActions()) {
			// get the paramenters
			String parameters = getScriptOfParameters();
			// get the funtionbody;
//			System.out.println(action.getName() + " " + action.getFunction());
			String function = action.getFunction();
			// get Type of actionVariable

			String type = getActionVariableType(action.getName());
			System.out.println("Variable Tyle = " + type);
			if (type != null && function != null) {

				if (type.equals("boolean") || type.equals("Boolean")) {
					try {
						action.setValue(String.valueOf(ExecutScript(parameters, getScriptOfValuesAsObjects(),
								"return " + function.replace("~", "!"))));

						if (action.getValue().equals("null")) {
							action.setValue(String.valueOf(new Random().nextBoolean()));
						}
						setResultstype(resultstype.Correct);

					} catch (Exception e) {
						setResultstype(resultstype.Failure);
					}

				} else if (type.equals("int")) {
					try {
						parameters = getScriptOfParametersDouble();
						action.setValue(String.valueOf(Math.round((int) ExecutScript(parameters,
								getScriptOfValuesAsInt(), "return (" + function + ")"))));

						setResultstype(resultstype.Correct);

					} catch (Exception e) {
						setResultstype(resultstype.Failure);
					}

				} else if (type.trim().equals("double")) {

					parameters = getScriptOfParametersDouble();
					action.setValue(String.valueOf(ExecutScriptAsDouble(parameters, getScriptOfValuesAsDouble(),
							" \n var x =" + function + " ; return x")));

					setResultstype(resultstype.Correct);

				} else if (type.contains("Enum")) {

					action.setValue(function);
					setResultstype(resultstype.Correct);

				}

				// update the new value of varaible based on the ActionVariable
				// value
				for (DataVariable var : dataTestModel) {
					if (var.getName().trim().equals(action.getName().trim())) {

						var.setValue(action.getValue());

						break;
					}
				}

			} else {
				setResultstype(resultstype.Failure);
			}
		}
	}

	public void initialVariables() {
		for (DataVariable var : dataTestModel) {
			if (!var.getType().equals("INPUT_DATA")) {
				if (var.getType().equals("boolean") || var.getType().equals("Boolean")) {
					var.setValue(String.valueOf(getRandomBoolean()));

				} else if (var.getType().equals("int")) {
					if (var.getMinimumValue() == null && var.getMaximumValue() == null) {
						var.setValue("0");
					} else {
						var.setValue(String.valueOf(randomInt(0, 100)));
					}
				} else if (var.getType().equals("double")) {
					if (var.getMinimumValue() == null && var.getMaximumValue() == null) {
						var.setValue("0");
					} else {
						var.setValue(String.valueOf(randomDouple(0.0, 100.0)));
					}
				} else if (var.getType().equals("Enum")) {
					var.setInitalvalue(var.getValue());

				}

			}
		}
	}

	public boolean doTransition(Vertex from, String condition, Vertex to) {
		boolean result = false;
		// if the tranisiton condition is null, that means the is a default
		// transition which is alaways true.

		if (condition == null) {

			updateEntryAction(from);
			updateEntryAction(to);
			result = true;
		} else {
			// every transiton generate the random values for all variables.

			updateEntryAction(from);
			result = executionCondition(condition);
			if (result == true) {
				// update the entry action of the next state to
				updateEntryAction(to);
			}
		}

		return result;

	}
}
