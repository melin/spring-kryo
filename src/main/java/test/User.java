package test;

import java.io.Serializable;

/**
 * @author bsli@starit.com.cn
 * @date 2012-7-5 下午4:01:43
 */
public class User implements Serializable {
	private String username;
	private String password;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
