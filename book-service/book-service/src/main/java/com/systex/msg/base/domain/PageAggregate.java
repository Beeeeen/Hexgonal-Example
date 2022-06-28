package com.systex.msg.base.domain;

import java.util.List;

import org.springframework.data.domain.Page;

import lombok.Getter;
import lombok.Setter;

@Getter
public class PageAggregate<A> {

	@Setter
	private List<A> content;
	
	private int totalPages;
	private long totalElements;
	private int size;
	private int number;
	private int numberOfElements;
	private boolean last;
	private boolean first;
	private boolean empty;	

	public PageAggregate(Page<A> page) {
		this(page, page.getContent());
	}

	public PageAggregate(Page<A> page, List<A> content) {
		this.content = content;
		this.totalPages = page.getTotalPages();
		this.totalElements = page.getTotalElements();
		this.size = page.getSize();
		this.number = page.getNumber();
		this.numberOfElements = page.getNumberOfElements();
		this.last = page.isLast();
		this.first = page.isFirst();
		this.empty = page.isEmpty();
	}
}
