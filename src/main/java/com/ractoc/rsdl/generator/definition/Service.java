package com.ractoc.rsdl.generator.definition;

import java.util.List;

/**
 * Created by racto_000 on 20-7-2016.
 */
public class Service {

    private String service;
    private String path;
    private List<Procedure> procedures;

    public Service() {
	}

	public Service(String service, String path, List<Procedure> procedures) {
		this.service = service;
		this.path = path;
		this.procedures = procedures;
	}

	public String getService() {
        return service;
    }

    public List<Procedure> getProcedures() {
        return procedures;
    }
    
    public String getPath() {
		return path;
	}

	@Override
    public String toString() {
        return "Service{" +
        		"service='" + service + '\'' +
                "pathj='" + path + '\'' +
                ", procedures=" + procedures +
                '}';
    }
}
