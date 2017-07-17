package stpaverifier.controller;

import xstampp.model.AbstractLTLProvider;

public interface IVerificationResult {

	public static final String SATISFIED = "satisfied";
	public static final String FAILS = "failed";
	public static final String INTERRUPTED = "Interrupted";

	/**
	 * @return the ssrLiteral
	 */
	public String getSsrLiteral();

	/**
	 * @return the depth
	 */
	public String getDepth();

	/**
	 * @return the storedStates
	 */
	public String getStoredStates();

	/**
	 * @return the transitions
	 */
	public String getTransitions();

	/**
	 * @return the time
	 */
	public String getTime();

	/**
	 * @return the usedMemory
	 */
	public String getUsedMemory();

	/**
	 * @return the result
	 */
	public String getResult();

	/**
	 * @return the provider
	 */
	public AbstractLTLProvider getProvider();

}