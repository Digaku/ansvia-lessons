
package com.ansvia.belajar

import com.ansvia.commons.logging.Slf4jLogger


class Motor extends Slf4jLogger {
  debug("class Motor diinstansiasi")
}

object LoggingExample extends Slf4jLogger  {
    def main(args:Array[String]){
        info("Hello Ansvia!")
        error("Oops ada error!")
        warn("Bahaya nih!")

      val motor = new Motor()

    }
}

