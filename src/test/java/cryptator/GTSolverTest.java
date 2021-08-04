/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

import static cryptator.solver.SolverUtils.arrayIntVarToString;
import static cryptator.solver.SolverUtils.contraint;
import static cryptator.tree.TreeUtils.arrayVarToString;
import static cryptator.tree.TreeUtils.explorationRecursive;
import static cryptator.tree.TreeUtils.findSolCrypta;
import static cryptator.tree.TreeUtils.makeArray;
import static cryptator.tree.TreeUtils.writeInorder;
import static cryptator.tree.TreeUtils.writePostorder;
import static cryptator.tree.TreeUtils.writePreorder;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;
import org.junit.Before;
import org.junit.Test;

import cryptator.parser.CryptaParserException;
import cryptator.parser.CryptaParserWrapper;
import cryptator.solver.CryptaModel;
import cryptator.solver.CryptaModel2;
import cryptator.solver.CryptaSolution;
import cryptator.solver.Variable;
import cryptator.specs.ICryptaEvaluation;
import cryptator.specs.ICryptaNode;
import cryptator.tree.CryptaEvaluation;
import cryptator.tree.CryptaLeaf;
import cryptator.tree.CryptaNode;
import cryptator.tree.GraphizExporter;

public class GTSolverTest {

	public ICryptaNode sendMoreMoney;
	
	public ICryptaNode donaldGeraldRobert;
	
	public ICryptaNode bigCatLion;

	public ICryptaNode ABC;

	public ICryptaNode ABC2;

	HashMap<Character, Variable> abc = new HashMap<>();
	HashMap<Character, Variable> abcSol = new HashMap<>();
	ArrayList<IntVar> abcIntVarSol = new ArrayList<>();
	HashMap<Character, Variable>  sendMory = new HashMap<>();
	HashMap<Character, Variable>  sendMorySol = new HashMap<>();

	Model modelABC=new Model();


	@Before
	public void initializeTrees() {
		sendMoreMoney = new CryptaNode(CryptaOperator.EQ,
				new CryptaNode(CryptaOperator.ADD, new CryptaLeaf("send"), new CryptaLeaf("more")),
				new CryptaLeaf("money"));

		donaldGeraldRobert = new CryptaNode(CryptaOperator.EQ,
				new CryptaNode(CryptaOperator.ADD, new CryptaLeaf("donald"), new CryptaLeaf("gerald")),
				new CryptaLeaf("robert"));

		bigCatLion= new CryptaNode(CryptaOperator.EQ,
				new CryptaNode(CryptaOperator.ADD, new CryptaLeaf("big"), new CryptaLeaf("cat")),
				new CryptaLeaf("lion"));

		//AB + BA = CBC
		ABC= new CryptaNode(CryptaOperator.EQ,
				new CryptaNode(CryptaOperator.ADD, new CryptaLeaf("AB"), new CryptaLeaf("BA")),
				new CryptaLeaf("CBC"));

		ABC2= new CryptaNode(CryptaOperator.EQ,
				new CryptaNode(CryptaOperator.MUL, new CryptaLeaf("A"), new CryptaLeaf("B")),
				new CryptaLeaf("C"));

		abc.put('A', new Variable("A", 0, 0, 9));
		abc.put('B', new Variable("B", 0, 0, 9));
		abc.put('C', new Variable("C", 0, 0, 9));


		// La solution est A=9 B=2 et C=1.
		abcSol.put('A', new Variable("A", 9, 1, 9));
		abcSol.put('B', new Variable("B", 2, 1, 9));
		abcSol.put('C', new Variable("C", 1, 1, 9));

		//A=9, B=2, AB=92, B=1, A=0, BA=29, C=1, B=0, C=0, CBC=121
		abcIntVarSol.add(modelABC.intVar("A", 9));
		abcIntVarSol.add(modelABC.intVar("B", 2));
		abcIntVarSol.add(modelABC.intVar("AB", 92));
		abcIntVarSol.add(modelABC.intVar("BA", 29));
		abcIntVarSol.add(modelABC.intVar("C", 1));
		abcIntVarSol.add(modelABC.intVar("CBC", 121));


		// La solution est O = 0, M = 1, Y = 2, E = 5, N = 6, D = 7, R = 8 et S = 9.
		sendMory.put('o', new Variable("o", 0, 0, 9));
		sendMory.put('m', new Variable("m", 0, 1, 9));
		sendMory.put('y', new Variable("y", 0, 0, 9));
		sendMory.put('e', new Variable("e", 0, 0, 9));
		sendMory.put('n', new Variable("n", 0, 0, 9));
		sendMory.put('d', new Variable("d", 0, 0, 9));
		sendMory.put('r', new Variable("r", 0, 0, 9));
		sendMory.put('s', new Variable("s", 0, 1, 9));


		// La solution est O = 0, M = 1, Y = 2, E = 5, N = 6, D = 7, R = 8 et S = 9.
		sendMorySol.put('o', new Variable("o", 0, 0, 9));
		sendMorySol.put('m', new Variable("m", 1, 1, 9));
		sendMorySol.put('y', new Variable("y", 2, 0, 9));
		sendMorySol.put('e', new Variable("e", 5, 0, 9));
		sendMorySol.put('n', new Variable("n", 6, 0, 9));
		sendMorySol.put('d', new Variable("d", 7, 0, 9));
		sendMorySol.put('r', new Variable("r", 8, 0, 9));
		sendMorySol.put('s', new Variable("s", 9, 1, 9));


	}
	
