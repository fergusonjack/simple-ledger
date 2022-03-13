package jack.ledger.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

public class Block {

    private static final Logger LOGGER = LoggerFactory.getLogger(Block.class);
    private static final Random random = new Random();

    private boolean firstBlock;
    private int previousHash;
    private final Collection<Transaction> transactions;
    private int nonce;


    public Block(int previousHash) {
        this.firstBlock = false;
        this.previousHash = previousHash;
        transactions = new HashSet<>();
    }

    public Block() {
        this.previousHash = 0;
        this.firstBlock = true;
        transactions = new HashSet<>();
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public Collection<Transaction> getTransactions() {
        return transactions;
    }

    public int getNumberOfTransactions() {
        return transactions.size();
    }

    private int getTransactionsHash() {
        return transactions.hashCode();
    }

    public int getPreviousHash() {
        return previousHash;
    }

    public boolean isFirstBlock() {
        return firstBlock;
    }

    public Block setPreviousHash(int previousHash) {
        this.previousHash = previousHash;
        return this;
    }

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
