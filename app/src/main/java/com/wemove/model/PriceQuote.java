package com.wemove.model;

import com.google.gson.annotations.SerializedName;

public class PriceQuote {
    @SerializedName("moveRequestId")
    private int moveRequestId;
    @SerializedName("movers")
    private UserDetails movers;
    @SerializedName("quoteStatus")
    private QuoteStatus quoteStatus;
    @SerializedName("price")
    private String price;

    public QuoteStatus getQuoteStatus() {
        return quoteStatus;
    }

    public void setQuoteStatus(QuoteStatus quoteStatus) {
        this.quoteStatus = quoteStatus;
    }

    public int getMoveRequestId() {
        return moveRequestId;
    }

    public void setMoveRequestId(int moveRequestId) {
        this.moveRequestId = moveRequestId;
    }

    public UserDetails getMovers() {
        return movers;
    }

    public void setMovers(UserDetails movers) {
        this.movers = movers;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
