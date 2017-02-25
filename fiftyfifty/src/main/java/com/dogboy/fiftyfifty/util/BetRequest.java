package com.dogboy.fiftyfifty.util;

import java.util.UUID;

/**
 * all rights reserved Copyright (c) 2016 dogboy602k
 */
public class BetRequest {
    private UUID sender;
    private UUID recipient;
    private int bet;

    public BetRequest(UUID sender, UUID recipient, int bet) {
        this.sender = sender;
        this.recipient = recipient;
        this.bet = bet;
    }

    public UUID getSender() {
        return sender;
    }

    public void setSender(UUID sender) {
        this.sender = sender;
    }

    public UUID getRecipient() {
        return recipient;
    }

    public void setRecipient(UUID recipient) {
        this.recipient = recipient;
    }

    public int getBet() {
        return bet;
    }

    public void setBet(int bet) {
        this.bet = bet;
    }
}
