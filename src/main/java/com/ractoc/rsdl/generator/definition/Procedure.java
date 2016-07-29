package com.ractoc.rsdl.generator.definition;

import java.util.List;

/**
 * Created by racto_000 on 20-7-2016.
 */
public class Procedure {

    private String procedure;
    private List<Parameter> parameters;
    private Result result;
    private String type;

    public Procedure() {
		super();
	}

    public Procedure(String procedure, List<Parameter> parameters, Result result, String type) {
		super();
		this.procedure = procedure;
		this.parameters = parameters;
		this.result = result;
		this.type = type;
	}

	public String getProcedure() {
        return procedure;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public Result getResult() {
        return result;
    }

    public String getType() {
		return type;
	}

	@Override
    public String toString() {
        return "Procedure{" +
                "procedure='" + procedure + '\'' +
                ", type='" + type + '\'' +
                ", parameters=" + parameters +
                ", result=" + result +
                '}';
    }
}