	public GTSolverTest() {}

	@Test
	public void testTree() throws Exception {
		(new GraphizExporter()).print(sendMoreMoney, System.out);

		ByteArrayOutputStream os = new ByteArrayOutputStream();


		//preorder
		writePreorder(sendMoreMoney, os);
		assertEquals(os.toString(), "= + send more money ");

		os = new ByteArrayOutputStream();
		writePreorder(donaldGeraldRobert, os);
		assertEquals(os.toString(), "= + donald gerald robert ");

		os = new ByteArrayOutputStream();
		writePreorder(bigCatLion, os);
		assertEquals(os.toString(), "= + big cat lion ");


		//postorder
		os = new ByteArrayOutputStream();
		writePostorder(sendMoreMoney, os);
		assertEquals(os.toString(), "send more + money = ");

		os = new ByteArrayOutputStream();
		writePostorder(donaldGeraldRobert, os);
		assertEquals(os.toString(), "donald gerald + robert = ");

		os = new ByteArrayOutputStream();
		writePostorder(bigCatLion, os);
		assertEquals(os.toString(), "big cat + lion = ");


		//inorder
		os = new ByteArrayOutputStream();
		writeInorder(sendMoreMoney, os);
		assertEquals(os.toString(), "send + more = money ");

		os = new ByteArrayOutputStream();
		writeInorder(donaldGeraldRobert, os);
		assertEquals(os.toString(), "donald + gerald = robert ");

		os = new ByteArrayOutputStream();
		writeInorder(bigCatLion, os);
		assertEquals(os.toString(), "big + cat = lion ");



		ICryptaEvaluation chk = new CryptaEvaluation();
		int v = chk.evaluate(sendMoreMoney, new CryptaSolution(sendMorySol), 10);
		assertEquals(v,1);

//		for(Variable var: sendMorySol){
//			if(var.getName().equals(String.valueOf('y'))){
//				var.setValue(0);
//			}
//		}
		sendMorySol.get('y').setValue(0);
		v = chk.evaluate(sendMoreMoney, new CryptaSolution(sendMorySol), 10);
		assertEquals(v,0);
	}


