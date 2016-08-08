package com.ractoc.rsdl.generator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.ractoc.rsdl.generator.definition.Parameter;
import com.ractoc.rsdl.generator.definition.Procedure;
import com.ractoc.rsdl.generator.definition.Service;

public class ApiGenerator extends BaseGenerator {
	
	private static ApiGenerator instance = new ApiGenerator();
	
	public static void generateServiceApi(Service service, String outputFolder, String genPackage) throws IOException {

		String apiPackage = genPackage + "." + API_SUFFIX.toLowerCase();
		File apiPackageFolder = instance.createFolderStructure(outputFolder, apiPackage);

		String serviceApiClassName = instance.createServiceClassName(service.getService()) + API_SUFFIX;
		File serviceApiClassFile = instance.createServiceClassFile(apiPackageFolder, serviceApiClassName);
		instance.generateApiStub(apiPackage, service, serviceApiClassName, serviceApiClassFile);
	}

	private void generateApiStub(String genPackage, Service service, String serviceClassName, File serviceClassFile)
			throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(serviceClassFile));
		writer.write("package " + genPackage + ";");
		writer.newLine();
		writer.newLine();
		writer.write("public class " + serviceClassName + " {");
		for (Procedure procedure : service.getProcedures()) {
			writer.newLine();
			writer.newLine();
			writer.write("\t" + procedure.getResult().getType() + " " + procedure.getProcedure() + "(");
			boolean first = true;
			for (Parameter parameter : procedure.getParameters()) {
				if (!first) {
					writer.write(", ");
				}
				writer.write(parameter.getType() + " " + parameter.getParameter());
				first = false;
			}
			writer.write(");");
		}
		writer.newLine();
		writer.newLine();
		writer.write("}");
		writer.flush();
		writer.close();
	}

}
