package com.ansvia.belajar.commons;

/**
 * Copyright (C) 2011-2012 Ansvia Inc.
 * User: robin
 * Date: 1/5/13
 * Time: 2:15 PM
 */
public class TestSingleton {

    private String con = null;
    private static TestSingleton instance = null;

    public static TestSingleton getInstance(){
        if(instance == null){
            instance = new TestSingleton("localhost", 27017);
        }
        return instance;
    }

    private TestSingleton(String host, int port){
        this.con = "MongoDB";

        System.out.println("dalam constructor");
    }

    public void whoami(){
        System.out.println(this.con);
    }

}

