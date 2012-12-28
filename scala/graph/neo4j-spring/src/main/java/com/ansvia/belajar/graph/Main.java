package com.ansvia.belajar.graph;

import com.ansvia.belajar.graph.model.User;
import com.ansvia.belajar.graph.model.UserRepo;

/**
 * Copyright (C) 2011-2012 Ansvia Inc.
 * User: robin
 * Date: 12/27/12
 * Time: 2:44 AM
 */
public class Main {
    public static void main(String[] args){

        UserRepo repo;

        User robin = new User("robin", 25);
        User temon = new User("temon", 22);

        temon.support(robin);

        robin.persist();

        System.out.println("=== who is supporting robin?");

        for (User sup : robin.supporting){
            System.out.println(sup.name);
        }
    }
}
