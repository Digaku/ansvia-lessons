package com.ansvia.belajar.graph.models

import javax.persistence.{Version, Id}

/**
 * Copyright (C) 2011-2012 Ansvia Inc.
 * User: robin
 * Date: 12/25/12
 * Time: 10:57 PM
 *
 */
class User{
  @Id var id:String = _
  var name:String = _
  var addresses:List[Address] = _
  @Version var version:String = _

  override def toString = "User(%s,%s)".format(id, name)
}
