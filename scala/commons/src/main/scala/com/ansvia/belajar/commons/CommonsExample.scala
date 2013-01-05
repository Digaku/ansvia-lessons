
package com.ansvia.belajar.commons

import com.ansvia.commons.logging.Slf4jLogger
//
//object TestSingleton2  {
//
//  private val nama = "bram"
//
//  println("di dalam constructor")
//
//  def whoami(){
//    println(nama)
//  }
//}


object CommonsExample extends Slf4jLogger {

  lazy val user1 = {
    println("di dalam user1 scope")
    "ervan"
  }
  lazy val user2 = {
    println("di dalam user2 scope")

    "bram"
  }
  lazy val user3 = {
      println("di dalam user3 scope #1")
      println("di dalam user3 scope #2")

      "adit"
  }


  def main(args:Array[String]){

//    TestSingleton2.whoami()
//    TestSingleton2.whoami()
//    TestSingleton2.whoami()


    println(user3)
    println(user3)
    println(user3)
    println(user3)
    println(user3)
    println(user2)
    println(user1)
    println(user1)
    println(user1)
    println(user1)
    println(user1)
    println(user1)
    println(user1)
    println(user1)
    println(user1)
    println(user1)
    println(user1)

  }

}