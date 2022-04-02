package jack.ledger.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

/**
 * Represents a block which holds transactions and the link to the previous block.
 */
public class Block {

    private static final Logger LOGGER = LoggerFactory.getLogger(Block.class);
    private static final Random random = new Random();

    private final boolean firstBlock;
    private int previousHash;
    private final Collection<Transaction> transactions;
    private int nonce;

    /**
     * Constructor for the non first block.
     *
     * @param previousHash The hash for the previous block
     */
    public Block(int previousHash) {
        this.firstBlock = false;
        this.previousHash = previousHash;
        transactions = new HashSet<>();
    }

    /**
     * Constructor for the first block.
     */
    public Block() {
        this.previousHash = 0;
        this.firstBlock = true;
        transactions = new HashSet<>();
    }

    /**
     * Add transaction.
     *
     * @param transaction transaction.
     */
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    /**
     * Get all transactions.
     *
     * @return All transactions.
     */
    public Collection<Transaction> getTransactions() {
        return transactions;
    }

    /**
     * Get the number of transactions.
     *
     * @return number of transactions.
     */
    public int getNumberOfTransactions() {
        return transactions.size();
    }

    /**
     * Get the hash for the transactions.
     *
     * @return has of the transactions with the block.
     */
    private int getTransactionsHash() {
        return transactions.hashCode();
    }

    /**
     * Get the previous hash.
     *
     * @return previous hash.
     */
    public int getPreviousHash() {
        return previousHash;
    }

    /**
     * Is first block?
     *
     * @return if first block.
     */
    public boolean isFirstBlock() {
        return firstBlock;
    }

    public Block setPreviousHash(int previousHash) {
        this.previousHash = previousHash;
        return this;
    }

    /**
     * Run the logic that calculates the nonce for the block.
     *
     * @return The nonce for the block (this does a full re-calculation).
     */
    public int calculateNonce() {
        LOGGER.info("Calculating hash for {} transactions", getNumberOfTransactions());
        HashSet<Integer> usedNumbers = new HashSet<>();
        for (int i = 0; i < Integer.MAX_VALUE; i++){
            this.nonce = random.nextInt();
            Optional<String> hashString = Chain.checkHash(this.hashCode());
            if (!usedNumbers.contains(this.nonce) && hashString.isPresent()) {
                LOGGER.info("Hash {} calculated for {} transactions", hashString.get(), getNumberOfTransactions());
                return this.nonce;
            }
            usedNumbers.add(this.nonce);
        }
        return -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Block block = (Block) o;
        return firstBlock == block.firstBlock && previousHash == block.previousHash
                && nonce == block.nonce && Objects.equals(transactions, block.transactions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstBlock, previousHash, transactions, nonce);
    }
}
