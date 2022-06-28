package com.systex.msg.base.domain.share;

public enum YesNo {

	// 是
	Y,

	// 否
	N;

	public static YesNo valueOf(String value, YesNo def) {
		return (value == null) ? def : YesNo.valueOf(value);
	}

}
