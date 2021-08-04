package cryptator;

import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.ExplicitBooleanOptionHandler;

public class CryptatorConfig extends CryptaConfig {
	
	// TODO Cmd Line Documentation
	
	@Option(name="-s", usage="boolean value for checking the custom handler")
	private int solutionLimit;

	@Option(name="-t", usage="boolean value for checking the custom handler")
	private int timeLimit;

	@Option(name="-g",handler=ExplicitBooleanOptionHandler.class,usage="boolean value for checking the custom handler")
	private boolean exportGraphiz;

	@Option(name="-c",handler=ExplicitBooleanOptionHandler.class,usage="boolean value for checking the custom handler")
	private boolean checkSolution;

	@Option(name="-v",handler=ExplicitBooleanOptionHandler.class,usage="boolean value for checking the custom handler")
	private boolean verbose;

	public CryptatorConfig() {}

	public final int getSolutionLimit() {
		return solutionLimit;
	}

	public final int getTimeLimit() {
		return timeLimit;
	}

	public final boolean isExportGraphiz() {
		return exportGraphiz;
	}

	public final boolean isCheckSolution() {
		return checkSolution;
	}

	public final boolean isVerbose() {
		return verbose;
	}
	
	
}
