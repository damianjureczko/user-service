package api

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.softwaremill.macwire.tagging.@@
import core.UserActor.{GetUserResult, GetUsersResult}
import core.UserTag
import core.model.{OperationError, OperationSuccess, ServiceInternalError, UserCreated}
import spray.http.HttpHeaders.Location
import spray.routing.RequestContext

object UserRequestActor {

  def props(requestContext: RequestContext, userActor: ActorRef @@ UserTag) = Props(new UserRequestActor(requestContext, userActor))
}

/**
 * Actor to handle single user related request.
 *
 * @param requestContext context of a request
 * @param userActor global actor handling user related requests
 */
class UserRequestActor(requestContext: RequestContext, userActor: ActorRef @@ UserTag) extends Actor with UserDirectivesAndProtocol with ActorLogging {

  override def receive: Receive = default

  private def default: Receive = {

    case msg =>
      userActor ! msg
      context become waitingForResponse
  }

  private def waitingForResponse: Receive = {

    case GetUserResult(user) =>
      requestContext.complete(user)
      context stop self

    case GetUsersResult(users) =>
      requestContext.complete(users)
      context stop self

    case result: UserCreated =>
      val uri = requestContext.request.uri
      requestContext
        .withHttpResponseHeadersMapped(Location(s"${uri.scheme}:${uri.authority}/users/${result.email}") :: _)
        .complete(result)
      context stop self

    case success: OperationSuccess =>
      requestContext.complete(success)
      context stop self

    case error: OperationError =>
      requestContext.complete(error)
      context stop self

    case _ =>
      requestContext.complete(ServiceInternalError("Unknown message received from user actor"))
      context stop self
  }
}
