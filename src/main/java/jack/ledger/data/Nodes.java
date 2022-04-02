package jack.ledger.data;

import jack.ledger.domain.Node;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashSet;

/**
 * Nodes class, this is used for other nodes on the network that will also be running the hash.
 */
@Repository
public class Nodes {

    private Collection<Node> nodes;

    /**
     * Constructor.
     */
    public Nodes() {
        nodes = new HashSet<>();
    }

    /**
     * Add node to the known network.
     *
     * @param node node to add.
     */
    public void addNode(Node node) {
        this.nodes.add(node);
    }

    /**
     * Add list of nodes.
     *
     * @param nodes nodes to add.
     */
    public void addNodes(Collection<Node> nodes) {
        this.nodes.addAll(nodes);
    }

    /**
     * Get all nodes this node knows.
     *
     * @return All nodes.
     */
    public Collection<Node> getNodes() {
        return this.nodes;
    }

}
