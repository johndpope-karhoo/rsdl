package com.ractoc.rsdl.generator;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ractoc.rsdl.generator.definition.Service;

public class RestGenerator {

	public static final String INTERFACE_PACKAGE = "com.ractoc.generated.service";
	public static final String INTERFACE_JSON = "/interface.json";
	public static final String PATHNAME = "src/main/resources";
	public static final String OUTPUT_PATHNAME = "target/generated-sources";

	public static void main(String[] args) throws IOException {
		RestGenerator generator = new RestGenerator();
		// generator.writeServiceToFile(PATHNAME + INTERFACE_JSON);
		Service service = generator.readServiceFromFile(PATHNAME + INTERFACE_JSON);
		ApiGenerator.generateServiceApi(service, OUTPUT_PATHNAME, INTERFACE_PACKAGE);
		ClientGenerator.generateServiceClient(service, OUTPUT_PATHNAME, INTERFACE_PACKAGE);
		ServerGenerator.generateServiceServer(service, OUTPUT_PATHNAME, INTERFACE_PACKAGE);
	}
	
//	private void writeServiceToFile(String interfaceJson)
//			throws JsonGenerationException, JsonMappingException, IOException {
//		List<Parameter> parameters = new ArrayList<Parameter>();
//		parameters.add(new Parameter("first", "Integer"));
//		parameters.add(new Parameter("second", "Integer"));
//		List<Procedure> procedures = new ArrayList<Procedure>();
//		procedures.add(new Procedure("add", parameters, new Result("Integer"), "GET"));
//		Service service = new Service("math service", "/math", procedures);
//
//		ObjectMapper mapper = new ObjectMapper();
//		mapper.writeValue(new File(interfaceJson), service);
//	}

	private Service readServiceFromFile(String interfaceJson)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		Service service = mapper.readValue(new File(interfaceJson), Service.class);
		return service;
	}
}
