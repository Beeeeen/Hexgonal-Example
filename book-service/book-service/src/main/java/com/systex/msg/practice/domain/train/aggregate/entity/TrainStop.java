package com.systex.msg.practice.domain.train.aggregate.entity;

import java.time.LocalTime;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Where;

import com.systex.msg.base.domain.share.UUID;
import com.systex.msg.base.domain.share.YesNo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Setter
@Table(name = "train_stop")
@Where(clause = "delete_flag = 'N'")
public class TrainStop {

	@Id
	@GeneratedValue(generator = "uuid-generator")
	@GenericGenerator(name = "uuid-generator", parameters = @Parameter(name = "column", value = "u"), strategy = "com.systex.msg.base.domain.share.UUIDGenerator")
	private UUID uuid;

	@Transient
	private UUID u;

	@AttributeOverride(name = "value", column = @Column(name = "train_uuid"))
	private UUID trainUUID;

	private Integer seq;

	private String name;

	@Column(columnDefinition = "TIME")
	private LocalTime time;

	@Enumerated(EnumType.STRING)
	@Column(name = "delete_flag")
	private YesNo deleteFlag;

	public void create(UUID trainUUID, int seq, String name, String time) {
		this.u = new UUID();

		this.trainUUID = trainUUID;
		this.seq = seq;
		
		this.name = name;
		this.time = LocalTime.parse(time);
		
		this.deleteFlag = YesNo.N;
	}
	
}
