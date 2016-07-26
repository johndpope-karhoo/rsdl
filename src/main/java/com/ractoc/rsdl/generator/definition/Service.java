package com.ractoc.rsdl.generator.definition;

import java.util.List;

/**
 * Created by racto_000 on 20-7-2016.
 */
public class Service {

    private String service;
    private List<Procedure> procedures;

    public String getService() {
        return service;
    }

    public List<Procedure> getProcedures() {
        return procedures;
    }

    @Override
    public String toString() {
        return "Service{" +
                "service='" + service + '\'' +
                ", procedures=" + procedures +
                '}';
    }
}
