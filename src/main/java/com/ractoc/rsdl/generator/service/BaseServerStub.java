package com.ractoc.rsdl.generator.service;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by racto_000 on 21-7-2016.
 */
public class BaseServerStub {

    private final URL endPointUrl;

    public BaseServerStub(String endPoint) {
        try {
            this.endPointUrl = new URL(endPoint);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Unable to create endPointUrl from supplied string: " + endPoint, e);
        }
    }
}
