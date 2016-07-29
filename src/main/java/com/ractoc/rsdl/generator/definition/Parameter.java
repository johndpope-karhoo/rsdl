package com.ractoc.rsdl.generator.definition;

/**
 * Created by racto_000 on 20-7-2016.
 */
public class Parameter {

    private String parameter;
    private String type;

    public Parameter() {
		super();
	}

    public Parameter(String parameter, String type) {
		super();
		this.parameter = parameter;
		this.type = type;
	}

	public String getParameter() {
        return parameter;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Parameter{" +
                "name='" + parameter + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
