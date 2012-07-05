package test;

/**
 * @author bsli@starit.com.cn
 * @date 2012-7-5 下午3:06:23
 */
public class HelloService implements Hello {
	public String sayHello(String name) {
		System.out.println(name);
		return "hello " + name;
	}
	
	public User saveUser(User user) {
		System.out.println(user.getUsername() + " = " + user.getPassword());
		user.setPassword("000000");
		return user;
	}
}
