package com.ractoc.rsdl.generator;

import java.io.File;
import java.io.IOException;

public class BaseGenerator {

	protected static final String JAVA_EXTENSION = ".java";
	protected static final String API_SUFFIX = "Api";
	protected static final String CLIENT_SUFFIX = "Client";
	protected static final String SERVER_SUFFIX = "Server";
	
	protected File createServiceClassFile(File packageFolder, String serviceClassName) throws IOException {
		File serviceClassFile = new File(packageFolder, serviceClassName + JAVA_EXTENSION);
		if (!serviceClassFile.exists()) {
			if (!serviceClassFile.createNewFile()) {
				throw new IOException("Unable to create new file for " + serviceClassFile.getAbsolutePath());
			}
		}
		return serviceClassFile;
	}

	protected String createServiceClassName(String serviceName) {
		String[] words = serviceName.split(" ");
		StringBuilder serviceClassName = new StringBuilder();
		for (String word : words) {
			serviceClassName.append(word.substring(0, 1).toUpperCase()).append(word.substring(1));
		}
		return serviceClassName.toString();
	}

	protected File createFolderStructure(String outputFolder, String genPackage) throws IOException {
		File folder = new File(outputFolder);
		String[] folderStructure = genPackage.split("\\.");
		for (String subFolder : folderStructure) {
			folder = new File(folder, subFolder);
		}
		if (!folder.exists() && !folder.mkdirs()) {
			throw new IOException("Unable to create new folder for " + folder.getAbsolutePath());
		}
		return folder;
	}

}
