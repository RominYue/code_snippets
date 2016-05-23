package com.clearcut.nlp

import java.util.Properties

import scala.collection.JavaConversions._

import edu.hit.ir.ltp4j._

import scala.io.Source

class DocumentParser(props: Properties) {

    def init = {
        //val model_path = Properties.getProperty("model")
        val model_path = "/home/yomin/spouse_example-0.8-STABLE/udf/bazaar/zh_parser/lib/models/"
        Segmentor.create(model_path + "cws.model")
        Postagger.create(model_path + "pos.model")
       	NER.create(model_path + "ner.model")
        Parser.create(model_path + "parser.model")
    }

    def close = {
        Segmentor.release()
        Postagger.release()
        NER.release()
        Parser.release()
    }

    def parseDocumentString(doc: String) = {

        val document = doc.replaceAll("""\[""", "(").replaceAll("""\]""", ")")

        //get sentences
        var sentences = new java.util.ArrayList[String]()
        SplitSentence.splitSentence(document,sentences)

        val sentenceResults = sentences.zipWithIndex.map { case(sentence, sentIdx) =>
            val content = sentence.toString
            //Console.println(content)

            var words = new java.util.ArrayList[String]()
            Segmentor.segment(content,words)
            //Console.println(words)

            var postags = new java.util.ArrayList[String]()
            Postagger.postag(words, postags)
            //Console.println(postags)
            
            var ners = new java.util.ArrayList[String]()
            NER.recognize(words, postags, ners)
            //Console.println(ners)

            var dep_Parents = new java.util.ArrayList[Integer]()
            var depLabels = new java.util.ArrayList[String]()
            Parser.parse(words, postags, dep_Parents, depLabels)
            var depParents = dep_Parents.toList.map(x => x.toInt)
            //Console.println(depParents)
            //Console.println(depLabels)
	     
            SentenceParseResult(
                content,
                words.toList,
                postags.toList,
                ners.toList,
                depLabels.toList,
                depParents.toList
            )
        }

    DocumentParseResult(sentenceResults.toList)
  }
  /**
    Construct a Postgres-acceptable array in the TSV format, from a list
  */
  def list2TSVArray(arr: List[String]) : String = {
    return arr.map( x =>
      // Replace '\' with '\\\\' to be accepted by COPY FROM
      // Replace '"' with '\\"' to be accepted by COPY FROM
      if (x.contains("\\"))
        "\"" + x.replace("\\", "\\\\\\\\").replace("\"", "\\\\\"") + "\""
      else
        "\"" + x + "\""
      ).mkString("{", ",", "}")
  }

  def intList2TSVArray(arr: List[Int]) : String = {
    return arr.map( x =>
      "" + x
      ).mkString("{", ",", "}")
  }

  def string2TSVString(str: String) : String = {
    if (str.contains("\\"))
      str.replace("\\", "\\\\")
    else
      str
  }

  // NOTE: an alternative would be to quote the field correctly
  // http://stackoverflow.com/questions/3089077/new-lines-in-tab-delimited-or-comma-delimtted-output
  def replaceChars(str: String) : String = {
    str.replace("\n", " ").replace("\t", " ")
  }
}
