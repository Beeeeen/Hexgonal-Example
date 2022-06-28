package com.systex.msg.book.domain.coupon.aggregate;

import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.systex.msg.base.domain.BaseAggregate;
import com.systex.msg.base.domain.share.UUID;
import com.systex.msg.book.domain.coupon.command.UseCouponCommand;
import com.systex.msg.exception.ValidateFailedException;
import com.systex.msg.exception.ValidateFailedException.DomainErrorStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "book_coupon")
@NoArgsConstructor
public class Coupon extends BaseAggregate<Coupon> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Embedded
	@Getter
	@AttributeOverride(name = "value", column = @Column(name = "coupon_no"))
	private UUID couponNo; // Aggregate Identifier

	@Embedded
	@Getter
	@AttributeOverride(name = "value", column = @Column(name = "token"))
	private UUID token;

	@Embedded
	@Getter
	@AttributeOverride(name = "value", column = @Column(name = "used_to"))
	private UUID usedTo;
	
	public void checkUse(List<Object> arguments) throws ValidateFailedException {
		if (this.usedTo != null)
			throw new ValidateFailedException(DomainErrorStatus.COUPON_INVALID, "Coupon is already used");
	}
	
	public void use(UseCouponCommand command) {
		this.usedTo = command.getUsedTo();
	}
}
