package com.ansvia.belajar.graph.models

import javax.persistence.{Version, Id}
import annotation.target.field


/**
 * Copyright (C) 2011-2012 Ansvia Inc.
 * User: robin
 * Date: 12/25/12
 * Time: 10:57 PM
 *
 */

trait DigakuModel {
    @Id var id:String = _
    @Version var version:String = _

    override def toString = "%s(%s,%s)".format(getClass.getSimpleName, id, version)
}

case class User(var name:String="") extends DigakuModel {


    var supporting:java.util.List[User] = new java.util.ArrayList[User]()

    def this(){
        this("")
    }

    override def toString = "User(%s,%s)".format(id, name)
}
