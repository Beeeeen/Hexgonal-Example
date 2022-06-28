package com.systex.msg.base.domain;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cglib.proxy.Callback;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.cglib.proxy.NoOp;

import com.systex.msg.exception.ServiceRuntimeException;
import com.systex.msg.exception.ValidateFailedException;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class AggregateProxyFactory {

	private static AggregateProxyFactory instance = new AggregateProxyFactory();
	private static final String CHECKER_METHOD = "check";
	
	public static <T> T getProxyInstance(Class<T> targetType, T target) {
		
		if (target == null) {
			throw new ServiceRuntimeException("target is null");
		}
		
		Enhancer en = new Enhancer();
		en.setSuperclass(targetType);
		en.setCallbacks(new Callback[] { instance.new AggregateMethodInterceptor(target), NoOp.INSTANCE });
		en.setCallbackFilter(method -> {
			String name = method.getName();
			if (name.startsWith("get") || name.startsWith("set") || name.equals("addDomainEvent") || name.startsWith(CHECKER_METHOD))
				return 1;
			else
				return 0;
		});
		return targetType.cast(en.create());
	}

	@AllArgsConstructor
	class AggregateMethodInterceptor implements MethodInterceptor {
		
		private Object target;
		
		@Override
		public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {

			log.debug("start intercept");
			String methodName = method.getName();
			if (!methodName.startsWith(CHECKER_METHOD))
				methodName = CHECKER_METHOD + methodName.substring(0, 1).toUpperCase() + methodName.substring(1);

			log.debug("call {} method to start validating", methodName);
			// 動態呼叫檢核方法，若方法不存在，表示不需要先檢核 
			try {
				Method check = obj.getClass().getMethod(methodName, getParameterTypes(args));
				check.invoke(target, getParameterList(args));
			} catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException e) {
				log.debug("Undefined check method: {}, ignore. ex: {}", methodName, e.getMessage());
			} catch (InvocationTargetException e) {
				if (e.getCause() instanceof ValidateFailedException)
					throw (ValidateFailedException) e.getCause();
				else
					throw new ServiceRuntimeException(e.getCause().getMessage());
			}
			
			log.debug("after validation is successful, call the command handler: {}", proxy.getSignature().getName());
//			Object returnValue = proxy.invokeSuper(obj, args); // proxy is empty shell
			Object returnValue = method.invoke(target, args);
			
			log.debug("end intercept");
			return returnValue;

		}

		@SuppressWarnings("rawtypes")
		private Class[] getParameterTypes(Object...parameters) {
			List<Class> list = new ArrayList<>();
			if (parameters.length > 0)
				list.add(List.class);
			
			Class[] result = new Class[list.size()];
	        result = list.toArray(result);
			return result;
		}		

		private Object[] getParameterList(Object...parameters) {
			List<Object> list = new ArrayList<>();
			list.addAll(Arrays.stream(parameters).collect(Collectors.toList()));
			
			List<Object> args = new ArrayList<>();
			if (parameters.length > 0)
				args.add(list);
			
			Object[] result = new Object[args.size()];
	        result = args.toArray(result);
			return result;
		}
	}

}
