package structure;
import structure.AbstractNodeTree;
import structure.NodeTree;

public class Operation extends AbstractNodeTree{

    public Operation(String name, NodeTree leftChildren, NodeTree rightChildren) {
        super(name, leftChildren, rightChildren);
    }

    @Override
    public void calcul() {

    }

}
