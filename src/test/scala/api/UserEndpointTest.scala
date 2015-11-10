package api

import akka.actor.ActorDSL._
import com.softwaremill.macwire.tagging.Tagger
import core.UserActor._
import core.UserTag
import core.model.{PageParams, User, UserCreated, UserDeleted}
import spray.http.StatusCodes

class UserEndpointTest extends BaseApiTest {

  "UserEndpoint" should {

    "return OK when getting list of users" in {
      // given
      val userActor = actor(new Act {
        become {
          case GetUsers(PageParams(Some(2), Some(1))) =>
            sender() ! Right(GetUsersResult(List(User("user@email.com", "pass"))))
        }
      }).taggedWith[UserTag]

      val userEndpoint = new UserEndpoint(userActor)

      // when
      Get("/users?skip=2&limit=1") ~> userEndpoint.route ~> check {

        // then
        status mustBe StatusCodes.OK
        responseAs[List[User]] must have size 1
      }
    }

    "return Created when user successfully created" in {
      // given
      val userActor = actor(new Act {
        become {
          case CreateUser(User("user@email.com", "pass")) =>
            sender() ! Right(UserCreated("user@email.com"))
        }
      }).taggedWith[UserTag]

      val userEndpoint = new UserEndpoint(userActor)

      // when
      Post("/users", User("user@email.com", "pass")) ~> userEndpoint.route ~> check {

        // then
        status mustBe StatusCodes.Created
        responseAs[String] must include("user@email.com")
        header("Location").value.value must endWith ("/users/user@email.com")
      }
    }

    "return OK when getting single user" in {
      // given
      val userActor = actor(new Act {
        become {
          case GetUser("user@email.com") =>
            sender() ! Right(GetUserResult(User("user@email.com", "pass")))
        }
      }).taggedWith[UserTag]

      val userEndpoint = new UserEndpoint(userActor)

      // when
      Get("/users/user@email.com") ~> userEndpoint.route ~> check {

        // then
        status mustBe StatusCodes.OK
        responseAs[User] mustBe User("user@email.com", "pass")
      }
    }

    "return No Content when deleting user" in {
      // given
      val userActor = actor(new Act {
        become {
          case DeleteUser("user@email.com") =>
            sender() ! Right(UserDeleted)
        }
      }).taggedWith[UserTag]

      val userEndpoint = new UserEndpoint(userActor)

      // when
      Delete("/users/user@email.com") ~> userEndpoint.route ~> check {

        // then
        status mustBe StatusCodes.NoContent
        responseAs[String] mustBe empty
      }
    }

  }
}
