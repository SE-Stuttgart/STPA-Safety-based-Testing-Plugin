package stpaverifier.controller.preferences.spin;

public interface SpinPreferenceConstants {

		

	  static final String PREF_MODEX_PATH = "MODEX_PATH";

	  static final String PREF_SPIN_PATH = "SPIN_PATH";
	  
	  static final String PREF_C_PATH = "C_COMPILER_PATH";
	  
	  /**
	   * under windows the user is required to install cygwin 
	   * 
	   */
	  static final String PREF_CYGWIN_DLL_PATH = "CYGWIN_DLL_PATH";
	  
	  /**
	   * use old scope rules (pre 5.3.0)	
	   */
	  static final String PREF_USE_OLD_SCOPE_RULES = "-O";
	  
      /**
       * turn off dataflow-optimizations in verifier	
       */
      static final String PREF_DATAFLOW_OPTIMIZATION_OFF = "-o1";
      
      /**
       * don't hide write-only variables in verifier
       */
      static final String PREF_HIDE_WRITE_ONLY = "-o2";
      
      
//      -o3 turn off statement merging in verifier	
//      static final String PREF_STATEMENT
//      -o4 turn on rendezvous optiomizations in verifier
//      -o5 turn on case caching (reduces size of pan.m, but affects reachability reports)
//      -o6 revert to the old rules for interpreting priority tags (pre version 6.2)
//      -o7 revert to the old rules for semi-colon usage (pre version 6.3)

	/**
	 * The static string constant which stores the current chosen argument 
	 * for the search mode 
	 */
	static final String PREFERENCE_SEARCH_MODE = "search mode";
		static final String SEARCH_USE_BFS = "-bfs";
		static final String SEARCH_USE_PARALLEL_BFS = "-bfspar";
		static final String SEARCH_USE_BCS = "-bcs";
		static final String SEARCH_USE_DFS_SHORTENING = "-i";
	
		
	static final String PREFERENCE_STORAGE_MODE = "storage mode";
		static final String STORAGE_COLLAPSE_STATE_COMPRESSION = "-collapse";
		static final String STORAGE_HASH_COMPACT = "-hc";
		static final String STORAGE_BITSTATE = "-bitstate";
		
	static final String PREFERENCE_PROCESS_SCHEDULING = "process scheduling";
		static final String PROCESS_SCHEDULING_PERMUTE = "-p_permute";
		static final String PROCESS_SCHEDULING_ROTATE = "-p_rotate%s";
		static final String PROCESS_SCHEDULING_REVERSE = "-p_reverse";

	static final String PREFERENCE_SEARCH_ACCEPTANCE = "-a";

	static final String PREFERENCE_SEARCH_NON_PROGRESS = "-l";
	
	/**
	 * set upperbound to the true number of Megabytes that can be allocated;
	 * usage, e.g.: -DMEMLIM=200 for a maximum of 200 Megabytes (meant to be a simple alternative to MEMCNT)
	 */
	static final String PREF_SPIN_INT_DMEMLIM = "-DMEMLIM";
	
	/**
	 * disable validity checks of x[rs] assertions
	 * (faster, and sometimes useful if the check is too strict, e.g. when channels are passed around as process parameters)
	 */
	static final String PREF_SPIN_BOOL_DXUSAFE ="-DXUSAFE";
	
	/**
	 * exclude the never claim from the verification, if present
	 */
	static final String PREF_SPIN_BOOL_DNOCLAIM ="-DNOCLAIM";
	
	/**
	 * optimize for the case where no cycle detection is needed (faster, uses less memory, disables both -l and -a)
	 */
	static final String PREF_SPIN_BOOL_DSAFETY ="-DSAFETY";
	
	/**
	 * use a minimized DFA encoding for the state space, similar to a BDD, assuming a maximum of N bytes in the state-vector
	 * (this can be combined with -DCOLLAPSE for greater effect in cases when the original state vector is long)
	 */
	static final String PREF_SPIN_INT_DMA_N="-DMA=";
	
	/**
	 * allocates memory (in bytes) for state vector usage, e.g.: -DVECTORSZ=2048 (default is 1024)
	 */
	static final String PREF_SPIN_INT_DVECTORSZ="-DVECTORSZ";
	
	/**
	 * a state vector compression mode;
	 * collapses state vector sizes by up to 80% to 90% (see Spin97 workshop paper)
	 * variations: add -DSEPQS or -DJOINPROCS (off by default)
	 */
	static final String PREF_SPIN_BOOL_DCOLLAPSE="-DCOLLAPSE";
	
	/**
	 * a run time option to set the max search depth to N steps (default N=10000) 
	 */
	static final String PREF_SPIN_INT_RUN_MAX = "-m";
}