	@Test
	public void testParse() throws CryptaParserException {
		CryptaParserWrapper parser = new CryptaParserWrapper();
		ICryptaNode node;

		node = parser.parse("send+more=money");
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		writeInorder(node, os);
		String n= os.toString();

		os = new ByteArrayOutputStream();
		writeInorder(sendMoreMoney, os);
		String smm= os.toString();

		assertEquals(n, smm);

//		CryptaParserException thrown = assertThrows(
//				CryptaParserException.class,
//				() -> {parser.parse("pppppppp + aaaaaaaaaaaaaaaaaaaaaaa = zzzzzzzzzzzzzzzzzzzzzzz");}
//		);
//
//		assertTrue(thrown.getMessage().contains("line 1:20 no viable alternative at input 'a'"));


//		node = parser.parse("pppppppp + aaaaaaaaaaaaaaaaaaaaaaa = zzzzzzzzzzzzzzzzzzzzzzz");
//		assertNull(node);
//
//		node = parser.parse("p1+di*&n2=z=z");
//		assertNull(node);
//
//		node = parser.parse("p1+di*+n2=z");
//		assertNull(node);

	}






	@Test
	public void incremental() throws Exception {
		long start = System.currentTimeMillis();
		//algo incremental
		ArrayList<Integer> tab2 = makeArray(abc.size());

		assertEquals(arrayVarToString(Objects.requireNonNull(explorationRecursive(tab2, abc, ABC, 10, 1))), arrayVarToString(abcSol));
		long end = System.currentTimeMillis();
		System.out.println("abc par incrementation: "+ String.valueOf(end-start) + "ms");

		start = System.currentTimeMillis();
		//algo incremental avec repetition
		tab2 = makeArray(abc.size());

		assertEquals(arrayVarToString(Objects.requireNonNull(explorationRecursive(tab2, abc, ABC2, 10, 2))), "A=0 B=2 C=0 ");
		end = System.currentTimeMillis();
		System.out.println("abc par incrementation avec repetition: "+ String.valueOf(end-start) + "ms");

		start = System.currentTimeMillis();
		//algo incremental
		ArrayList<Integer> tab3 = makeArray(sendMory.size());
		assertEquals(arrayVarToString(Objects.requireNonNull(explorationRecursive(tab3, sendMory, sendMoreMoney, 10, 1))), arrayVarToString(sendMorySol));
		end = System.currentTimeMillis();
		System.out.println("sendmory par incrementation: "+ String.valueOf(end-start) + "ms");
	}

	@Test
	public void heap() throws Exception {
		long start = System.currentTimeMillis();
		//algo heap
		ArrayList<Integer> comb= new ArrayList<>();
		for (int i=0; i<10; i++) {
			comb.add(i);
		}

		assertEquals(arrayVarToString(findSolCrypta(comb, 3, 10, abc, ABC, 1)), arrayVarToString(abcSol));
		long end = System.currentTimeMillis();
		System.out.println("abc par heap: "+ String.valueOf(end-start) + "ms");


		start = System.currentTimeMillis();
		//algo heap avec repetition
		comb= new ArrayList<>();
		for (int i=0; i<10; i++) {
			comb.add(i);
		}

		assertEquals(arrayVarToString(findSolCrypta(comb, 3, 10, abc, ABC2, 2)), "A=0 B=1 C=0 ");
		end = System.currentTimeMillis();
		System.out.println("abc par heap avec repetition: "+ String.valueOf(end-start) + "ms");

		start = System.currentTimeMillis();
		//algo heap
		comb= new ArrayList<>();
		for (int i=0; i<10; i++) {
			comb.add(i);
		}

		assertEquals(arrayVarToString(findSolCrypta(comb, sendMory.size(), 10, sendMory, sendMoreMoney, 1)), arrayVarToString(sendMorySol));
		end = System.currentTimeMillis();
		System.out.println("sendmory par heap: "+ String.valueOf(end-start) + "ms");

	}

	@Test
	public void choco() throws Exception {
		long start = System.currentTimeMillis();

		CryptaModel2 model= new CryptaModel2("Cryptarithme");

		contraint(ABC, model);
		Solver solver = model.getModel().getSolver();
		Solution solution=new Solution(model.getModel());
		solver.solve();
		assertEquals(arrayIntVarToString(model.getMap()), arrayIntVarToString(abcIntVarSol));

		long end = System.currentTimeMillis();
		System.out.println("abc par choco: "+ String.valueOf(end-start) + "ms");



	}




}


