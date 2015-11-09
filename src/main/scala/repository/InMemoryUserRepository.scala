package repository

import core.model.{User, UserCreated, UserDeleted}

import scala.concurrent.{ExecutionContext, Future, Promise}

class InMemoryUserRepository(implicit ec: ExecutionContext) extends UserRepository {

  private var users: Map[String, User] = Map()

  val BannedEmails = """^.*@banned.com$""".r

  implicit val usersOrder = Ordering.by[(String, User), String](_._1)


  def getUsers(skip: Int, limit: Int): Future[List[User]] = {
    Future {
      users.toList.sorted.map(_._2).slice(skip, skip + limit)
    }
  }

  def createUser(user: User): Future[UserCreated] = {
    val result = Promise[UserCreated]()

    user.email match {
      case BannedEmails(_*) =>
        result.failure(new RepositoryException("banned email"))
      case _ =>
        users.contains(user.email) match {
          case true =>
            result.failure(new UserConflictException("user with given email already exists"))
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
