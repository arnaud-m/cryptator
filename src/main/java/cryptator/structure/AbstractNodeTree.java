package cryptator.structure;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


abstract public class AbstractNodeTree implements NodeTree {
    private String name;
    private NodeTree leftChildren;
    private NodeTree rightChildren;
    private String nodeName; //the name associated to the dot node

    public AbstractNodeTree(String name, NodeTree leftChildren, NodeTree rightChildren) {
        this.name = name;
        this.leftChildren = leftChildren;
        this.rightChildren = rightChildren;
    }

    /**
     * function to visualize in dot the tree
     */
    public void visualise() {

        try{
            String start = "digraph {\n";
            String end = "}";

            File file = new File("./dot/tree.dot");

            //create a file if it does not exist
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(start);
            makeNode(bw, this, 1);
            makeLink(bw, this);
            bw.write(end);
            bw.close();

            System.out.println("Modification terminée!");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * write dot node corresponding to node of tree
     * @param bw where we write the dot
     * @param current the current node
     * @param i a number to differentiate nodes
     * @throws IOException if there is a probleme when we write in bw
     */

    private void makeNode(BufferedWriter bw, NodeTree current, int i) throws IOException {
        if (current!=null){
            current.setNodeName("node_"+ i);
            bw.write(current.getNodeName()+" [label=\""+ current.getName() +"\"]\n");
            makeNode(bw, current.getLeftChildren(), i*2);
            makeNode(bw, current.getRightChildren(), i*2+1);
        }
    }

    /**
     * write dot link between nodes of tree
     * @param bw where we write the dot
     * @param current the current node
     * @throws IOException if there is a probleme when we write in bw
     */

    private void makeLink(BufferedWriter bw, NodeTree current) throws IOException {
        if (current!=null && current.getLeftChildren()!=null && current.getRightChildren()!=null) {
            bw.write(current.getNodeName()+" -> "+ current.getLeftChildren().getNodeName() +" []\n");
            bw.write(current.getNodeName()+" -> "+ current.getRightChildren().getNodeName() +" []\n");
            makeLink(bw, current.getLeftChildren());
            makeLink(bw, current.getRightChildren());
        }
    }

    /**
     * resolve the cryptarithme
     */

    @Override
    public void calcul() {

    }

    public String getName() {
        return name;
    }

    public NodeTree getLeftChildren() {
        return leftChildren;
    }

    public NodeTree getRightChildren() {
        return rightChildren;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String toString(){
        StringBuilder s = new StringBuilder();
        if(this.getLeftChildren()!=null) {
            s.append("(");
            s.append(this.getLeftChildren().toString());
        }
        s.append(this.getName());
        if(this.getRightChildren()!=null) {
            s.append(this.getRightChildren().toString());
            s.append(")");
        }

        return s.toString();
    }
}
