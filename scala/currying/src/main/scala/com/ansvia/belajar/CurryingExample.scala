
package com.ansvia.belajar

import com.ansvia.commons.logging.Slf4jLogger
import java.io.{BufferedReader, FileReader, File}

object CurryingExample extends Slf4jLogger {

    def readFile(filePath:String)(func: (BufferedReader) => Unit){
        var br:BufferedReader = null
        try {

            br = new BufferedReader(new FileReader(new File(filePath)))

            func(br)

        }catch{
            case e:Exception =>
                error(e.getMessage)
        }finally{
            if (br!=null)
                br.close()
        }
    }

	def main(args: Array[String]) {

        val SQUAD = Array(
            "adit", "andrie", "surya", "temon"
        )

        readFile("/tmp/temon.txt"){ rb =>
            info(rb.readLine())
        }
        readFile("/tmp/temon-2.txt"){ rb =>
            info(rb.readLine())
        }
        readFile("/tmp/temon.txt")(rb => info(rb.readLine()))

        info("done.")
	}

}
