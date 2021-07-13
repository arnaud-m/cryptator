package cryptator.structure;

public interface NodeTree {
    public void calcul();
    public void visualise();
    public String getName();
    public NodeTree getLeftChildren();
    public NodeTree getRightChildren();
    public String getNodeName();
    public void setNodeName(String nodeName);
}
