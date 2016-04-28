package org.spat.dao;

public class DBConfig {

	private String name;
	
	private String driversClass;
	
	private String userName;
	
	private String passWord;
	
	private int maxPoolSize = 10;
	
	private int minPoolSize = 1;
	
	private int idleTimeout = 1000;
	
	private long timeout;
	
	private Boolean autoShrink = true;
	
	private String host;
	
	private String host2;
	
	private String dbName;
	
	private String encoder = "utf-8";
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getDriversClass() {
		return driversClass;
	}

	public void setDriversClass(String driversClass) {
		this.driversClass = driversClass;
	}

	public String getConnetionUrl() {
		StringBuilder url = new StringBuilder();
		url.append("jdbc:mysql://");
		url.append(host);
		url.append("/");
		url.append(this.dbName);
		url.append("?connectTimeout=");
		url.append(this.timeout*1000);
		url.append("&amp;useUnicode=true&amp;characterEncoding=");
		url.append(encoder);
		return url.toString();
		//return connetionUrl;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public int getMaxPoolSize() {
		return maxPoolSize;
	}

	public void setMaxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}

	public void setMinPoolSize(int minPoolSize) {
		this.minPoolSize = minPoolSize;
	}

	public int getMinPoolSize() {
		return minPoolSize;
	}

	public void setIdleTimeout(int idleTimeout) {
		this.idleTimeout = idleTimeout;
	}

	public int getIdleTimeout() {
		return idleTimeout;
	}


	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}


	public long getTimeout() {
		return timeout;
	}

	public void setAutoShrink(Boolean autoShrink) {
		this.autoShrink = autoShrink;
	}

	public Boolean getAutoShrink() {
		return autoShrink;
	}
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getHost2() {
		return host2;
	}

	public void setHost2(String host2) {
		this.host2 = host2;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getEncoder() {
		return encoder;
	}

	public void setEncoder(String encoder) {
		this.encoder = encoder;
	}
}