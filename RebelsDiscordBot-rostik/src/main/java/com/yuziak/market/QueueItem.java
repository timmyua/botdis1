package com.yuziak.market;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
public class QueueItem {

    private String name;
    private Long id;
    private Long sid;
    private BigInteger price;
    private Long liveAt;

    public boolean isAnother(QueueItem queueItem) {
        return !this.id.equals(queueItem.getId()) || !this.sid.equals(queueItem.getSid()) || !this.liveAt.equals(queueItem.getLiveAt());
    }
}


