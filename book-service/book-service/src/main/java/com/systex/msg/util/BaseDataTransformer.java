package com.systex.msg.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.systex.msg.base.domain.PageAggregate;
import com.systex.msg.base.domain.command.CreateEventLogCommand;
import com.systex.msg.base.domain.outbound.BaseEvent;
import com.systex.msg.base.domain.share.CombineEntityConvertable;
import com.systex.msg.base.domain.share.UUID;
import com.systex.msg.base.iface.rest.dto.DataResource;
import com.systex.msg.base.iface.rest.dto.PageResource;
import com.systex.msg.base.iface.rest.dto.PageableResource;
import com.systex.msg.base.iface.rest.dto.UUIDResource;
import com.systex.msg.config.ContextHolder;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BaseDataTransformer {
	
	protected static final ModelMapper mapper = new ModelMapper();
	static {
		mapper.getConfiguration().setFullTypeMatchingRequired(true);
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
//		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
// 防止轉換 JPA Entity 時, 自動啟動 lazy fetching 機制：過濾掉 source 為 PersistentCollection 的情況
//		mapper.getConfiguration().setPropertyCondition(new Condition<Object, Object>() {
//			public boolean applies(MappingContext<Object, Object> context) {
//				return !(context.getSource() instanceof PersistentCollection);
//			}
//		});
		mapper.addConverter(new AbstractConverter<UUID, String>() {
			protected String convert(UUID source) {
				return source == null? null: source.getValue();
			}
		});
		mapper.addConverter(new AbstractConverter<String, UUID>() {
			protected UUID convert(String source) {
				return source == null? null: new UUID(source);
			}
		});
		mapper.addConverter(new AbstractConverter<UUID, UUIDResource>() {
			protected UUIDResource convert(UUID source) {
				return source == null? null: new UUIDResource(source.getValue());
			}
		});
		mapper.addConverter(new AbstractConverter<CombineEntityConvertable, DataResource>() {
			protected DataResource convert(CombineEntityConvertable source) {
				if (source == null)
					return null;
				else {
					DataResource target = new DataResource();
					target.setUuid(source.getUuid());
					target.setName(source.getName());
					return target;
				}
			}
		});
	}
	
	public static <S,D> void addConverter(Converter<S,D> converter, Class<S> sourceType, Class<D> destinationType) {
		mapper.addConverter(converter, sourceType, destinationType);
	}
	
	public static <T> T transformACL(Object response, Class<T> clazz) {
		return mapper.map(response, clazz);
	}

	public static <C> C transformDTO(Object resource, Class<C> clazz) {
		return mapper.map(resource, clazz);
	}
	
	public static <C> List<C> transformDTO(Iterable<?> aggregates, Class<C> clazz) {
		List<C> resources = new ArrayList<>();
		if (aggregates != null) {
			aggregates.forEach(aggregate -> resources.add(transformAggregate(aggregate, clazz)));
		}
		return resources;
	}

	public static <R> R transformAggregate(Object aggregate, Class<R> clazz) {
		return mapper.map(aggregate, clazz);
	}

	public static <R> List<R> transformAggregate(Iterable<?> aggregates, Class<R> clazz) {
		List<R> resources = new ArrayList<>();
		if (aggregates != null) {
			aggregates.forEach(aggregate -> resources.add(transformAggregate(aggregate, clazz)));
		}
		return resources;
	}

	public static <R> PageableResource<R> transformAggregatePageable(List<?> aggregates, Class<R> clazz) {
		List<R> list = new ArrayList<>();
		aggregates.forEach(aggregate -> list.add(transformAggregate(aggregate, clazz)));
		PageableResource<R> resource = new PageableResource<>();
		resource.setContent(list);
		return resource;
	}

	public static <R> PageableResource<R> transformAggregatePageable(PageAggregate<?> page, Class<R> clazz) {
		PageableResource<R> resource = transformAggregatePageable(page.getContent(), clazz);
		PageResource pageResource = new PageResource();
		pageResource.setTotalPages(page.getTotalPages());
		pageResource.setTotalElements(page.getTotalElements());
		pageResource.setSize(page.getSize());
		pageResource.setNumber(page.getNumber());
		pageResource.setNumberOfElements(page.getNumberOfElements());
		pageResource.setLast(page.isLast());
		pageResource.setFirst(page.isFirst());
		pageResource.setEmpty(page.isEmpty());
		resource.setPage(pageResource);
		return resource;
	}
	
	public static <T> CreateEventLogCommand transformEvent(T event) {
		String uuid = ContextHolder.getCurrentUser();
		if (uuid == null)
			uuid = "unknown"; // login不需帶token
		
		ObjectMapper mapper = new ObjectMapper();
		String jsonStr = "error!";
		try {
			jsonStr = mapper.writeValueAsString(event);
		} catch (JsonProcessingException e) {
			log.error("json fail", e);
		}
		
		String targetId = "targetId";
		if (event instanceof BaseEvent) {
			targetId = ((BaseEvent)event).getTargetId();
		}

		return new CreateEventLogCommand(uuid, event.getClass().getName(), new Date(), targetId, jsonStr);
	}
	
}
