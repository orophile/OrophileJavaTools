package com.orophile.tools.files.v1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class FileTreatment
{
	
	public void replaceInFile(String filePath, String toChange, String replacement) throws IOException
	{
		File file = new File(filePath);
		FileReader fileReader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		
		FileWriter fileWriter = new FileWriter(filePath + "_tmp");
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		
		String line;
		while ((line = bufferedReader.readLine()) != null)
		{
			if (line.contains(toChange))
			{
				line = line.replace(toChange, replacement);
			}
			
			bufferedWriter.write(line);
			bufferedWriter.newLine();
		}
		
		bufferedWriter.close();
		bufferedReader.close();
		fileWriter.close();
		fileReader.close();
		
		file = new File(filePath);
		file.delete();
		
		File tmpFile = new File(filePath + "_tmp");
		tmpFile.renameTo(new File(filePath));
		
	}
	
	public void getFiles(String remotePath, Set<String> filesToDownloadSet, String localPath, ServerInfo server, Logger logger) throws JSchException, SftpException
	{
		ChannelSftp channelSftp;
		Session sessionSFtp;
		JSch jsch = new JSch();

		sessionSFtp = jsch.getSession(server.getLogin(), server.getHost(), server.getPort());

		java.util.Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");
		sessionSFtp.setConfig(config);
		sessionSFtp.setConfig("PreferredAuthentications", "password");
		sessionSFtp.setPassword(server.getPassword());

		logger.logMessage(server.toString());

		logger.logMessage("Connecting to " + server.getHost() + " ...");
		sessionSFtp.connect(server.getTimeOut());
		logger.logMessage("Connected to " + server.getHost());

		channelSftp = (ChannelSftp) sessionSFtp.openChannel("sftp");

		logger.logMessage("Opening channel to " + server.getHost() + " ...");
		channelSftp.connect(server.getTimeOut());
		logger.logMessage("Sftp Channel opened to " + server.getHost());

		channelSftp.cd(remotePath);

		logger.logMessage("remotePath = " + remotePath);

		File directory = new File(localPath);

		if (!directory.exists())
		{
			directory.mkdirs();
		}

		for (String fileName : filesToDownloadSet)
		{
			logger.logMessage("\nDownloading file " + fileName + "...");
			channelSftp.get(fileName, localPath + "\\" + fileName);
			logger.logMessage("File " + fileName + " downloaded.\n");
		}

		channelSftp.exit();
		logger.logMessage("Sftp Channel Exited!");
		channelSftp.disconnect();
		logger.logMessage("Sftp Channel Closed!");
		sessionSFtp.disconnect();
		logger.logMessage("Sftp Session Closed!");
	}
	
	public void getFile(String remotePath, String remotefileName, String localPath, ServerInfo server, Logger logger) throws JSchException, SftpException
	{
		if(remotePath == null || remotefileName == null || localPath == null || server == null)
		{
			logger.logMessage("Error : remotePath == null || remotefileName == null || localPath == null || server == null");
			return;
		}
		
		ChannelSftp channelSftp;
		Session sessionSFtp;
		JSch jsch = new JSch();

		sessionSFtp = jsch.getSession(server.getLogin(), server.getHost(), server.getPort());

		java.util.Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");
		sessionSFtp.setConfig(config);
		sessionSFtp.setConfig("PreferredAuthentications", "password");
		sessionSFtp.setPassword(server.getPassword());

		logger.logMessage(server.toString());

		logger.logMessage("Connecting to " + server.getHost() + " ...");
		sessionSFtp.connect(server.getTimeOut());
		logger.logMessage("Connected to " + server.getHost());

		channelSftp = (ChannelSftp) sessionSFtp.openChannel("sftp");

		logger.logMessage("Opening channel to " + server.getHost() + " ...");
		channelSftp.connect(server.getTimeOut());
		logger.logMessage("Sftp Channel opened to " + server.getHost());

		channelSftp.cd(remotePath);

		logger.logMessage("remotePath = " + remotePath);

		File directory = new File(localPath);

		if (!directory.exists())
		{
			directory.mkdirs();
		}

		logger.logMessage("\nDownloading file " + remotefileName + "...");
		channelSftp.get(remotefileName, localPath + "\\" + remotefileName);
		logger.logMessage("File " + remotefileName + " downloaded.\n");
		
		channelSftp.exit();
		logger.logMessage("Sftp Channel Exited!");
		channelSftp.disconnect();
		logger.logMessage("Sftp Channel Closed!");
		sessionSFtp.disconnect();
		logger.logMessage("Sftp Session Closed!");
	}
	
	
	public Set<String> fileLinesToSet(String filePath) throws IOException
	{
		Set<String> lineSet = new HashSet<String>();
		File file = new File(filePath);
		FileReader fileReader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(fileReader);

		String line;

		while ((line = bufferedReader.readLine()) != null)
		{
			if(line.trim().length() > 0)
			{
				lineSet.add(line.trim());
			}
		}
		bufferedReader.close();
		fileReader.close();

		return lineSet;
	}
	
	public Set<String> fileNamesToSet2(String filePath) throws IOException
	{
		Set<String> lineSet = new HashSet<String>();
		File file = new File(filePath);
		FileReader fileReader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(fileReader);

		String line;

		while ((line = bufferedReader.readLine()) != null)
		{
			if(line.length() > 0)
			{
				if(line.trim().startsWith("./"))
				{
					line = line.trim().substring(2);
					lineSet.add(line);
				}
			}
		}
		bufferedReader.close();
		fileReader.close();

		return lineSet;
	}
	
	public Set<String> logfileNamesToSet(String filePath) throws IOException
	{
		Set<String> lineSet = new HashSet<String>();
		File file = new File(filePath);
		FileReader fileReader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(fileReader);

		String line;

		while ((line = bufferedReader.readLine()) != null)
		{
			if(line.length() > 0)
			{
				if(line.trim().startsWith("./"))
				{
					line = line.trim().substring(2);
					lineSet.add(line);
				}
			}
		}
		bufferedReader.close();
		fileReader.close();

		return lineSet;
	}
	
	public Set<String> invokfileNamesToSet(String filePath) throws IOException
	{
		Set<String> lineSet = new HashSet<String>();
		File file = new File(filePath);
		FileReader fileReader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(fileReader);

		String line;

		while ((line = bufferedReader.readLine()) != null)
		{
			if(line.length() > 0)
			{
				if(line.trim().startsWith("./Invoke_RESP"))
				{
					line = line.trim().substring(2);
					lineSet.add(line);
				}
			}
		}
		bufferedReader.close();
		fileReader.close();

		return lineSet;
	}
	
	public Set<String> notInvokfileNamesToSet(String filePath) throws IOException
	{
		Set<String> lineSet = new HashSet<String>();
		File file = new File(filePath);
		FileReader fileReader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(fileReader);

		String line;

		while ((line = bufferedReader.readLine()) != null)
		{
			if(line.length() > 0)
			{
				if(!(line.trim().startsWith("./Invoke_RESP")) && (line.trim().startsWith("./")))
				{
					line = line.trim().substring(2);
					lineSet.add(line);
				}
			}
		}
		bufferedReader.close();
		fileReader.close();

		return lineSet;
	}
	
	
	
	public Set<String> fileNamesToSet(String dirPath) throws IOException
	{
		Set<String> lineSet = new HashSet<String>();
		String[] pathnames;
		
		File dir = new File(dirPath);
		
		if(!dir.isDirectory()) return null;
		
		pathnames = dir.list();
		
		for(String pathname : pathnames)
		{
			lineSet.add(pathname);
		}

		return lineSet;
	}

	public void copyFile(String sourceFilePath, String destinationPath, String fileName) throws IOException
	{
		File directory = new File(destinationPath);
		if (!directory.exists())
		{
			directory.mkdirs();
		}
		
		File src = new File(sourceFilePath);
		File dest = new File(destinationPath + "\\" + fileName);
		
		InputStream is = null;
		OutputStream os = null;
		try
		{
			is = new FileInputStream(src);
			os = new FileOutputStream(dest);

			// buffer size 1K
			byte[] buf = new byte[1024];
			int bytesRead;
			while ((bytesRead = is.read(buf)) > 0)
			{
				os.write(buf, 0, bytesRead);
			}
			System.out.println("");
		}
		finally
		{
			is.close();
			os.close();
		}
	}
	
	public void copyFile(String sourceFilePath, String destinationFilePath) throws IOException
	{
		File directory = new File(destinationFilePath);
		if (!directory.exists())
		{
			directory.mkdirs();
		}
		
		File src = new File(sourceFilePath);
		File dest = new File(destinationFilePath);
		
		InputStream is = null;
		OutputStream os = null;
		try
		{
			is = new FileInputStream(src);
			os = new FileOutputStream(dest);

			// buffer size 1K
			byte[] buf = new byte[1024];
			int bytesRead;
			while ((bytesRead = is.read(buf)) > 0)
			{
				os.write(buf, 0, bytesRead);
			}
			System.out.println("");
		}
		finally
		{
			is.close();
			os.close();
		}
	}
}
