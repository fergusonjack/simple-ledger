package jack.ledger.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * Transaction between two entities.
 */
@Getter
@Setter
public class Transaction {

    private Wallet debitWallet;
    private Wallet creditWallet;
    private double amount;

}
