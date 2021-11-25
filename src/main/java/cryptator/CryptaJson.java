package cryptator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cryptator.config.CryptatorConfig;
import cryptator.json.SolveInput;
import cryptator.json.SolveOutput;
import cryptator.parser.CryptaParserWrapper;
import cryptator.solver.CryptaModelException;
import cryptator.solver.CryptaSolverException;
import cryptator.specs.ICryptaNode;
import cryptator.specs.ICryptaSolver;

public class CryptaJson {

	private CryptaJson() {
		super();
	}
	
	public static SolveOutput solve(SolveInput input) throws CryptaModelException, CryptaSolverException {
		final CryptaParserWrapper parser = new CryptaParserWrapper();
		final ICryptaNode node = parser.parse(input.getCryptarithm());
		
		final SolveOutput output = new SolveOutput(input);
		
		final ICryptaSolver solver = Cryptator.buildSolver(input.getConfig());
		solver.solve(node, input.getConfig(), s -> output.accept(node, s));
		return output;
	}
	
	
	public static void solve(InputStream jsonInput, OutputStream jsonOutput) throws StreamReadException, DatabindException, IOException, CryptaModelException, CryptaSolverException {
		final ObjectMapper mapper = new ObjectMapper(); 
		final SolveInput input = mapper.readValue(jsonInput, SolveInput.class);
		SolveOutput output = solve(input);
		mapper.writeValue(jsonOutput, output);
	}
	
	public static InputStream buildJsonInputStream(SolveInput input) throws StreamWriteException, DatabindException, IOException {
		ObjectMapper mapper = new ObjectMapper(); 
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		mapper.writeValue(out, input);
		return new ByteArrayInputStream(out.toByteArray());
	}
	
	public static void main(String[] args) throws StreamWriteException, DatabindException, IOException, CryptaModelException, CryptaSolverException {
		// TODO What are the allowed handlers for REST ?
		JULogUtil.configureJsonLoggers();
		
		
		
		SolveInput input = new SolveInput("send+more=money", new CryptatorConfig());
		InputStream inputStream = buildJsonInputStream(input);
		
		solve(inputStream, System.out);
		
	}
}
