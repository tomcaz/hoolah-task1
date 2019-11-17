package org.interview.hoolah.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Transactions  implements Serializable {
    String id;
    Date transDate;

    @Override
    public String toString() {
        return "Transactions{" +
                "id='" + id + '\'' +
                ", transDate=" + transDate +
                ", amount=" + amount +
                ", merchant='" + merchant + '\'' +
                ", type=" + type +
                ", PAYMENT=" + PAYMENT +
                ", REVERSAL=" + REVERSAL +
                ", relatedTransactionId='" + relatedTransactionId + '\'' +
                ", error=" + error +
                '}';
    }

    BigDecimal amount;
    String merchant;
    short type;
    short PAYMENT = 1;
    short REVERSAL = 2;
    String relatedTransactionId;

    boolean error = false;

    public Transactions(String id, Date transDate, BigDecimal amount,
                        String merchant,
                        String type,
                        String relatedTransactionId) throws Exception {
        this.id = id;
        this.amount = amount;
        this.merchant = merchant;
        type = type.replaceAll(" ",""); // removing space
        if (type.equalsIgnoreCase("payment")) {
            this.type = PAYMENT;
        } else if (type.equalsIgnoreCase("REVERSAL")) {
            this.type = REVERSAL;
        } else {
            this.type = -1;
            error = true;
            throw new Exception("Invalid TransactionType");
        }
        if (this.type == REVERSAL && (relatedTransactionId == null || relatedTransactionId.isEmpty())) { // validation for reversal record
            error = true;
            throw new Exception("Reversal has no related transaction id");
        }
        this.transDate = transDate;
    }

    public boolean hasError() {
        return error;
    }

    public Date getTransDate() {
        return transDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public boolean isReversal(){
        return type == REVERSAL;
    }
}
