package repository

import core.model.{User, UserCreated, UserDeleted}

import scala.concurrent.Future

/**
 * Repository to store users.
 */
trait UserRepository {

  /**
   * Returns list of users.
   *
   * @param skip a number of users to skip from the begining
   * @param limit a number of users to return
   * @return future with list of users if operation succeed
   */
  def getUsers(skip: Int, limit: Int): Future[List[User]]

  /**
   * Creates new user.
   * Fails with UserConflictException is user already exists.
   *
   * @param user a new user to create
   * @return future with confirmation that operation succeed
   */
  def createUser(user: User): Future[UserCreated]

  /**
   * Returns user with given email.
   * Fails with UserNotFoundException is user was not found.
   *
   * @param email an email of user to be returned
   * @return future with user if operation succeed.
   */
  def getUser(email: String): Future[Option[User]]

  /**
   * Removes user with given email
   *
   * @param email an email of user to be deleted
   * @return future with confirmation that operation succeed
   */
  def deleteUser(email: String): Future[UserDeleted.type]
}
