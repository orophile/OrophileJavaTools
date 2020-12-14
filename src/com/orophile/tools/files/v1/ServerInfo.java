package com.orophile.tools.files.v1;

public class ServerInfo
{
	private String login;
	private String password;
	private String host;
	
	private String serviceName;
	private int port;
	private int timeOut;
	
	@Override
	public String toString()
	{
		String text;
		text  = "Server Info\n";
		text += "Host        : " + this.host + "\n";
		text += "Login       : " + this.login + "\n";
		text += "ServiceName : " + this.serviceName + "\n";
		text += "Port        : " + this.port + "\n";
		text += "Time Out    : " + this.timeOut + "\n";
		
		return text;
	}
	
	public ServerInfo()
	{
		this.timeOut = 30000;
		this.port = 22;
	}
		
	
	public String getLogin()
	{
		return login;
	}
	public void setLogin(String login)
	{
		this.login = login;
	}
	public String getPassword()
	{
		return password;
	}
	public void setPassword(String password)
	{
		this.password = password;
	}
	public String getHost()
	{
		return host;
	}
	public void setHost(String host)
	{
		this.host = host;
	}
	public String getServiceName()
	{
		return serviceName;
	}
	public void setServiceName(String serviceName)
	{
		this.serviceName = serviceName;
	}
	public int getPort()
	{
		return port;
	}
	public void setPort(int port)
	{
		this.port = port;
	}
	public int getTimeOut()
	{
		return timeOut;
	}
	public void setTimeOut(int timeOut)
	{
		this.timeOut = timeOut;
	}
}
