package jack.ledger.domain;

import jack.ledger.core.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static jack.ledger.data.Constant.ZEROS_LENGTH;

/**
 * A "Block"-Chain
 */
public class Chain {

    private static final Logger LOGGER = LoggerFactory.getLogger(Chain.class);

    // Create what is effectively a HashSet
    private final HashMap<Integer, Block> blocks = new HashMap<>();
    public HashMap<Block, Integer> chainLength = new HashMap<>();
    private HashSet<Block> endChainBlocks = new HashSet<>();

    public Chain(Block block) {
        blocks.put(block.hashCode(), block);
    }

    /**
     * Add block to the chain.
     *
     * @param block add a single block to a given chain.
     */
    public void addBlock(Block block) {
        // First verify the block
        if (Chain.checkHash(block.hashCode()).isPresent()) {
            // Check if we have previous block (for now if we don't we won't add in the future we might want to
            // ask other nodes for matching blocks for a hash)
            synchronized(this) {
                if (blocks.containsKey(block.getPreviousHash())) {
                    blocks.put(block.hashCode(), block);
                } else {
                    LOGGER.info("Tried to add block {} however couldn't find previous block for hash {}", block, block.getPreviousHash());
                }
            }
        } else {
            LOGGER.info("Received a block which has an invalid hash, something has gone wrong :( (block: {})", block);
        }
    }

    /**
     * Find the last block in a chain.
     *
     * @return Returns the last block.
     */
    public Block findLastBlock() {
        fullRefreshChainLength();
        HashMap<Integer, List<Block>> blockLength = this.chainLength.keySet().stream()
                .reduce(new HashMap<Integer, List<Block>>(), (acc, nextBlock) -> {
                    acc.compute(this.chainLength.get(nextBlock), (key, value) -> {
                        if (value == null) {
                            return List.of(nextBlock);
                        } else {
                            value.add(nextBlock);
                            return value;
                        }
                    });
                    return acc;
                }, (x,y) -> x);
        return blockLength.get(blockLength.keySet().stream().max(Integer::compare)).stream().findFirst().get();
    }

    /**
     * Full refresh of the chain length calculation.
     */
    public void fullRefreshChainLength() {
        this.chainLength = new HashMap<>();
        for (Block block : this.blocks.values()) {
            Pair<Integer, HashMap<Block, Integer>> chainLengthPair = findChainLength(block, this.chainLength);
            this.chainLength = chainLengthPair.getSecond();
            this.chainLength.put(block, chainLengthPair.getFirst());
        }
    }

    private Pair<Integer, HashMap<Block, Integer>> findChainLength(Block block, HashMap<Block, Integer> internalChainLength) {
        if (block.isFirstBlock()) {
            return new Pair<>(1, internalChainLength);
        } else if (internalChainLength.containsKey(block)) {
            return new Pair<>(internalChainLength.get(block), internalChainLength);
        } else {
            Pair<Integer, HashMap<Block, Integer>> internalChainLengthPartialPair =
                    findChainLength(this.blocks.get(block.getPreviousHash()), internalChainLength);
            HashMap<Block, Integer> internalChainLengthPartial = internalChainLengthPartialPair.getSecond();
            internalChainLengthPartial.put(block, 1 + internalChainLengthPartialPair.getFirst());
            return new Pair<>(internalChainLengthPartial.get(block), internalChainLengthPartial);
        }
    }

    /**
     * Get all blocks.
     *
     * @return A map of length to the last block in the chain
     */
    public HashMap<Integer, Block> getBlocks() {
        return blocks;
    }

    /**
     * Get the
     *
     * @return
     */
    public HashMap<Block, Integer> getChainLength() {
        return chainLength;
    }

    public static Optional<String> checkHash(int hashCode) {
        String hashString = Integer.toBinaryString(hashCode);
        int numZeros = 0;
        for (int i = 1; i < hashString.length() - 1; i++) {
            if (hashString.charAt(i) == '0') {
                numZeros++;
            } else {
                return numZeros == ZEROS_LENGTH ? Optional.of(hashString) : Optional.empty();
            }
        }
        return Optional.empty();
    }
}
