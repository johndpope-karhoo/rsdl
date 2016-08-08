package com.ractoc.rsdl.generator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.ractoc.rsdl.generator.definition.Parameter;
import com.ractoc.rsdl.generator.definition.Procedure;
import com.ractoc.rsdl.generator.definition.Service;

public class ClientGenerator extends BaseGenerator {
	
	private static ClientGenerator instance = new ClientGenerator();
	
	public static void generateServiceClient(Service service, String outputFolder, String genPackage) throws IOException {

		String clientPackage = genPackage + "." + CLIENT_SUFFIX.toLowerCase();
		String apiPackage = genPackage + "." + API_SUFFIX.toLowerCase();
		String serviceClientClassName = instance.createServiceClassName(service.getService()) + CLIENT_SUFFIX;
		String serviceApiClassName = instance.createServiceClassName(service.getService()) + API_SUFFIX;
		
		File clientPackageFolder = instance.createFolderStructure(outputFolder, clientPackage);
		File serviceClientClassFile = instance.createServiceClassFile(clientPackageFolder, serviceClientClassName);
		instance.generateClientStub(clientPackage, apiPackage, service, serviceClientClassName, serviceApiClassName, serviceClientClassFile);
	}

	private void generateClientStub(String genPackage, String apiPackage, Service service, String serviceClassName, String serviceApiClassName, File serviceClassFile)
			throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(serviceClassFile));
		writer.write("package " + genPackage + ";");
		writer.newLine();
		writer.newLine();
		writer.write("import java.net.URI;");
		writer.newLine();
		writer.write("import javax.ws.rs.client.Client;");
		writer.newLine();
		writer.write("import javax.ws.rs.client.ClientBuilder;");
		writer.newLine();
		writer.write("import javax.ws.rs.client.WebTarget;");
		writer.newLine();
		writer.write("import javax.ws.rs.core.MediaType;");
		writer.newLine();
		writer.write("import javax.ws.rs.core.Response;");
		writer.newLine();
		writer.write("import javax.ws.rs.core.UriBuilder;");
		writer.newLine();
		writer.newLine();
		writer.write("import " + apiPackage + ".*;");
		writer.newLine();
		writer.newLine();
		writer.write("public class " + serviceClassName+ " implements " + serviceApiClassName + " {");
		writer.newLine();
		writer.newLine();
		writer.write("\tprivate final URI endpointURI;");
		writer.newLine();
		writer.newLine();
		writer.write("\tpublic " + serviceClassName + "(String endpoint) {");
		writer.newLine();
		writer.write("\t\tthis.endpointURI = UriBuilder.fromUri(endpoint).build();");
		writer.newLine();
		writer.write("\t}");
		for (Procedure procedure : service.getProcedures()) {
			writer.newLine();
			writer.newLine();
			writer.write("\tpublic " + procedure.getResult().getType() + " " + procedure.getProcedure() + "(");
			boolean first = true;
			for (Parameter parameter : procedure.getParameters()) {
				if (!first) {
					writer.write(", ");
				}
				writer.write("final " + parameter.getType() + " " + parameter.getParameter());
				first = false;
			}
			writer.write(") {");
			writer.newLine();
			writer.write("\t\tClient client = ClientBuilder.newClient();");
			writer.newLine();
			writer.write("\t\tWebTarget service = client.target(endpointURI);");
			writer.newLine();
			writer.write("\t\tResponse response = service.path(\"" + service.getPath() + "\").path(\""
					+ procedure.getProcedure());
			if (!procedure.getParameters().isEmpty()) {
				first = true;
				for (Parameter parameter : procedure.getParameters()) {
					if (!first) {
						writer.write(" + \"");
					}
					writer.write("/\" + " + parameter.getParameter());
					first = false;
				}
			} else {
				writer.write("\"");
			}
			writer.write(").request().accept(MediaType.APPLICATION_JSON)." + procedure.getType().toLowerCase() + "();");
			writer.newLine();
			writer.write("\t\tif (response.getStatus() != 200) {");
			writer.newLine();
			writer.write("\t\t\tthrow new RuntimeException(\"Invalid response code: \" + response.getStatus());");
			writer.newLine();
			writer.write("\t\t}");
			writer.newLine();
			writer.write("\t\t" + procedure.getResult().getType() + " result = response.readEntity("
					+ procedure.getResult().getType() + ".class);");
			writer.newLine();
			writer.write("\t\tSystem.out.println(\"result: \" + result);");
			writer.newLine();
			writer.write("\t\treturn result;");
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
