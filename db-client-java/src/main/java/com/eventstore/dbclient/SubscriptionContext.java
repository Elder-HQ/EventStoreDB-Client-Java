package com.eventstore.dbclient;

public class SubscriptionContext {
    private final long lastStreamPosition;
    private final long firstStreamPosition;
    private final Position lastAllStreamPosition;

    public SubscriptionContext(long firstStreamPosition, long lastStreamPosition, Position lastAllStreamPosition) {
        this.firstStreamPosition = firstStreamPosition;
        this.lastStreamPosition = lastStreamPosition;
        this.lastAllStreamPosition = lastAllStreamPosition;
    }

    public long getLastStreamPosition() {
        return lastStreamPosition;
    }

    public long getFirstStreamPosition() {
        return firstStreamPosition;
    }

    public Position getLastAllStreamPosition() {
        return lastAllStreamPosition;
    }
}
