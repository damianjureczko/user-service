package core

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfterAll, MustMatchers, WordSpecLike}

class BaseActorTest
  extends TestKit(ActorSystem("test-system"))
  with WordSpecLike
  with MockFactory
  with ImplicitSender
  with MustMatchers
  with BeforeAndAfterAll {

  override protected def afterAll() {
    super.afterAll()
    system.shutdown()
  }

}
