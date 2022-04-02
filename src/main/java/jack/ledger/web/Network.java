package jack.ledger.web;

import jack.ledger.data.Nodes;
import jack.ledger.domain.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;

@RestController
public class Network {

    private Nodes nodes;

    @Autowired
    public Network(Nodes nodes) {
        this.nodes = nodes;
    }

    /**
     * Get all nodes that this node knows exist
     * @return Collection of nodes
     */
    @GetMapping("/nodes")
    public Collection<Node> nodes() {
        return this.nodes.;
    }

}
