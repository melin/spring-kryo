package com.starit.spring.remote.kryo;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.remoting.support.RemoteInvocation;
import org.springframework.remoting.support.RemoteInvocationBasedExporter;
import org.springframework.remoting.support.RemoteInvocationResult;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.util.NestedServletException;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * @author bsli@starit.com.cn
 * @date 2012-7-5 下午1:48:33
 */
public class KryoHttpInvokerServiceExporter extends RemoteInvocationBasedExporter implements HttpRequestHandler, InitializingBean {
	public static final String CONTENT_TYPE_SERIALIZED_OBJECT = "application/x-kryo";

	private String contentType = CONTENT_TYPE_SERIALIZED_OBJECT;

	private Object proxy;
	
	public void handleRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			RemoteInvocation invocation = readRemoteInvocation(request);
			RemoteInvocationResult result = invokeAndCreateResult(invocation, getProxy());
			writeRemoteInvocationResult(request, response, result);
		}
		catch (ClassNotFoundException ex) {
			throw new NestedServletException("Class not found during deserialization", ex);
		}
	}

	protected RemoteInvocation readRemoteInvocation(HttpServletRequest request)
			throws IOException, ClassNotFoundException {
		Input input = new Input(request.getInputStream());
		try {
			Kryo kryo = new Kryo();
			kryo.register(RemoteInvocation.class);
			RemoteInvocation invocation = kryo.readObjectOrNull(input, RemoteInvocation.class);
			return invocation;
		} finally {
			input.close();
		}
	}

	protected void writeRemoteInvocationResult(
			HttpServletRequest request, HttpServletResponse response, RemoteInvocationResult result)
			throws IOException {

		response.setContentType(getContentType());
		Output output = new Output(response.getOutputStream());
		try {
			Kryo kryo = new Kryo();
			kryo.register(KryoRemoteInvocationResult.class);
			kryo.writeObject(output, result);
		} finally {
			output.close();
		}
	}
	
	@Override
	protected RemoteInvocationResult invokeAndCreateResult(RemoteInvocation invocation, Object targetObject) {
		try {
			Object value = invoke(invocation, targetObject);
			return new KryoRemoteInvocationResult(value);
		}
		catch (Throwable ex) {
			return new KryoRemoteInvocationResult(ex);
		}
	}

	public void setContentType(String contentType) {
		Assert.notNull(contentType, "'contentType' must not be null");
		this.contentType = contentType;
	}

	public String getContentType() {
		return this.contentType;
	}

	public void afterPropertiesSet() {
		prepare();
	}

	public void prepare() {
		this.proxy = getProxyForService();
	}

	protected final Object getProxy() {
		Assert.notNull(this.proxy, ClassUtils.getShortName(getClass()) + " has not been initialized");
		return this.proxy;
	}

}
