package cryptator;

import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.ExplicitBooleanOptionHandler;

public class CryptagenConfig extends CryptaCmdConfig {
	
	@Option(name="-d",handler=ExplicitBooleanOptionHandler.class,usage="dry run (generate but do not solve candidate cryptarithms)")
	private boolean dryRun;

}
