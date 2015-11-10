package core

import akka.actor.{Actor, ActorLogging, Props}
import core.UserActor._
import core.model._
import repository.{UserConflictException, UserRepository}

import scala.util.{Failure, Success}

trait UserTag

/**
 * Contains protocol for UserActor.
 */
object UserActor {

  case class GetUsers(page: PageParams)

  case class GetUsersResult(users: List[User])

  case class CreateUser(user: User)

  case class GetUser(email: String)

  case class GetUserResult(user: User)

  case class DeleteUser(email: String)

  def props(userRepository: UserRepository) = Props(new UserActor(userRepository))

}

/**
 * Handles all user related operations.
 *
 * @param userRepository repository for CRUD on users
 */
class UserActor(userRepository: UserRepository) extends Actor with ActorLogging with ActorTimeout {

  import context.dispatcher

  override def receive: Receive = {

    case CreateUser(user) =>
      val savedSender = sender()

      userRepository.createUser(user) onComplete {
        case Success(result) =>
          savedSender ! Right(result)
        case Failure(ex: UserConflictException) =>
          savedSender ! Left(UserConflict(s"User with email ${user.email} already exists"))
        case Failure(ex) =>
          savedSender ! Left(ServiceInternalError(ex.getMessage))
      }

    case GetUsers(page) =>
      val savedSender = sender()

      userRepository.getUsers(page.getSkip, page.getLimit) onComplete {
        case Success(users) =>
          savedSender ! Right(GetUsersResult(users))
        case Failure(ex) =>
          savedSender ! Left(ServiceInternalError(s"Could not get users, error: ${ex.getMessage}"))
      }

    case GetUser(email) =>
      val savedSender = sender()

      userRepository.getUser(email) onComplete {
        case Success(Some(user)) =>
          savedSender ! Right(GetUserResult(user))

        case Success(None) =>
          savedSender ! Left(UserNotFound(s"User with email $email not found"))

        case Failure(ex) =>
          savedSender ! Left(ServiceInternalError(s"Error reading user with email $email"))
      }


    case DeleteUser(email) =>
      val savedSender = sender()

      userRepository.deleteUser(email) onComplete {
        case Success(result) =>
          savedSender ! Right(result)

        case Failure(ex) =>
          savedSender ! Left(ServiceInternalError(s"Error deleting user with email $email"))

      }
  }
}
