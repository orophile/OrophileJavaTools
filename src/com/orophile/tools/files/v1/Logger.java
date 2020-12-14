package com.orophile.tools.files.v1;

public class Logger
{
	public void logMessage(String message)
	{
		System.out.println("-> " + message);
	}
	
	public void logMessage(String className, String methodName, String message)
	{
		System.out.println("=>=> " + className + " - " + methodName + " - " + message);
	}
}
