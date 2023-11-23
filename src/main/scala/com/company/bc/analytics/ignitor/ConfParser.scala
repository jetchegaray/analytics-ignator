
package com.company.bc.analytics.ignitor

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path

import scala.xml.Elem
import scalaj.http.{Http, HttpOptions}

object ConfParser {
  val READ_TIMEOUT = 10000;
  val CONN_TIMEOUT = 15000;

  def getHadoopConfFromLocalXml(resources: Seq[String]): Configuration = {
    val conf = new Configuration()
    resources.foreach(r => conf.addResource(new Path(r)))
    conf
  }

  def fetchHadoopConf(url: String): Configuration = {
    convertToHadoopConf(fetchConfMap(url))
  }

  def fetchConfMap(url: String): Map[String, String] = {
    parseConfXmlToMap(Http.get(url).options(HttpOptions.readTimeout(READ_TIMEOUT), HttpOptions.connTimeout(CONN_TIMEOUT))
      .asXml)
  }

  def parseConfXmlToMap(xml: Elem): Map[String, String] = {
    xml \ "property" map (node => (node \ "name" text, node \ "value" text)) toMap
  }

  def convertToHadoopConf(map: Map[String, String]): Configuration = {
    val conf = new Configuration()
    map.foreach(e => {
      conf.set(e._1, e._2)
    })
    conf
  }
}

