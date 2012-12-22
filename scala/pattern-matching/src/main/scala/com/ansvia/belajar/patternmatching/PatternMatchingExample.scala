
package com.ansvia.belajar.patternmatching

import com.ansvia.commons.logging.Slf4jLogger

case class Owner(name:String)
case class Motor(merk:String)
case class Mobil(merk:String, owner:Owner)

object PatternMatchingExample extends Slf4jLogger {


    def main(args:Array[String]){

        val robin = Owner("robin")
        val jazz = Mobil("Honda", robin)

        val m = jazz

        m match {
            case Mobil(merk, owner) if owner.name == "robin" =>
                info("mobil " + merk + " punya " + owner.name)
        }

        val ninja = Motor("Kawasaki")
        val supra = Motor("Honda")

        val x = ninja

        x match {
            case Motor(merk) if merk.startsWith("K") && merk.endsWith("i")  =>
                info("ke dealer kawasaki: " + merk)

            case Motor(merk) if merk == "Honda"  =>
                info("ke dealer honda: " + merk)

//            case Motor("Honda") =>
//                info("ke dealer honda")

//            case Motor(merk) =>
//                info("x = Motor(%s)".format(merk))
        }


        val text = "apa aja"
        val angka = 3

        text match {
            case "Hello" =>
                info("text = Hello")
            case "Test" =>
                info("text = test")
            case apapun =>
                info("gak sama dengan apapun: " + apapun)
        }


        angka match {
            case 1 => {
                info("angka = " + 1)
            }
            case 2 =>
                info("angka = " + 2)

            case 3 =>
                info("angka = " + 3)

        }


    }
}

