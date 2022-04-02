package jack.ledger.core;

/**
 * Simple pair class.
 *
 * @param <T> first type.
 * @param <U> second type.
 */
public class Pair<T, U> {

    private T first;
    private U second;

    /**
     * Constructor
     *
     * @param first first.
     * @param second second.
     */
    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public U getSecond() {
        return second;
    }
}
