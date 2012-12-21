
package com.ansvia.belajar.lucene.zoie

import proj.zoie.api.indexing.{IndexReaderDecorator, ZoieIndexableInterpreter, ZoieIndexable}
import org.apache.lucene.document.Field.{Store, Index}
import org.apache.lucene.document.{Document, Field}
import proj.zoie.api.indexing.ZoieIndexable.IndexingReq
import java.io.File
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.util.Version
import org.apache.lucene.search._
import org.apache.lucene.index.{Term, MultiReader, IndexReader, FilterIndexReader}
import proj.zoie.api.ZoieIndexReader
import proj.zoie.impl.indexing.ZoieSystem
import java.util.Comparator
import java.util
import proj.zoie.api.DataConsumer.DataEvent
import collection.JavaConversions
import com.ansvia.commons.logging.Slf4jLogger
import scala.util.Random


object ZoieExample {

  case class Data(id:Long, content:String)

  class DataIndexable(data:Data) extends ZoieIndexable {

    def getUID = data.id

    def isDeleted = data.content.startsWith("**MARK-FOR-DELETE**")

    def isSkip = data.content.startsWith("**MARK-FOR-SKIP**")

    def buildIndexingReqs() = {
      val doc = new Document()
      doc.add(new Field("content", data.content, Store.NO, Index.ANALYZED))

      Array(new IndexingReq(doc))
    }

    def isStorable = false

    def getStoreValue = null
  }

  class DataIndexableInterpreter extends ZoieIndexableInterpreter[Data] {
    def convertAndInterpret(src: Data) = new DataIndexable(src)
  }

  class Fir(in:IndexReader) extends FilterIndexReader(in){
    def getInnerReader = in
  }
  class FirDecorator() extends IndexReaderDecorator[Fir]{
    def decorate(indexReader: ZoieIndexReader[Fir]) = new Fir(indexReader)

    def redecorate(decorated: Fir, copy: ZoieIndexReader[Fir], withDeletes: Boolean) = {
      decorated
    }

    def setDeleteSet(reader: Fir, docIds: DocIdSet) {}
  }

  def main(args:Array[String]){

    val indexDir = new File("/tmp/zoie-test")

    val analyzer = new StandardAnalyzer(Version.LUCENE_35)
    val similarity = new DefaultSimilarity

    val intrepreter = new DataIndexableInterpreter

    val decorator = new FirDecorator()

    val system = new ZoieSystem(
    indexDir,
    intrepreter,
    decorator,
    analyzer,
    similarity,
    1000,
    300000,
    true,
    new Comparator[String] {
      def compare(p1: String, p2: String) = p1.compareTo(p2)
    },
    true
    )

    system.start()

    val RANDOM_TEXT = Array("hello","keren","text","robin","anis")
    def randtext = {
      val rnd = new Random()
      RANDOM_TEXT.apply(rnd.nextInt(RANDOM_TEXT.length-1))
    }

    object indexingThread extends Thread(){
      override def run() {

        var version = 1

        while(true){
          val datas = (1 to 100) map( i => new Data(i, "text index-ke-" + i + ": " + randtext) )
          val eventList = new util.ArrayList[DataEvent[Data]](datas.length)
          for (data <- datas){
            eventList.add(new DataEvent[Data](data, version.toString))
          }
          system.consume(eventList)
          version = version + 1
        }

      }
    }

    object searchingThread extends Thread with Slf4jLogger {
      override def run() {

        while(true){
          debug("searching %s...".format(randtext))

          val readerList = system.getIndexReaders
          val decoratedReaders = ZoieIndexReader.extractDecoratedReaders(readerList)
          val subReaderAccessor = ZoieIndexReader.getSubReaderAccessor(decoratedReaders)

          val x:Array[IndexReader] = JavaConversions.collectionAsScalaIterable(readerList).toArray
          val reader = new MultiReader(x, false)

          val searcher = new IndexSearcher(reader)

          val docs = searcher.search(new TermQuery(new Term("content", randtext)),10)
          val scoreDocs = docs.scoreDocs

          for (sd <- scoreDocs){
            val docid = sd.doc

            val readerInfo = subReaderAccessor.getSubReaderInfo(docid)

            val uid = readerInfo.subreader.getInnerReader.asInstanceOf[ZoieIndexReader[Fir]].getUID(docid)

            println("got uid: " + uid)
          }
          Thread.sleep(2000)
        }

      }
    }

    indexingThread.start()
    searchingThread.start()


  }
}