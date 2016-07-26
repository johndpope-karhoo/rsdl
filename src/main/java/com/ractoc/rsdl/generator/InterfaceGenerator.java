package com.ractoc.rsdl.generator;

import com.ractoc.rsdl.generator.definition.Parameter;
import com.ractoc.rsdl.generator.definition.Procedure;
import com.ractoc.rsdl.generator.definition.Service;
import com.ractoc.json.JsonParser;

import java.io.*;


public class InterfaceGenerator {

    public static final String INTERFACE_PACKAGE = "com.ractoc.generated.service";
    public static final String INTERFACE_JSON = "/interface.json";
    public static final String PATHNAME = "c:/Temp";
    public static final String CLIENT_SUFFIX = "Client";
    public static final String SERVER_SUFFIX = "Server";
    public static final String JAVA_EXTENSION = ".java";

    public static void main(String[] args) throws IOException {
        InterfaceGenerator generator = new InterfaceGenerator();
        Service service = generator.loadServiceFromConfigStream(InterfaceGenerator.class.getResourceAsStream(INTERFACE_JSON));
        generator.generateServiceClient(service, PATHNAME, INTERFACE_PACKAGE);
        generator.generateServiceServer(service, PATHNAME, INTERFACE_PACKAGE);
    }

    public void generateServiceClient(Service service, String outputFolder, String genPackage) throws IOException {

        String clientPackage = genPackage + "." + CLIENT_SUFFIX.toLowerCase();
        File clientPackageFolder = createFolderStructure(outputFolder, clientPackage);

        String serviceClientClassName = createServiceClassName(service.getService()) + CLIENT_SUFFIX;
        File serviceClientClassFile = createServiceClassFile(clientPackageFolder, serviceClientClassName);
        generateClientStub(clientPackage, service, serviceClientClassName, serviceClientClassFile);
    }

    public void generateServiceServer(Service service, String outputFolder, String genPackage) throws IOException {
        String serverPackage = genPackage + "." + SERVER_SUFFIX.toLowerCase();
        File serverPackageFolder = createFolderStructure(outputFolder, serverPackage);

        String serviceServerClassName = createServiceClassName(service.getService()) + SERVER_SUFFIX;
        File serviceServerClassFile = createServiceClassFile(serverPackageFolder, serviceServerClassName);
        generateServerStub(serverPackage, service, serviceServerClassName, serviceServerClassFile);
    }

    private void generateClientStub(String genPackage, Service service, String serviceClassName, File serviceClassFile) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(serviceClassFile));
        writer.write("package " + genPackage + ";");
        writer.newLine();
        writer.newLine();
        writer.write("import com.ractoc.rsdl.generator.service.BaseClientStub;");
        writer.newLine();
        writer.newLine();
        writer.write("public class " + serviceClassName + " extends BaseClientStub {");
        writer.newLine();
        writer.newLine();
        writer.write("\tpublic " + serviceClassName + "(String endPoint) {");
        writer.newLine();
        writer.write("\t\tsuper(endPoint);");
        writer.newLine();
        writer.write("\t}");
        for(Procedure procedure : service.getProcedures()) {
            writer.newLine();
            writer.newLine();
            writer.write("\tpublic " + procedure.getResult().getType() + " " + procedure.getProcedure() + "(");
            boolean first = true;
            for(Parameter parameter : procedure.getParameters()) {
                if (!first) {
                    writer.write(", ");
                }
                writer.write("final " + parameter.getType() + " " + parameter.getName());
                first = false;
            }
            writer.write(") {");
            writer.newLine();
            writer.write("\t\tSystem.out.println(\"testing " + procedure.getProcedure() + "\");");
            writer.newLine();
            writer.write("\t}");
        }
        writer.newLine();
        writer.newLine();
        writer.write("}");
        writer.flush();
        writer.close();
    }

    private void generateServerStub(String genPackage, Service service, String serviceClassName, File serviceClassFile) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(serviceClassFile));
        writer.write("package " + genPackage + ";");
        writer.newLine();
        writer.newLine();
        writer.write("import com.ractoc.rsdl.generator.service.BaseServerStub;");
        writer.newLine();
        writer.newLine();
        writer.write("public class " + serviceClassName + " extends BaseServerStub {");
        writer.newLine();
        writer.newLine();
        writer.write("\tpublic " + serviceClassName + "(String endPoint) {");
        writer.newLine();
        writer.write("\t\tsuper(endPoint);");
        writer.newLine();
        writer.write("\t}");
        for(Procedure procedure : service.getProcedures()) {
            writer.newLine();
            writer.newLine();
            writer.write("\tpublic " + procedure.getResult().getType() + " " + procedure.getProcedure() + "(");
            boolean first = true;
            for(Parameter parameter : procedure.getParameters()) {
                if (!first) {
                    writer.write(", ");
                }
                writer.write("final " + parameter.getType() + " " + parameter.getName());
                first = false;
            }
            writer.write(") {");
            writer.newLine();
            writer.write("\t\tSystem.out.println(\"testing " + procedure.getProcedure() + "\");");
            writer.newLine();
            writer.write("\t}");
        }
        writer.newLine();
        writer.newLine();
        writer.write("}");
        writer.flush();
        writer.close();
    }

    private File createServiceClassFile(File packageFolder, String serviceClassName) throws IOException {
        File serviceClassFile = new File(packageFolder, serviceClassName + JAVA_EXTENSION);
        if (!serviceClassFile.exists()) {
            if (!serviceClassFile.createNewFile()) {
                throw new IOException("Unable to create new file for " + serviceClassFile.getAbsolutePath());
            }
        }
        return serviceClassFile;
    }

    private String createServiceClassName(String serviceName) {
        String[] words = serviceName.split(" ");
        StringBuilder serviceClassName = new StringBuilder();
        for (String word : words) {
            serviceClassName.append(word.substring(0, 1).toUpperCase()).append(word.substring(1));
        }
        return serviceClassName.toString();
    }

    private File createFolderStructure(String outputFolder, String genPackage) throws IOException {
        File folder = new File(outputFolder);
        String[] folderStructure = genPackage.split("\\.");
        for(String subFolder : folderStructure) {
            folder = new File(folder, subFolder);
            if (!folder.exists()) {
                if (!folder.mkdir()) {
                    throw new IOException("Unable to create new folder for " + folder.getAbsolutePath());
                }
            }
        }
        return folder;
    }

    private Service loadServiceFromConfigStream(InputStream is) {
        InputStreamReader reader = new InputStreamReader(is);
        JsonParser parser = new JsonParser(reader);
        Service service = parser.readInto(Service.class);
        System.out.println(service);
        return service;
    }

}
