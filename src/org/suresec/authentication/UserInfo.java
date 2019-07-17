package org.suresec.authentication;

import java.io.Serializable;


public class UserInfo implements Serializable{

	private String account;
	private String oldpwd;
	private String newpwd;
	private String again;
	
	public UserInfo() {
		// TODO Auto-generated constructor stub
	}
	
	public UserInfo(String account, String oldpwd, String newpwd) {
		// TODO Auto-generated constructor stub
		this.account = account;
		this.oldpwd = oldpwd;
		this.newpwd = newpwd;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getOldpwd() {
		return oldpwd;
	}

	public void setOldpwd(String oldpwd) {
		this.oldpwd = oldpwd;
	}

	public String getNewpwd() {
		return newpwd;
	}

	public void setNewpwd(String newpwd) {
		this.newpwd = newpwd;
	}

	public String getAgain() {
		return again;
	}

	public void setAgain(String again) {
		this.again = again;
	}


	
	

}
