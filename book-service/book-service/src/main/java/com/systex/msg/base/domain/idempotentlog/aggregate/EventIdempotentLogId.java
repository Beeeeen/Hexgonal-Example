package com.systex.msg.base.domain.idempotentlog.aggregate;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
@NoArgsConstructor

public class EventIdempotentLogId implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String eventType;
	private String uniqueKey;
	
	@Override
	public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((eventType == null) ? 0 : eventType.hashCode());
        result = prime * result + ((uniqueKey == null) ? 0 : uniqueKey.hashCode());
        return result;
    }
	
	@Override
	public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EventIdempotentLogId other = (EventIdempotentLogId) obj;
        if (eventType == null) {
            if (other.eventType != null)
                return false;
        } else if (!eventType.equals(other.eventType))
            return false;
        if (uniqueKey == null) {
            if (other.uniqueKey != null)
                return false;
        } else if (!uniqueKey.equals(other.uniqueKey))
            return false;
        return true;

	}
}
