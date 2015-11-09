package core

import akka.actor.{Actor, ActorLogging, Props}
import akka.pattern.pipe
import core.UserActor._
import core.model._
import repository.{UserConflictException, UserRepository}

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

      userRepository.createUser(user) recover {
        case ex: UserConflictException =>
          UserConflict(s"User with email ${user.email} already exists")
        case ex: Throwable =>
          ServiceInternalError(ex.getMessage)
      } pipeTo sender()

    case GetUsers(page) =>

      userRepository.getUsers(page.getSkip, page.getLimit) map { users =>
        GetUsersResult(users)
      } recover {
        case ex: Throwable =>
          ServiceInternalError(s"Could not get users, error: ${ex.getMessage}")
      } pipeTo sender()

    case GetUser(email) =>

      userRepository.getUser(email) map {
        case Some(user) => GetUserResult(user)
        case None => UserNotFound(s"User with email $email not found")
      } recover {
        case ex: Throwable =>
          ServiceInternalError(s"Error reading user with email $email")
      } pipeTo sender()


    case DeleteUser(email) =>

      userRepository.deleteUser(email) recover {
        case ex: Throwable =>
          ServiceInternalError(s"Error deleting user with email $email")

      }
  }
}
