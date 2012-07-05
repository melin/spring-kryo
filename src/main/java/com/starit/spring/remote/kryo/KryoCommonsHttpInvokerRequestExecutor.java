package com.starit.spring.remote.kryo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.remoting.httpinvoker.CommonsHttpInvokerRequestExecutor;
import org.springframework.remoting.support.RemoteInvocation;
import org.springframework.remoting.support.RemoteInvocationResult;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * @author bsli@starit.com.cn
 * @date 2012-7-5 下午2:57:11
 */
public class KryoCommonsHttpInvokerRequestExecutor extends CommonsHttpInvokerRequestExecutor {
	
	public String getContentType() {
		return "application/x-kryo";
	}
	
	@Override
	protected void writeRemoteInvocation(RemoteInvocation invocation, OutputStream os) throws IOException {
		Kryo kryo = new Kryo();
		kryo.register(RemoteInvocation.class);
		Output output = new Output(os);
		try {
			kryo.writeObject(output, invocation);
		}
		finally {
			output.close();
		}
	}
	
	@Override
	protected RemoteInvocationResult readRemoteInvocationResult(InputStream is, String codebaseUrl)
			throws IOException, ClassNotFoundException {
		Kryo kryo = new Kryo();
		kryo.register(KryoRemoteInvocationResult.class);
		Input input = new Input(is);
		try {
			KryoRemoteInvocationResult result = kryo.readObjectOrNull(input, KryoRemoteInvocationResult.class);
			return result;
		} finally {
			input.close();
		}
	}
}
