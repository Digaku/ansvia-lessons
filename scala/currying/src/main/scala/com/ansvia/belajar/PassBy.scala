package com.ansvia.belajar

import com.ansvia.commons.logging.Slf4jLogger
import java.util.Random


/**
 * Copyright (C) 2011-2012 Ansvia Inc.
 * User: robin
 * Date: 12/22/12
 * Time: 6:11 PM
 * 
 */


class Printer(name: => String) extends Thread with Slf4jLogger {
    override def run() {
        while(true){
            info("name: " + name)
            Thread.sleep(2000)
        }
    }
}


object PassBy extends Slf4jLogger {

    private var name = "nadir"

    object nameChanger extends Thread {
        private val SQUADS = Array("andrie","nadir","adit","temon")
        override def run() {
            val rnd = new Random()
            rnd.setSeed(10000)

            while(true){

                name = SQUADS(rnd.nextInt(SQUADS.length-1))

                Thread.sleep(1000)
            }
        }
    }

    def main(args:Array[String]){

        val printer = new Printer(name)

        printer.start()
        nameChanger.start()
    }

}

