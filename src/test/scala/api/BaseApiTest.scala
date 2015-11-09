package api

import org.scalatest.{BeforeAndAfterAll, MustMatchers, OptionValues, WordSpecLike}
import spray.testkit.ScalatestRouteTest

class BaseApiTest
  extends WordSpecLike
  with MustMatchers
  with ScalatestRouteTest
  with OptionValues
  with BeforeAndAfterAll
  with UserDirectivesAndProtocol {

}
