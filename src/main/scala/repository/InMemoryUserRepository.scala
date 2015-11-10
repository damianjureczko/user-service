package repository

import core.model.{User, UserCreated, UserDeleted}

import scala.concurrent.{ExecutionContext, Future, Promise}

/**
 * "In memory" implementation of UserRepository trait, only for purpose of demonstration.
 *
 * @param ec execution context to run asynchronous operation
 */
class InMemoryUserRepository(implicit ec: ExecutionContext) extends UserRepository {

  private var users: Map[String, User] = Map()

  val ErrorEmails = """^.*@error.com$""".r

  implicit val usersOrder = Ordering.by[(String, User), String](_._1)


  def getUsers(skip: Int, limit: Int): Future[List[User]] = {
    Future {
      users.toList.sorted.map(_._2).slice(skip, skip + limit)
    }
  }

  def createUser(user: User): Future[UserCreated] = {
    val result = Promise[UserCreated]()

    user.email match {
      case ErrorEmails(_*) =>
        result.failure(new RepositoryException(s"Error creating user with email '${user.email}'"))
      case _ =>
        users.contains(user.email) match {
          case true =>
            result.failure(new UserConflictException(s"User with email '${user.email}' already exists"))
          case false =>
            users += user.email -> user
            result.success(UserCreated(user.email))
        }
    }

    result.future
  }

  def getUser(email: String): Future[Option[User]] = Future {
    users.get(email)
  }

  override def deleteUser(email: String): Future[UserDeleted.type] = Future {
    users -= email
    UserDeleted
  }
}
