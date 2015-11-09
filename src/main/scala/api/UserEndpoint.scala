package api

import akka.actor.ActorRef
import akka.pattern.ask
import core.UserActor._
import core.model.{OperationError, User, UserCreated, UserDeleted}
import spray.httpx.marshalling.ToResponseMarshallable
import spray.routing.Route

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success, Try}

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
          entity(as[User]) { user =>
            onComplete((userActor ? CreateUser(user)).mapTo[Either[OperationError, UserCreated]]) {
              case Success(result) =>
                complete(result)
              case Failure(ex) =>
                completeWithInternalServerError(ex)
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

  private def handleResponse[T <: ToResponseMarshallable](res: Try[T]): Route = {
    res match {
      case Success(result) =>
        complete(result)
      case Failure(ex) =>
        completeWithInternalServerError(ex)
    }
  }
}
