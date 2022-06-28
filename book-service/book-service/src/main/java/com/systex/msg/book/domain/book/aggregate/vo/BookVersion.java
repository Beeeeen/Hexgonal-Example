package com.systex.msg.book.domain.book.aggregate.vo;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.systex.msg.base.domain.share.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@Entity
@Table(name = "book_version")
public class BookVersion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Getter
	@AttributeOverride(name = "value", column = @Column(name = "book_uuid"))
	private UUID bookUUID;

	@Getter
	private Integer version;

	public BookVersion(UUID bookUUID, Integer version) {
		this.bookUUID = bookUUID;
		this.version = version;
	}
}
