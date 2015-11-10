package api

import akka.actor.ActorRef
import akka.pattern.ask
import core.UserActor._
import core.model.{OperationError, User, UserCreated, UserDeleted}
import spray.http.HttpHeaders.Location

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

/**
 * Endpoint to handle request to /users/... path.
 *
 * @param userActor global actor handling user related requests
 * @param executionContext execution context to run asynchronous operation
 */
class UserEndpoint(userActor: ActorRef)(implicit executionContext: ExecutionContext)
  extends UserDirectivesAndProtocol with EndpointTimeOut {

  val route =
    path("users") {
      get {
        pageParams { page =>
          onComplete((userActor ? GetUsers(page)).mapTo[Either[OperationError, GetUsersResult]]) {
            case Success(result) =>
              complete(result)
            case Failure(ex) =>
              completeWithInternalServerError(ex)
          }
        }
      } ~
        post {
          requestUri { uri =>
            entity(as[User]) { user =>
              onComplete((userActor ? CreateUser(user)).mapTo[Either[OperationError, UserCreated]]) {
                case Success(Right(result)) =>
                  respondWithHeader(Location(s"${uri.scheme}:${uri.authority}/users/${user.email}")) {
                    complete(result)
                  }
                case Success(Left(problem)) =>
                  complete(problem)
                case Failure(ex) =>
                  completeWithInternalServerError(ex)
              }
            }
          }
        }
    } ~
      path("users" / Segment) { email =>
        get {
          onComplete((userActor ? GetUser(email)).mapTo[Either[OperationError, GetUserResult]]) {
            case Success(result) =>
              complete(result)
            case Failure(ex) =>
              completeWithInternalServerError(ex)
          }
        } ~
          delete {
            onComplete((userActor ? DeleteUser(email)).mapTo[Either[OperationError, UserDeleted.type]]) {
              case Success(result) =>
                complete(result)
              case Failure(ex) =>
                completeWithInternalServerError(ex)
            }
          }
      }
}
