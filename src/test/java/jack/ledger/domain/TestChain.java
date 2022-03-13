package jack.ledger.domain;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.is;

import static org.hamcrest.MatcherAssert.assertThat;

public class TestChain {

    @Test
    public void testChainVerify() {
        Transaction tran1 = new Transaction();
        Transaction tran2 = new Transaction();
        Block block1 = new Block();
        block1.addTransaction(tran1);
        block1.addTransaction(tran2);
        block1.calculateNonce();

        // Create chain with single initial block
        Chain chain = new Chain(block1);

        Transaction tran3 = new Transaction();
        Transaction tran4 = new Transaction();
        Block block2 = new Block(block1.hashCode());
        block2.addTransaction(tran3);
        block2.addTransaction(tran4);
        block2.calculateNonce();

        Transaction tran5 = new Transaction();
        Transaction tran6 = new Transaction();
        Block block3 = new Block(block1.hashCode());
        block3.addTransaction(tran5);
        block3.addTransaction(tran6);
        block3.calculateNonce();

        Transaction tran7 = new Transaction();
        Transaction tran8 = new Transaction();
        Block block4 = new Block(block3.hashCode());
        block4.addTransaction(tran7);
        block4.addTransaction(tran8);
        block4.calculateNonce();

        chain.addBlock(block2);
        chain.addBlock(block3);
        chain.addBlock(block4);
        chain.fullRefreshChainLength();
        HashMap<Block, Integer> chainLength = chain.getChainLength();

        assertThat(1, is(chainLength.get(block1)));
        assertThat(2, is(chainLength.get(block2)));
        assertThat(2, is(chainLength.get(block3)));
        assertThat(3, is(chainLength.get(block4)));
    }

}
