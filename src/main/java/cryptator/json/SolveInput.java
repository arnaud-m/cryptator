package cryptator.json;

import cryptator.config.CryptatorConfig;

public final class SolveInput {
	
	private String cryptarithm;
	
	private CryptatorConfig config;
	
	
	public SolveInput() {
		super();
	}

	public SolveInput(String cryptarithm, CryptatorConfig config) {
		super();
		this.cryptarithm = cryptarithm;
		this.config = config;
	}
	
	public String getCryptarithm() {
		return cryptarithm;
	}
	public void setCryptarithm(String cryptarithm) {
		this.cryptarithm = cryptarithm;
	}
	public CryptatorConfig getConfig() {
		return config;
	}
	public void setConfig(CryptatorConfig config) {
		this.config = config;
	}	
}