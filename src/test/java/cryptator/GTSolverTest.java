///**
// * This file is part of cryptator, https://github.com/arnaud-m/cryptator
// *
// * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
// *
// * Licensed under the BSD 3-clause license.
// * See LICENSE file in the project root for full license information.
// */
//package cryptator;
//
//import static cryptator.tree.TreeUtils.writeInorder;
//import static cryptator.tree.TreeUtils.writePostorder;
//import static cryptator.tree.TreeUtils.writePreorder;
//import static org.junit.Assert.assertEquals;
//
//import java.io.ByteArrayOutputStream;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Objects;
//
//import cryptator.solver.gentest.*;
//import cryptator.specs.ICryptaSolution;
//import org.chocosolver.solver.Model;
//import org.chocosolver.solver.Solution;
//import org.chocosolver.solver.Solver;
//import org.chocosolver.solver.variables.IntVar;
//import org.junit.Before;
//import org.junit.Ignore;
//import org.junit.Test;
//
//import cryptator.parser.CryptaParserException;
//import cryptator.parser.CryptaParserWrapper;
//import cryptator.specs.ICryptaEvaluation;
//import cryptator.specs.ICryptaNode;
//import cryptator.tree.CryptaEvaluation;
//import cryptator.tree.CryptaLeaf;
//import cryptator.tree.CryptaNode;
//import cryptator.tree.GraphizExporter;
//
//public class GTSolverTest {
//
//	public ICryptaNode sendMoreMoney;
//
//	public ICryptaNode donaldGeraldRobert;
//
//	public ICryptaNode bigCatLion;
//
//	public ICryptaNode ABC;
//
//	public ICryptaNode ABC2;
//
//	HashMap<Character, GTVariable> abc = new HashMap<>();
//	HashMap<Character, GTVariable> abcSol = new HashMap<>();
//	ArrayList<IntVar> abcIntVarSol = new ArrayList<>();
//	HashMap<Character, GTVariable>  sendMory = new HashMap<>();
//	HashMap<Character, GTVariable>  sendMorySol = new HashMap<>();
//
//	Model modelABC=new Model();
//
//
//	@Before
//	public void initializeTrees() {
//		sendMoreMoney = new CryptaNode(CryptaOperator.EQ,
//				new CryptaNode(CryptaOperator.ADD, new CryptaLeaf("send"), new CryptaLeaf("more")),
//				new CryptaLeaf("money"));
//
//		donaldGeraldRobert = new CryptaNode(CryptaOperator.EQ,
//				new CryptaNode(CryptaOperator.ADD, new CryptaLeaf("donald"), new CryptaLeaf("gerald")),
//				new CryptaLeaf("robert"));
//
//		bigCatLion= new CryptaNode(CryptaOperator.EQ,
//				new CryptaNode(CryptaOperator.ADD, new CryptaLeaf("big"), new CryptaLeaf("cat")),
//				new CryptaLeaf("lion"));
//
//		//AB + BA = CBC
//		ABC= new CryptaNode(CryptaOperator.EQ,
//				new CryptaNode(CryptaOperator.ADD, new CryptaLeaf("AB"), new CryptaLeaf("BA")),
//				new CryptaLeaf("CBC"));
//
//		ABC2= new CryptaNode(CryptaOperator.EQ,
//				new CryptaNode(CryptaOperator.MUL, new CryptaLeaf("A"), new CryptaLeaf("B")),
//				new CryptaLeaf("C"));
//
//		abc.put('A', new GTVariable("A", 0, 0, 9));
//		abc.put('B', new GTVariable("B", 0, 0, 9));
//		abc.put('C', new GTVariable("C", 0, 0, 9));
//
//
//		// La solution est A=9 B=2 et C=1.
//		abcSol.put('A', new GTVariable("A", 9, 1, 9));
//		abcSol.put('B', new GTVariable("B", 2, 1, 9));
//		abcSol.put('C', new GTVariable("C", 1, 1, 9));
//
//		//A=9, B=2, AB=92, B=1, A=0, BA=29, C=1, B=0, C=0, CBC=121
//		abcIntVarSol.add(modelABC.intVar("A", 9));
//		abcIntVarSol.add(modelABC.intVar("B", 2));
//		abcIntVarSol.add(modelABC.intVar("AB", 92));
//		abcIntVarSol.add(modelABC.intVar("BA", 29));
//		abcIntVarSol.add(modelABC.intVar("C", 1));
//		abcIntVarSol.add(modelABC.intVar("CBC", 121));
//
//
//		// La solution est O = 0, M = 1, Y = 2, E = 5, N = 6, D = 7, R = 8 et S = 9.
//		sendMory.put('o', new GTVariable("o", 0, 0, 9));
//		sendMory.put('m', new GTVariable("m", 0, 1, 9));
//		sendMory.put('y', new GTVariable("y", 0, 0, 9));
//		sendMory.put('e', new GTVariable("e", 0, 0, 9));
//		sendMory.put('n', new GTVariable("n", 0, 0, 9));
//		sendMory.put('d', new GTVariable("d", 0, 0, 9));
//		sendMory.put('r', new GTVariable("r", 0, 0, 9));
//		sendMory.put('s', new GTVariable("s", 0, 1, 9));
//
//
//		// La solution est O = 0, M = 1, Y = 2, E = 5, N = 6, D = 7, R = 8 et S = 9.
//		sendMorySol.put('o', new GTVariable("o", 0, 0, 9));
//		sendMorySol.put('m', new GTVariable("m", 1, 1, 9));
//		sendMorySol.put('y', new GTVariable("y", 2, 0, 9));
//		sendMorySol.put('e', new GTVariable("e", 5, 0, 9));
//		sendMorySol.put('n', new GTVariable("n", 6, 0, 9));
//		sendMorySol.put('d', new GTVariable("d", 7, 0, 9));
//		sendMorySol.put('r', new GTVariable("r", 8, 0, 9));
//		sendMorySol.put('s', new GTVariable("s", 9, 1, 9));
//
//
//	}
//
//	public GTSolverTest() {}
//
//	@Test
//	public void testTree() throws Exception {
//		(new GraphizExporter()).print(sendMoreMoney, System.out);
//
//		ByteArrayOutputStream os = new ByteArrayOutputStream();
//
//
//		//preorder
//		writePreorder(sendMoreMoney, os);
//		assertEquals(os.toString(), "= + send more money ");
//
//		os = new ByteArrayOutputStream();
//		writePreorder(donaldGeraldRobert, os);
//		assertEquals(os.toString(), "= + donald gerald robert ");
//
//		os = new ByteArrayOutputStream();
//		writePreorder(bigCatLion, os);
//		assertEquals(os.toString(), "= + big cat lion ");
//
//
//		//postorder
//		os = new ByteArrayOutputStream();
//		writePostorder(sendMoreMoney, os);
//		assertEquals(os.toString(), "send more + money = ");
//
//		os = new ByteArrayOutputStream();
//		writePostorder(donaldGeraldRobert, os);
//		assertEquals(os.toString(), "donald gerald + robert = ");
//
//		os = new ByteArrayOutputStream();
//		writePostorder(bigCatLion, os);
//		assertEquals(os.toString(), "big cat + lion = ");
//
//
//		//inorder
//		os = new ByteArrayOutputStream();
//		writeInorder(sendMoreMoney, os);
//		assertEquals(os.toString(), "send + more = money ");
//
//		os = new ByteArrayOutputStream();
//		writeInorder(donaldGeraldRobert, os);
//		assertEquals(os.toString(), "donald + gerald = robert ");
//
//		os = new ByteArrayOutputStream();
//		writeInorder(bigCatLion, os);
//		assertEquals(os.toString(), "big + cat = lion ");
//
//		ICryptaEvaluation chk = new CryptaEvaluation();
//		int v = chk.evaluate(sendMoreMoney, new CryptaGTSolution(sendMorySol), 10);
//		assertEquals(v,1);
//
//		sendMorySol.get('y').setValue(0);
//		v = chk.evaluate(sendMoreMoney, new CryptaGTSolution(sendMorySol), 10);
//		assertEquals(v,0);
//	}
//
//
//	@Test
//	public void testParse() throws CryptaParserException {
//		CryptaParserWrapper parser = new CryptaParserWrapper();
//		ICryptaNode node;
//
//		node = parser.parse("send+more=money");
//		ByteArrayOutputStream os = new ByteArrayOutputStream();
//		writeInorder(node, os);
//		String n= os.toString();
//
//		os = new ByteArrayOutputStream();
//		writeInorder(sendMoreMoney, os);
//		String smm= os.toString();
//
//		assertEquals(n, smm);
//
////		CryptaParserException thrown = assertThrows(
////				CryptaParserException.class,
////				() -> {parser.parse("pppppppp + aaaaaaaaaaaaaaaaaaaaaaa = zzzzzzzzzzzzzzzzzzzzzzz");}
////		);
////
////		assertTrue(thrown.getMessage().contains("line 1:20 no viable alternative at input 'a'"));
//
//
////		node = parser.parse("pppppppp + aaaaaaaaaaaaaaaaaaaaaaa = zzzzzzzzzzzzzzzzzzzzzzz");
////		assertNull(node);
////
////		node = parser.parse("p1+di*&n2=z=z");
////		assertNull(node);
////
////		node = parser.parse("p1+di*+n2=z");
////		assertNull(node);
//
//	}
//
//
//
//
//
//
//	@Test
//	public void incremental() throws Exception {
//		long start = System.currentTimeMillis();
//		//algo incremental
//		CryptaGTSolution sol=new CryptaGTSolution(abc);
//		CryptaIncrSolver GTSolver= new CryptaIncrSolver(ABC, sol);
//		ArrayList<Integer> tab2 = GTSolverTest.makeArray(abc.size());
//
//		assertEquals(GTSolverTest.arrayVarToString(Objects.requireNonNull(GTSolver.incrementalSolve(tab2, 10, 1))), GTSolverTest.arrayVarToString(abcSol));
//		long end = System.currentTimeMillis();
//		System.out.println("abc par incrementation: "+ (end - start) + "ms");
//
//		start = System.currentTimeMillis();
//		//algo incremental avec repetition
//		GTSolver.setRoot(ABC2);
//
//		tab2 = GTSolverTest.makeArray(abc.size());
//
//		assertEquals(GTSolverTest.arrayVarToString(Objects.requireNonNull(GTSolver.incrementalSolve(tab2, 10, 2))), "A=0 B=2 C=0 ");
//		end = System.currentTimeMillis();
//		System.out.println("abc par incrementation avec repetition: "+ (end - start) + "ms");
//
//		start = System.currentTimeMillis();
//		//algo incremental
//		GTSolver.setRoot(sendMoreMoney);
//		sol = (CryptaGTSolution) GTSolver.getSolution();
//		sol.setSymbolToDigit(sendMory);
//		ArrayList<Integer> tab3 = GTSolverTest.makeArray(sendMory.size());
//		assertEquals(GTSolverTest.arrayVarToString(Objects.requireNonNull(GTSolver.incrementalSolve(tab3, 10, 1))), GTSolverTest.arrayVarToString(sendMorySol));
//		end = System.currentTimeMillis();
//		System.out.println("sendmory par incrementation: "+ (end - start) + "ms");
//	}
//
//	@Test
//	public void heap() throws Exception {
//		long start = System.currentTimeMillis();
//		//algo heap
//		ArrayList<Integer> comb= new ArrayList<>();
//		for (int i=0; i<10; i++) {
//			comb.add(i);
//		}
//		CryptaGTSolution sol= new CryptaGTSolution(abc);
//		CryptaHeapSolver GTSolver= new CryptaHeapSolver(ABC, sol);
//		assertEquals(GTSolverTest.arrayVarToString(GTSolver.findSolCrypta(comb, 10, 1)), GTSolverTest.arrayVarToString(abcSol));
//		long end = System.currentTimeMillis();
//		System.out.println("abc par heap: "+ (end - start) + "ms");
//
//
//		start = System.currentTimeMillis();
//		//algo heap avec repetition
//		comb= new ArrayList<>();
//		for (int i=0; i<10; i++) {
//			comb.add(i);
//		}
//		GTSolver.setRoot(ABC2);
//		assertEquals(GTSolverTest.arrayVarToString(GTSolver.findSolCrypta(comb, 10, 2)), "A=0 B=1 C=0 ");
//		end = System.currentTimeMillis();
//		System.out.println("abc par heap avec repetition: "+ (end - start) + "ms");
//
//		start = System.currentTimeMillis();
//		//algo heap
//		comb= new ArrayList<>();
//		for (int i=0; i<10; i++) {
//			comb.add(i);
//		}
//		GTSolver.setRoot(sendMoreMoney);
//		sol = (CryptaGTSolution) GTSolver.getSolution();
//		sol.setSymbolToDigit(sendMory);
//		assertEquals(GTSolverTest.arrayVarToString(GTSolver.findSolCrypta(comb, 10, 1)), GTSolverTest.arrayVarToString(sendMorySol));
//		end = System.currentTimeMillis();
//		System.out.println("sendmory par heap: "+ (end - start) + "ms");
//
//	}
//
////	@Test
////	@Ignore
////	public void choco() throws Exception {
////		long start = System.currentTimeMillis();
////
////		CryptaGTModel model= new CryptaGTModel("Cryptarithme");
////		CryptaIncrSolver GTsolver = new CryptaIncrSolver(ABC);
////		GTsolver.contraint(model);
////		Solver solver = model.getModel().getSolver();
////		Solution solution=new Solution(model.getModel());
////		solver.solve();
////		// FIXME assertEquals(arrayVarToString(model.getMap()), arrayVarToString(abcIntVarSol));
////
////		long end = System.currentTimeMillis();
////		System.out.println("abc par choco: "+ (end - start) + "ms");
////
////	}
//
//	public static String arrayVarToString(HashMap<Character, GTVariable> map) {
//		StringBuilder sb=new StringBuilder();
//		if(map!=null) {
//			for (GTVariable var : map.values()) {
//				sb.append(var.getName());
//				sb.append("=");
//				sb.append(var.getValue());
//				sb.append(" ");
//			}
//		}
//		return sb.toString();
//	}
//
//
//
//
//
//
//
//}
//
//
