
package com.ansvia.belajar.commons

import com.ansvia.commons.logging.Slf4jLogger


// map
// [1,2,3].map(_ + 1)
// => 2, 3, 4

// flatMap
// [[1],[2],[3,4,5,[7,8,[9,10]]],[6]].flatMap(_ + 1)
// => [2,3,4,5,6,8,10,11,7]

// reduce
// [1,2,3,4].reduce(_ * _)
// => 24

object CommonsExample2 extends Slf4jLogger {

  lazy val tag = "jogja, oke, Kuliner makan      , HUMOR, dancuk, asem, unggas"
  lazy val restrictedKeywords = Seq("dancuk", "asem", "makan")

  def main(args:Array[String]){

    val tags = tag.split(",")
      .flatMap(_.toLowerCase.trim.split(" "))
      .filter { text =>
        !restrictedKeywords.contains(text)
      }
      .sorted
      .reverse

    val (vokal, konsonan) = tags.partition { text =>
      text.startsWith("a") || text.startsWith("u") || text.startsWith("e") || text.startsWith("o") || text.startsWith("i")
    }

    println("vokal: " + vokal.reduce(_ + ", " + _))
    println("konsonan: " + konsonan.reduce(_ + ", " + _))

    val x = Array[Int](1)

    val z = x.foldLeft(1)(_ + _)

    println(z)

//      .reduceLeft { (a, b) =>
//        a + "," + b
//      }




//    println(tags)

  }

}
