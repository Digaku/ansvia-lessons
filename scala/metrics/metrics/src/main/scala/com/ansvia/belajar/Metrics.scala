package com.ansvia.belajar

import com.yammer.metrics.scala.Instrumented
import com.yammer.metrics.reporting.ConsoleReporter
import java.util.concurrent.TimeUnit

object Metrics extends Instrumented {

    lazy val reporter = ConsoleReporter.enable(metricsRegistry, 5, TimeUnit.SECONDS)

    def main(args:Array[String]){

        reporter

        while(true){
            metrics.timer("sleeper").time {
                val min = 1000
                val max = 20000
                val rand:Long = (min + (math.random * ((max - min) + 1)).toInt)
                println("seep in %d ms...".format(rand))
                Thread.sleep(rand.toLong)
                println("wake up after %d ms!".format(rand))
            }
        }
    }

}



