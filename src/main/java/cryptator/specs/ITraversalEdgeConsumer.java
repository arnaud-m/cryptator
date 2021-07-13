package cryptator.specs;

public interface ITraversalEdgeConsumer {
	
	void accept(ICryptaTree node, int numNode, ICryptaTree father, int numFather);
}