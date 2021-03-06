package jack.ledger.web;

import jack.ledger.data.Nodes;
import jack.ledger.domain.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * Network controller, this is used to communicate with other nodes and allow new nodes on the network to be
 * communicated around other nods on the network.
 */
@RestController
public class Network {

    private final Nodes nodes;

    /**
     * Constructor.
     *
     * @param nodes The Nodes repository.
     */
    @Autowired
    public Network(Nodes nodes) {
        this.nodes = nodes;
    }

    /**
     * Get all nodes that this node knows exist.
     *
     * @return Collection of nodes.
     */
    @GetMapping("/nodes")
    public Collection<Node> nodes() {
        return this.nodes.getNodes();
    }

}
