package com.systex.msg.config;

import com.systex.msg.base.domain.outbound.BaseEvent;

public class ContextHolder {
	
	private ContextHolder() {
	    throw new IllegalStateException("ContextHolder class");
	}

	private static final ThreadLocal<BaseEvent> EVENTLOG_HOLDER = new ThreadLocal<>();
	
	public static String getCurrentUser() {
		return "guest";
	}

	public static void setEvent(BaseEvent event) {
		EVENTLOG_HOLDER.set(event);
	}
	
	public static BaseEvent getEvent() {
		return EVENTLOG_HOLDER.get();
	}

	public static void cleanupEventLogHolder() {
		EVENTLOG_HOLDER.remove();
	}

}
