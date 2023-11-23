
package com.company.bc.analytics.ignitor

import org.scalatest._
import org.scalatest.mock.MockitoSugar

abstract class UnitSpec extends FlatSpec with Matchers with
  OptionValues with Inside with Inspectors with BeforeAndAfter with MockitoSugar with PrivateMethodTester {
  def getTestClassesPath: String = {
    new java.io.File("target/test-classes/").toString + "/"
  }
}

