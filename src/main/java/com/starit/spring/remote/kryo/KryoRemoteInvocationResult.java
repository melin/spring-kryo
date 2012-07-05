package com.starit.spring.remote.kryo;

import org.springframework.remoting.support.RemoteInvocationResult;

/**
 * @author bsli@starit.com.cn
 * @date 2012-7-5 下午3:51:21
 */
public class KryoRemoteInvocationResult extends RemoteInvocationResult {
	
	public KryoRemoteInvocationResult() {
		super(null);
	}

	public KryoRemoteInvocationResult(Object value) {
		super(value);
	}

}
