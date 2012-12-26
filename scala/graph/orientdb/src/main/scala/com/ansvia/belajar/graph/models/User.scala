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
class User {
  @Id @field var id:String = _
  var name:String = _
  @Version var version:String = _

    var supports:java.util.List[User] = new java.util.ArrayList[User]()


  override def toString = "User(%s,%s)".format(id, name)
}
