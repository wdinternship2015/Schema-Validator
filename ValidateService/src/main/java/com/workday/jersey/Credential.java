package com.workday.jersey;

public 	class Credential {
	public String username;
	public String password;
	public Credential() {};
	public Credential(String username, String password) {
		this.username = username;
		this.password = password;
	}
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
	
	@Override
	    public String toString() {
	        return new StringBuffer(" username : ").append(this.username)
	                .append(" password : ").append(this.password)
	                .toString();
	    }

}

