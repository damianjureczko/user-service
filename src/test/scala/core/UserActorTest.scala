package core

import core.UserActor.{GetUsers, GetUsersResult}
import core.model.{PageParams, ServiceInternalError, User}
import repository.{RepositoryException, UserRepository}

import scala.concurrent.Future

class UserActorTest extends BaseActorTest {

  val userRepository = stub[UserRepository]

  "UserActor" should {

    "return list of users" in {
      // given
      (userRepository.getUsers _)
        .when(3, 2)
        .returns(Future.successful(List(User("user1@gmail.com", "pass"), User("user2@gmail.com", "secret"))))

      val userActor = system.actorOf(UserActor.props(userRepository))

      // when
      userActor ! GetUsers(PageParams(Some(3), Some(2)))

      // then
      expectMsg(Right(GetUsersResult(List(User("user1@gmail.com", "pass"), User("user2@gmail.com", "secret")))))
    }

    "return service internal error when getting user failes" in {
      // given
      (userRepository.getUsers _)
        .when(3, 2)
        .returns(Future.failed(new RepositoryException("some error")))

      val userActor = system.actorOf(UserActor.props(userRepository))

      // when
      userActor ! GetUsers(PageParams(Some(3), Some(2)))

      // then
      expectMsgPF() {
        case Left(ServiceInternalError(_)) => true
      }
    }
  }
}
