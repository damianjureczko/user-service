package api

import akka.actor.ActorDSL._
import core.UserActor.{CreateUser, GetUser, GetUserResult}
import core.model.User
import org.scalatest.{MustMatchers, OptionValues, WordSpecLike}
import spray.http.StatusCodes
import spray.testkit.ScalatestRouteTest

class UserEndpointTest extends WordSpecLike with MustMatchers with ScalatestRouteTest with OptionValues
with UserDirectivesAndProtocol {

  "UserEndpoint" should {

    "return OK when getting user" in {
      // given
      val userActor = actor(new Act {
        become {
          case GetUser("user@email.com") => sender() ! Right(GetUserResult(User("user@email.com", "pass")))
        }
      })

      val userEndpoint = new UserEndpoint(userActor)

      // when
      Get("/users/user@email.com") ~> userEndpoint.route ~> check {

        // then
        status mustBe StatusCodes.OK
        responseAs[User] mustBe User("user@email.com", "pass")
      }
    }

    "return Created when user successfully created" in {
//      // given
//      val userActor = actor(new Act {
//        become {
//          case CreateUser("user@email.com") => sender() ! Right(GetUserResult(User("user@email.com", "pass")))
//        }
//      })
//
//      val userEndpoint = new UserEndpoint(userActor)
//
//      // when
//      Get("/users/user@email.com") ~> userEndpoint.route ~> check {
//
//        // then
//        status mustBe StatusCodes.OK
//        responseAs[String] must include("user@email.com")
//      }
    }

  }
}
