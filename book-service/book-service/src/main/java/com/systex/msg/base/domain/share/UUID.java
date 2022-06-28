package com.systex.msg.base.domain.share;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Aggregate Identifier for the Aggregate
 */
@ToString
@Embeddable
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class UUID implements Serializable {

	private static final long serialVersionUID = -4736096267454071379L;
	
	@Column(name = "uuid")
	private String value;

	public UUID() {
		this.value = java.util.UUID.randomUUID().toString().toUpperCase();
		this.value = this.value.replace("-", "");
	}

	public static String generateUUID() {
		UUID uuid = new UUID();
		return uuid.getValue();
	}
}
