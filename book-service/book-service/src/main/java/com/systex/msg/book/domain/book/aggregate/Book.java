package com.systex.msg.book.domain.book.aggregate;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.systex.msg.base.domain.BaseAggregate;
import com.systex.msg.base.domain.outbound.BaseEvent;
import com.systex.msg.base.domain.share.UUID;
import com.systex.msg.book.domain.book.aggregate.vo.BookVersion;
import com.systex.msg.book.domain.book.command.CreateBookCommand;
import com.systex.msg.book.domain.book.command.ReleaseBookCommand;
import com.systex.msg.book.domain.book.command.RenameBookCommand;
import com.systex.msg.book.domain.book.command.UpdateBookCommand;
import com.systex.msg.book.domain.book.outbound.BookCreatedEvent;
import com.systex.msg.book.domain.book.outbound.BookCreatedEventData;
import com.systex.msg.book.domain.book.outbound.BookUpdatedEvent;
import com.systex.msg.book.domain.book.outbound.BookUpdatedEventData;
import com.systex.msg.config.ContextHolder;
import com.systex.msg.exception.ValidateFailedException;
import com.systex.msg.exception.ValidateFailedException.DomainErrorStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Book extends BaseAggregate<Book> {

	@Id
	@GeneratedValue(generator = "uuid-generator")
	@GenericGenerator(name = "uuid-generator", parameters = @Parameter(name = "column", value = "u"), strategy = "com.systex.msg.base.domain.share.UUIDGenerator")
    private UUID uuid;
	
	@Transient
	private UUID u;
	
	private String name;

	private String author;

	private String isbn;
	
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinColumn(name = "book_uuid", updatable = false)
	private List<BookVersion> versions;

	/**
	 * Constructor Command Handler. registers Event
	 */
	public void create(CreateBookCommand command) {
		this.u = new UUID();

		this.name = command.getName();
		this.author = command.getAuthor();
		this.isbn = command.getIsbn();
		
		BookVersion version = new BookVersion(this.u, 0);
		this.versions = new ArrayList<>();
		this.versions.add(version);
	
		String couponNo = null;
		if (command.getCouponNo() != null) {
			couponNo = command.getCouponNo().getValue();
		}
		
		// 註冊 Domain Event（當有 Next Event 需要發佈時）
		BaseEvent event = BookCreatedEvent.builder().eventLogUuid(new UUID()).targetId(this.u.getValue())
				.data(new BookCreatedEventData(this.u.getValue(), couponNo)).build();
		ContextHolder.cleanupEventLogHolder();
		ContextHolder.setEvent(event);
		addDomainEvent(event);
	}
	
	// 透過 Aggregate check method 進行領域檢核
	public void checkUpdate(List<Object> arguments) throws ValidateFailedException {
		if (this.versions == null || this.versions.isEmpty())
			throw new ValidateFailedException(DomainErrorStatus.BOOK_VERSION_INVALID);
		if (this.versions.get(this.versions.size() - 1).getVersion().intValue() == 0)
			throw new ValidateFailedException(DomainErrorStatus.BOOK_VERSION_INVALID);
	}
	
	public void update(UpdateBookCommand command) {		
		this.name = command.getName();
		this.author = command.getAuthor();
		this.isbn = command.getIsbn();
		
		// 註冊 Domain Event（當有 Next Event 需要發佈時）
		BaseEvent event = BookUpdatedEvent.builder().eventLogUuid(new UUID()).targetId(this.uuid.getValue())
				.data(new BookUpdatedEventData(this.uuid.getValue())).build();
		ContextHolder.cleanupEventLogHolder();
		ContextHolder.setEvent(event);
		addDomainEvent(event);

	}
	
	public void release(ReleaseBookCommand command) {		
		BookVersion version = new BookVersion(this.uuid, this.versions.size());
		this.versions.add(version);
	}
	
	public void rename(RenameBookCommand command) {	
		if (StringUtils.isNotBlank(command.getName())) {
			this.name = command.getName();
			
			if (this.versions == null) {
				BookVersion version = new BookVersion(this.uuid, 0);
				this.versions = new ArrayList<>();
				this.versions.add(version);
			}
			BookVersion version = new BookVersion(this.uuid, this.versions.size() + 1);
			this.versions.add(version);
		}					
	}
	
}
