
package com.company.bc.analytics.ignitor

import scala.xml.XML

class ConfParserSpec extends UnitSpec {
  val elem = XML.loadString(
    """
      |<configuration>
      |   <property>
      |      <name>key1</name>
      |      <value>value1</value>
      |      <source>ignored</source>
      |   </property>
      |   <property>
      |      <name>key2</name>
      |      <value>value2</value>
      |      <source>ignored</source>
      |   </property>
      |</configuration>
    """.stripMargin
  )

  "parseConfXmlToMap" should "return a map of size two, and accessible with key 'key1'" in {
    val confMap = ConfParser.parseConfXmlToMap(elem)
    assertResult(2) {
      confMap.size
    }
    assertResult("value1") {
      confMap("key1")
    }
  }

  "fillHadoopConfig" should "return a hadoop conf with access into at least key1 and key2" in {
    val conf = ConfParser.convertToHadoopConf(ConfParser.parseConfXmlToMap(elem))
    assert(conf.size >= 2)
    assertResult("value1") {
      conf.get("key1")
    }
    assertResult("value2") {
      conf.get("key2")
    }
  }
}
