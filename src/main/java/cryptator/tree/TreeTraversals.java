package cryptator.tree;

import java.util.Stack;
import java.util.function.ObjIntConsumer;

import cryptator.specs.ICryptaTree;
import cryptator.specs.ITraversalEdgeConsumer;
import cryptator.specs.ITraversalNodeConsumer;


public final class TreeTraversals {

	private TreeTraversals() {}
	
	static class TraversalEdge {
		
		public final ICryptaTree node;
		
		public final ICryptaTree father;
		
		public final int numFather;

		public TraversalEdge(ICryptaTree node, ICryptaTree father, int numFather) {
			super();
			this.node = node;
			this.father = father;
			this.numFather = numFather;
		}

		public final ICryptaTree getNode() {
			return node;
		}

		public final ICryptaTree getFather() {
			return father;
		}

		public final int getNumFather() {
			return numFather;
		}
		
		
	}




	public static void preorderTraversal(ICryptaTree root, ITraversalNodeConsumer traversalConsumer) {
		final Stack<ICryptaTree> stack = new Stack<ICryptaTree>();
		int num = 1;
		stack.push(root);
		while(! stack.isEmpty()) {
			final ICryptaTree n = stack.pop();
			traversalConsumer.accept(n, num);
			if(n.isInternalNode()) {
				stack.push(n.getRightChild());
				stack.push(n.getLeftChild());
			}
			num++;
		}	
	}
	
	private static void pushChildren(Stack<TraversalEdge> stack, ICryptaTree n, int num) {
		if(n.isInternalNode()) {
			stack.push(new TraversalEdge(n.getRightChild(), n, num));
			stack.push(new TraversalEdge(n.getLeftChild(), n, num));
		}
	}
	
	public static void preorderTraversal(ICryptaTree root, ITraversalEdgeConsumer traversalConsumer) {
		final Stack<TraversalEdge> stack = new Stack<TraversalEdge>();
		int num = 1;
		pushChildren(stack, root, num);
		while(! stack.isEmpty()) {
			num++;
			final TraversalEdge e = stack.pop();
			final ICryptaTree n = e.getNode();
			traversalConsumer.accept(n, num, e.father, e.numFather);
			pushChildren(stack, n, num);
		}	
	}


	public static void postorderTraversal(ICryptaTree root, ITraversalNodeConsumer traversalNodeConsumer) {

	}
	
	public static void inorderTraversal(ICryptaTree root, ITraversalNodeConsumer traversalNodeConsumer) {

	}	
}
