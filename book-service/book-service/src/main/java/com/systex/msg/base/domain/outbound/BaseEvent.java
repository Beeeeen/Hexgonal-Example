package com.systex.msg.base.domain.outbound;

import com.systex.msg.base.domain.share.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
public abstract class BaseEvent {

	protected UUID eventLogUuid;

	protected String targetId;

}
