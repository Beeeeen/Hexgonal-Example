package com.systex.msg.practice.domain.train.aggregate.vo;

import java.util.HashMap;
import java.util.Map;

public enum TrainKind {

	NOAH_ARK("A", "諾亞方舟號"),

	HOGWARTS("B", "霍格華茲號");

	private final String code;
	private final String label;

	private TrainKind(String code, String label) {
		this.code = code;
		this.label = label;
	}

	public String getCode() {
		return this.code;
	}
	public String getLabel() {
		return this.label;
	}

	private static final Map<String, TrainKind> labelToTypeMap = new HashMap<>();

	static {
		for (TrainKind type : TrainKind.values()) {
			labelToTypeMap.put(type.label, type);
		}
	}

	public static TrainKind fromLabel(String label) {
		return labelToTypeMap.get(label);
	}

}