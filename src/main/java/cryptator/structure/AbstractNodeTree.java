package structure;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import structure.NodeTree;

abstract public class AbstractNodeTree implements NodeTree {
    private String name;
    private NodeTree leftChildren;
    private NodeTree rightChildren;
    private String nodeName;

    public AbstractNodeTree(String name, NodeTree leftChildren, NodeTree rightChildren) {
        this.name = name;
        this.leftChildren = leftChildren;
        this.rightChildren = rightChildren;
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

    public void visualise() {

        try{
            String start = "digraph {\n";
            String end = "}";

            File file = new File("/Users/margauxschmied/Documents/cryptator/src/main/java/cryptator/dot/tree.dot");

            // créer le fichier s'il n'existe pas
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

    private void makeNode(BufferedWriter bw, NodeTree current, int i) throws IOException {
        if (current!=null){
            current.setNodeName("node_"+ i);
            bw.write(current.getNodeName()+" [label=\""+ current.getName() +"\"]\n");
            makeNode(bw, current.getLeftChildren(), i*2);
            makeNode(bw, current.getRightChildren(), i*2+1);
        }
    }

    private void makeLink(BufferedWriter bw, NodeTree current) throws IOException {
        if (current!=null && current.getLeftChildren()!=null && current.getRightChildren()!=null) {
            bw.write(current.getNodeName()+" -> "+ current.getLeftChildren().getNodeName() +" []\n");
            bw.write(current.getNodeName()+" -> "+ current.getRightChildren().getNodeName() +" []\n");
            makeLink(bw, current.getLeftChildren());
            makeLink(bw, current.getRightChildren());
        }
    }
}
