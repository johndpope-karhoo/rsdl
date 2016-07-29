package com.ractoc.rsdl.generator.definition;

/**
 * Created by racto_000 on 20-7-2016.
 */
public class Result {
    private String type;

    public Result() {
		super();
	}

    public Result(String type) {
		super();
		this.type = type;
	}

	public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Result{" +
                "type='" + type + '\'' +
                '}';
    }
}
