/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import cryptator.solver.CryptaSolution;
import cryptator.solver.Variable;
import cryptator.specs.ICryptaEvaluation;
import cryptator.tree.*;
import org.junit.Before;
import org.junit.Test;

import cryptator.specs.ICryptaNode;

import static cryptator.tree.TreeUtils.*;
import static org.junit.Assert.*;

public class TreeTest {

	public ICryptaNode sendMoreMoney;
	
	public ICryptaNode donaldGeraldRobert;
	
	public ICryptaNode bigCatLion;

	public ICryptaNode ABC;

	public ICryptaNode ABC2;



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
	}
	
	public TreeTest() {}
	
	@Test
	public void testTree() throws Exception {
		(new GraphizExporter()).print(sendMoreMoney, System.out);

		//preorder
		assertEquals(writePreorder(sendMoreMoney, System.out), "= + send more money ");
		System.out.println();
		assertEquals(writePreorder(donaldGeraldRobert, System.out), "= + donald gerald robert ");
		System.out.println();
		assertEquals(writePreorder(bigCatLion, System.out), "= + big cat lion ");
		System.out.println();

		//postorder
		assertEquals(writePostorder(sendMoreMoney, System.out), "send more + money = ");
		System.out.println();
		assertEquals(writePostorder(donaldGeraldRobert, System.out), "donald gerald + robert = ");
		System.out.println();
		assertEquals(writePostorder(bigCatLion, System.out), "big cat + lion = ");
		System.out.println();

		//inorder
		assertEquals(writeInorder(sendMoreMoney, System.out), "send + more = money ");
		System.out.println();
		assertEquals(writeInorder(donaldGeraldRobert, System.out), "donald + gerald = robert ");
		System.out.println();
		assertEquals(writeInorder(bigCatLion, System.out), "big + cat = lion ");
		System.out.println();

		//evaluate
		ArrayList<Variable> abc = new ArrayList<>();
		abc.add(new Variable("A", 0, 0, 9));
		abc.add(new Variable("B", 0, 0, 9));
		abc.add(new Variable("C", 0, 0, 9));


		ArrayList<Variable> abcSol = new ArrayList<>();
		// La solution est A=9 B=2 et C=1.
		abcSol.add(new Variable("A", 9, 1, 9));
		abcSol.add(new Variable("B", 2, 1, 9));
		abcSol.add(new Variable("C", 1, 1, 9));

		long start = System.currentTimeMillis();
		//algo incremental
		ArrayList<Integer> tab2 = makeArray(abc.size());

		assertEquals(arrayVarToString(Objects.requireNonNull(explorationRecursive(tab2, abc, ABC, 10, 1))), arrayVarToString(abcSol));
		long end = System.currentTimeMillis();
		System.out.println("abc par incrementation: "+ String.valueOf(end-start) + "ms");


		start = System.currentTimeMillis();
		//algo heap
		ArrayList<Integer> comb= new ArrayList<>();
		for (int i=0; i<10; i++) {
			comb.add(i);
		}

		assertEquals(arrayVarToString(findSolCrypta(comb, 3, 10, abc, ABC, 1)), arrayVarToString(abcSol));
		end = System.currentTimeMillis();
		System.out.println("abc par heap: "+ String.valueOf(end-start) + "ms");

		start = System.currentTimeMillis();
		//algo incremental avec repetition
		tab2 = makeArray(abc.size());

		assertEquals(arrayVarToString(Objects.requireNonNull(explorationRecursive(tab2, abc, ABC2, 10, 2))), "A=0 B=2 C=0 ");
		end = System.currentTimeMillis();
		System.out.println("abc par incrementation avec repetition: "+ String.valueOf(end-start) + "ms");


		start = System.currentTimeMillis();
		//algo heap avec repetition
		comb= new ArrayList<>();
		for (int i=0; i<10; i++) {
			comb.add(i);
		}

		assertEquals(arrayVarToString(findSolCrypta(comb, 3, 10, abc, ABC2, 2)), "A=0 B=1 C=0 ");
		end = System.currentTimeMillis();
		System.out.println("abc par heap avec repetition: "+ String.valueOf(end-start) + "ms");


		ArrayList<Variable> sendMory = new ArrayList<>();
		// La solution est O = 0, M = 1, Y = 2, E = 5, N = 6, D = 7, R = 8 et S = 9.
		sendMory.add(new Variable("o", 0, 0, 9));
		sendMory.add(new Variable("m", 0, 1, 9));
		sendMory.add(new Variable("y", 0, 0, 9));
		sendMory.add(new Variable("e", 0, 0, 9));
		sendMory.add(new Variable("n", 0, 0, 9));
		sendMory.add(new Variable("d", 0, 0, 9));
		sendMory.add(new Variable("r", 0, 0, 9));
		sendMory.add(new Variable("s", 0, 1, 9));

		ArrayList<Variable> sendMorySol = new ArrayList<>();
		// La solution est O = 0, M = 1, Y = 2, E = 5, N = 6, D = 7, R = 8 et S = 9.
		sendMorySol.add(new Variable("o", 0, 0, 9));
		sendMorySol.add(new Variable("m", 1, 1, 9));
		sendMorySol.add(new Variable("y", 2, 0, 9));
		sendMorySol.add(new Variable("e", 5, 0, 9));
		sendMorySol.add(new Variable("n", 6, 0, 9));
		sendMorySol.add(new Variable("d", 7, 0, 9));
		sendMorySol.add(new Variable("r", 8, 0, 9));
		sendMorySol.add(new Variable("s", 9, 1, 9));

		start = System.currentTimeMillis();
		//algo incremental
		ArrayList<Integer> tab3 = makeArray(sendMory.size());
		assertEquals(arrayVarToString(Objects.requireNonNull(explorationRecursive(tab3, sendMory, sendMoreMoney, 10, 1))), arrayVarToString(sendMorySol));
		end = System.currentTimeMillis();
		System.out.println("sendmory par incrementation: "+ String.valueOf(end-start) + "ms");

		start = System.currentTimeMillis();
		//algo heap
		comb= new ArrayList<>();
		for (int i=0; i<10; i++) {
			comb.add(i);
		}

		assertEquals(arrayVarToString(findSolCrypta(comb, sendMory.size(), 10, sendMory, sendMoreMoney, 1)), arrayVarToString(sendMorySol));
		end = System.currentTimeMillis();
		System.out.println("sendmory par heap: "+ String.valueOf(end-start) + "ms");


		ICryptaEvaluation chk = new CryptaEvaluation();
		int v = chk.evaluate(sendMoreMoney, new CryptaSolution(sendMory), 10);
		assertEquals(v,1);
		System.out.println(v);

		for(Variable var: sendMory){
			if(var.getName().equals(String.valueOf('y'))){
				var.setValue(0);
			}
		}
		v = chk.evaluate(sendMoreMoney, new CryptaSolution(sendMory), 10);
		assertEquals(v,0);
		System.out.println(v);
	}


	@Test
	public void testParse() {
		CryptaParserWrapper parser = new CryptaParserWrapper();
		ICryptaNode node;

		node = parser.parse("send+more=money");
		assertEquals(TreeUtils.writeInorder(node, System.out), TreeUtils.writeInorder(sendMoreMoney, System.out));

		node = parser.parse("pppppppp + aaaaaaaaaaaaaaaaaaaaaaa = zzzzzzzzzzzzzzzzzzzzzzz");
		assertNull(node);

		node = parser.parse("p1+di*&n2=z=z");
		assertNull(node);

		node = parser.parse("p1+di*+n2=z");
		assertNull(node);

	}




}
