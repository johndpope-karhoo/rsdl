package com.ractoc.rsdl.generator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.ractoc.rsdl.generator.definition.Parameter;
import com.ractoc.rsdl.generator.definition.Procedure;
import com.ractoc.rsdl.generator.definition.Service;

public class ServerGenerator extends BaseGenerator {
	
	private static ServerGenerator instance = new ServerGenerator();

	public static void generateServiceServer(Service service, String outputFolder, String genPackage) throws IOException {
		String serverPackage = genPackage + "." + SERVER_SUFFIX.toLowerCase();
		String apiPackage = genPackage + "." + API_SUFFIX.toLowerCase();
		String serviceServerClassName = instance.createServiceClassName(service.getService()) + SERVER_SUFFIX;
		String serviceApiClassName = instance.createServiceClassName(service.getService()) + API_SUFFIX;

		File serverPackageFolder = instance.createFolderStructure(outputFolder, serverPackage);
		File serviceServerClassFile = instance.createServiceClassFile(serverPackageFolder, serviceServerClassName);
		instance.generateServerStub(serverPackage, apiPackage, service, serviceServerClassName, serviceApiClassName, serviceServerClassFile);
	}

	private void generateServerStub(String serverPackage, String apiPackage, Service service, String serviceClassName, String serviceApiClassName, File serviceClassFile)
			throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(serviceClassFile));
		writer.write("package " + serverPackage + ";");
		writer.newLine();
		writer.newLine();
		writer.write("import com.ractoc.rsdl.generator.service.BaseServerStub;");
		writer.newLine();
		writer.write("import javax.ws.rs.*;");
		writer.newLine();
		writer.write("import javax.ws.rs.core.MediaType;");
		writer.newLine();
		writer.newLine();
		writer.write("import " + apiPackage + ".*;");
		writer.newLine();
		writer.newLine();
		writer.write("@Path(\"" + service.getPath() + "\")");
		writer.newLine();
		writer.write("public class " + serviceClassName + " implements " + serviceApiClassName + " {");
		writer.newLine();
		writer.newLine();
		writer.write("\tprivate final " + serviceApiClassName + " listener;");
		writer.newLine();
		writer.newLine();
		writer.write("\tpublic " + serviceClassName + "(final " + serviceApiClassName + " listener) {");
		writer.newLine();
		writer.write("\t\tthis.listener = listener;");
		writer.newLine();
		writer.write("\t}");
		for (Procedure procedure : service.getProcedures()) {
			writer.newLine();
			writer.newLine();
			writer.write("\t@" + procedure.getType().toUpperCase());
			writer.newLine();
			writer.write("\t@Path(\"" + procedure.getProcedure());
			if (!procedure.getParameters().isEmpty()) {
				for (Parameter parameter : procedure.getParameters()) {
					writer.write("/{" + parameter.getParameter() + "}");
				}
			}
			writer.write("\")");
			writer.newLine();
			writer.write("\t@Produces(MediaType.APPLICATION_JSON)");
			writer.newLine();
			writer.write("\tpublic " + procedure.getResult().getType() + " " + procedure.getProcedure() + "(");
			boolean first = true;
			for (Parameter parameter : procedure.getParameters()) {
				if (!first) {
					writer.write(", ");
				}
				writer.write("@FormParam(\"" + parameter.getParameter() + "\") final " + parameter.getType() + " "
						+ parameter.getParameter());
				first = false;
			}
			writer.write(") {");
			writer.newLine();
			writer.write("\t\treturn listener." + procedure.getProcedure() + "(");
			first = true;
			for (Parameter parameter : procedure.getParameters()) {
				if (!first) {
					writer.write(", ");
				}
				writer.write(parameter.getParameter());
				first = false;
			}
			writer.write(");");
			writer.newLine();
			writer.write("\t}");
		}
		writer.newLine();
		writer.newLine();
		writer.write("}");
		writer.flush();
		writer.close();
	}
}
