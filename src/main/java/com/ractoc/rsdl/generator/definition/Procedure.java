package com.ractoc.rsdl.generator.definition;

import java.util.List;

/**
 * Created by racto_000 on 20-7-2016.
 */
public class Procedure {

    private String procedure;
    private List<Parameter> parameters;
    private Result result;
    private Fault fault;

    public String getProcedure() {
        return procedure;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public Result getResult() {
        return result;
    }

    public Fault getFault() {
        return fault;
    }

    @Override
    public String toString() {
        return "Procedure{" +
                "procedure='" + procedure + '\'' +
                ", parameters=" + parameters +
                ", result=" + result +
                ", fault=" + fault +
                '}';
    }
}
