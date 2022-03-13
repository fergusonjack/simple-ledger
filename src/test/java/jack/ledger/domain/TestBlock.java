package jack.ledger.domain;

import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestBlock {

    private static final int PREVIOUS_HASH = 143131;

    @Test
    public void testHash() {
        Block block = new Block(PREVIOUS_HASH);
        Transaction tran1 = new Transaction();
        block.addTransaction(tran1);

        int nonce = block.calculateNonce();
        String binaryString = Integer.toBinaryString(Objects.hash(PREVIOUS_HASH, block.getTransactions(), nonce));
        assertThat(binaryString.substring(1, 15), is("00000000000000"));
    }

}
