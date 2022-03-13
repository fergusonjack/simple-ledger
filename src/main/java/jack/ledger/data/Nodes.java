package jack.ledger.data;

import jack.ledger.domain.Node;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashSet;

@Repository
public class Nodes {

    private Collection<Node> nodes;

    public Nodes() {
        nodes = new HashSet<>();
    }

    public void addNode(Node node) {
        this.nodes.add(node);
    }

    public void addNodes(Collection<Node> nodes) {
        this.nodes.addAll(nodes);
    }

}
