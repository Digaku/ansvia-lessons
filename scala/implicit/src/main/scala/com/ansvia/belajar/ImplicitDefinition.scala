
package com.ansvia.belajar

import com.ansvia.commons.logging.Slf4jLogger


object ImplicitDefinition extends Slf4jLogger  {


    class AlayConverter(text:String) {
        def alay = {
            text.replaceAll("e", "3").replaceAll("o", "0").replaceAll("i", "1").replaceAll("a", "4")
        }
    }

    implicit def strToAlayConverter(text:String) = new AlayConverter(text)


    case class User(name:String, age:Int){
        def printName() {
            println("nama: " + name)
        }
    }

    case class UserAgePrinter(user:User){
        def printAge() {
            println("age: " + user.age)
        }
    }

    implicit def userToAgePrinter(user:User) = UserAgePrinter(user)


    def whoami(implicit currentUser:User){
        currentUser.printName()
    }

    def makan(implicit currentUser:User){
        println("%s makan".format(currentUser.name))
    }

    def minum(implicit currentUser:User){
        println("%s minum".format(currentUser.name))
    }



    def main(args:Array[String]){

        val temon = User("temon", 17)
        val andrie = User("andrie", 30)

        temon.printName()
        andrie.printName()
        andrie.printAge()

        println("bramoe".alay)
        println("bramoe dan nadir".alay)
        println("bramoe dan andrie".alay)

        {
            implicit val user = temon


            whoami

            makan

            minum

        }

        {
            implicit val user = andrie

            whoami

            makan

            minum
        }


    }
}

