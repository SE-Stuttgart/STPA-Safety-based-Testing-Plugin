package xstampp.stpatcgenerator.model.generateTestCases;
/**
*
* @author asimabdulkhaleq
*/
public class Edge {
	private Vertex from;
    private int fromIndex;
    private Vertex to;
    private int toIndex;
    private int cost;
    private boolean visited = false;
    private String TranID;
    private String TransitonsAction;

    public String getTranID() {
        return TranID;
    }

    public void setTranID(String TranID) {
        this.TranID = TranID;
    }

    public int getFromIndex() {
        return fromIndex;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public Edge(Vertex from, int fromIndex, Vertex to, int toIndex, String TransitonsAction, String tranId) {
        this.from = from;
        this.fromIndex = fromIndex;
        this.to = to;
        this.toIndex = toIndex;
        this.TransitonsAction = TransitonsAction;
        this.TranID=tranId;
    }

    public void setFromIndex(int fromIndex) {
        this.fromIndex = fromIndex;
    }

    public int getToIndex() {
        return toIndex;
    }

    public void setToIndex(int toIndex) {
        this.toIndex = toIndex;
    }

    public boolean isMark() {
        return mark;
    }

    public void setMark(boolean mark) {
        this.mark = mark;
    }
    private boolean mark;

    /**
     * Create a zero cost edge between from and to
     *
     * @param from the starting vertex
     * @param to the ending vertex
     */
    public Edge(Vertex from, Vertex to, String transitionAction, String tranId) {
        this.from = from;
        this.to = to;
        this.TransitonsAction = transitionAction;
        this.TranID=tranId;
    }

    public Edge(Vertex from, Vertex to) {
        this(from, to, 0);
    }

    public String getTransitonsAction() {
        return TransitonsAction;
    }

    public void setTransitonsAction(String TransitonsAction) {
        this.TransitonsAction = TransitonsAction;
    }

    /**
     * Create an edge between from and to with the given cost.
     *
     * @param from the starting vertex
     * @param to the ending vertex
     * @param cost the cost of the edge
     */
    public Edge(Vertex from, Vertex to, int cost) {
        this.from = from;
        this.to = to;
        this.cost = cost;
        mark = false;
    }

    /**
     * Get the ending vertex
     *
     * @return ending vertex
     */
    public Vertex getTo() {
        return to;
    }

    /**
     * Get the starting vertex
     *
     * @return starting vertex
     */
    public Vertex getFrom() {
        return from;
    }

    /**
     * Get the cost of the edge
     *
     * @return cost of the edge
     */
    public int getCost() {
        return cost;
    }

    /**
     * Set the mark flag of the edge
     *
     */
    public void mark() {
        mark = true;
    }

    /**
     * Clear the edge mark flag
     *
     */
    public void clearMark() {
        mark = false;
    }

    /**
     * Get the edge mark flag
     *
     * @return edge mark flag
     */
    public boolean isMarked() {
        return mark;
    }

    /**
     * String rep of edge
     *
     * @return string rep with from/to vertex names and cost
     */
    public String toString() {
        StringBuffer tmp = new StringBuffer("Edge[from: ");
        tmp.append(from.getName());
        tmp.append(",to: ");
        tmp.append(to.getName());
        tmp.append(", cost: ");
        tmp.append(cost);
        tmp.append("]");
        return tmp.toString();
    }
}
