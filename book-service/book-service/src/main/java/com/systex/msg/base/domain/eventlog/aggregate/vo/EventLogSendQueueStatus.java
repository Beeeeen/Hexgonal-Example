package com.systex.msg.base.domain.eventlog.aggregate.vo;

public enum EventLogSendQueueStatus {

	INITIAL(0), SENDED(1);

	private final int value;

	private EventLogSendQueueStatus(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public boolean sameValueAs(EventLogSendQueueStatus other) {
		return other != null && this.equals(other);
	}	
}