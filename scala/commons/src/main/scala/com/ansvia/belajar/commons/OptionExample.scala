package com.ansvia.belajar.commons

import com.ansvia.commons.logging.Slf4jLogger

/**
 * Copyright (C) 2011-2013 Ansvia Inc.
 * Author: robin
 * Date: 1/12/13
 *
 * Contoh kode penggunakan pattern Option.
 *
 */


/**
 *
 * Option[T]
 *
 * Option[String]
 * Option[Int]
 * Option[Long]
 *
 * Some("test")
 *
 * None
 *
 */

object OptionExample {

    var x:Option[String] = None
    var z:String = _

    case class Phone(number:String) extends Slf4jLogger {
        def call(number:String){
            info("Calling from %s to %s".format(this.number, number))
        }
        def sms(text:String, number:String){
            // do here
        }
    }

    var samsung:Option[Phone] = None

    def show(name:Option[String], age:Option[Int]=None){
        name.map(x => println("name: " + x))
        age.map(x => println("age: " + x))
    }

    def main(args:Array[String]){

        x = Some("hello")

        println(x.getOrElse("kosong bro!"))

        x map { data =>
            // do with data
            println(data)
        }

        samsung = Some(Phone("andrie"))

        samsung map { phone =>
            phone.call("nadhir")
        }

        show(Some("andrie"))
        show(Some("nadir"), Some(21))

    }
}
