package jack.ledger.web;

import jack.ledger.domain.Node;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;

@RestController
public class network {

    /**
     * Get all nodes that this node knows exist
     * @return Collection of nodes
     */
    @GetMapping("/nodes")
    public Collection<Node> nodes() {
        return new ArrayList<>();
    }

}
