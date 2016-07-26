package com.ractoc.rsdl.generator.definition;

/**
 * Created by racto_000 on 20-7-2016.
 */
public class Parameter {

    private String name;
    private String type;

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Parameter{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
