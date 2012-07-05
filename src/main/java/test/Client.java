package test;

import org.springframework.remoting.httpinvoker.CommonsHttpInvokerRequestExecutor;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;
import org.springframework.util.StopWatch;

import com.starit.spring.remote.kryo.KryoCommonsHttpInvokerRequestExecutor;

/**
 * @author bsli@starit.com.cn
 * @date 2012-7-5 下午3:33:13
 */
public class Client {
	public static void main(String[] args) {
		User user = new User();
		user.setUsername("melin");
		user.setPassword("111111");
		
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		for(int i=0; i<1000; i++) {
			testKryo(user);
		}
		stopWatch.stop();
		System.out.println(stopWatch.getTotalTimeMillis());
		
	}
	
	private static void testKryo(User user) {
		HttpInvokerProxyFactoryBean factoryBean = new HttpInvokerProxyFactoryBean();
		factoryBean.setServiceInterface(Hello.class);
		factoryBean.setServiceUrl("http://localhost:8080/kryo/remoting/helloService");
		factoryBean.setHttpInvokerRequestExecutor(new KryoCommonsHttpInvokerRequestExecutor());
		factoryBean.afterPropertiesSet();
		
		Hello hello = (Hello)factoryBean.getObject();
		hello.saveUser(user);
	}
	
	private static void testJava(User user) {
		HttpInvokerProxyFactoryBean factoryBean = new HttpInvokerProxyFactoryBean();
		factoryBean.setServiceInterface(Hello.class);
		factoryBean.setServiceUrl("http://localhost:8080/kryo/remoting/helloService1");
		factoryBean.setHttpInvokerRequestExecutor(new CommonsHttpInvokerRequestExecutor());
		factoryBean.afterPropertiesSet();
		
		Hello hello = (Hello)factoryBean.getObject();
		hello.saveUser(user);
	}
}
