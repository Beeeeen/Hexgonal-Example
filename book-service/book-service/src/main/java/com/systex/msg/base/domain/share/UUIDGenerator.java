package com.systex.msg.base.domain.share;

import java.io.Serializable;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

public class UUIDGenerator implements IdentifierGenerator, Configurable {

	private String column;

	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object obj) throws HibernateException {

		Object value;
		try {
			value = PropertyUtils.getProperty(obj, column);
		} catch (Exception e) {
			throw new HibernateException(e);
		}

		return UUID.class.cast(value);
	}

	@Override
	public void configure(Type type, Properties properties, ServiceRegistry serviceRegistry) throws MappingException {
		column = properties.getProperty("column");
	}
}
