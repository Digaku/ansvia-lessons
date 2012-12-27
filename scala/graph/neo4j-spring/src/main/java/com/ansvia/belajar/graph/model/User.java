package com.ansvia.belajar.graph.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.Query;

import java.util.Set;

/**
 * Copyright (C) 2011-2012 Ansvia Inc.
 * User: robin
 * Date: 12/27/12
 * Time: 2:13 AM
 */

@NodeEntity
public class User {
    public @GraphId Long id;

    public @Indexed String name;
    public Integer age;

    @Query("start node=node({self}) match node-[:SUPPORT]->x return x")
    public Set<User> supporting;

    @Query("start node=node({self}) match node<-[:SUPPORT]-x return x")
    public Set<User> supporters;

    @Autowired static UserRepo repo;
    public static UserRepo getRepo(){
        return repo;
    }

    public User(String _name, int _age){
        name = _name;
        age = _age;
    }

    public void support(User user){
        supporting.add(user);
    }

    public void unsupport(User user){
        supporting.remove(user);
    }

}

