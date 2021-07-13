package cryptator;

import org.junit.Test;

import cryptator.tree.CryptaLeaf;
import cryptator.tree.CryptaNode;
import cryptator.tree.TreeUtils;

public class TreeTest {

	public TreeTest() {}
	
	@Test
	public void testTree() {
		CryptaNode l = new CryptaNode(CryptaOperator.ADD, new CryptaLeaf("send"), new CryptaLeaf("more"));
		CryptaNode t = new CryptaNode(CryptaOperator.EQ, l, new CryptaLeaf("money"));
		TreeUtils.toDotty(t, System.out);
		TreeUtils.writePreorder(t, System.out);
	}
}
