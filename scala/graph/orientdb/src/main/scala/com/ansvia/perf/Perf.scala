package com.ansvia.perf


/**
 * Copyright (C) 2011-2012 Ansvia Inc.
 * User: robin
 * Date: 12/27/12
 * Time: 12:09 AM
 *
 */
object Perf {

    /**
     * Untuk performance testing dengan mengevaluasi
     * lama waktu eksekusi sebuah routin.
     * @param title judul dari performance test-nya.
     * @param routineFunction rutin yang akan dieksekusi dalam bentuk fungsi.
     * @return
     */
    def timing(title:String = "title")(routineFunction: => Unit){
        println(title + ":")

        val start = System.currentTimeMillis()

        routineFunction

        val totalTime = System.currentTimeMillis() - start
        println(title + " - done in " + totalTime + "ms")
    }
}
