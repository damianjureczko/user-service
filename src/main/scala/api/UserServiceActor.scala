package api

import akka.actor._
import spray.http.MediaTypes._
import spray.routing._

import scala.concurrent.ExecutionContext

object UserServiceActor {

  def props(userActor: ActorRef) = Props(new UserServiceActor(userActor))
}

/**
 * Main actor receiving request to user service.
 *
 * @param userActor global actor handling user related requests
 */
class UserServiceActor(userActor: ActorRef) extends Actor with UserServiceApi {

  import context.dispatcher

  def actorRefFactory: ActorContext = context

  def receive: Receive = runRoute(route(userActor))
}

/**
 * Defines API of user service.
 */
trait UserServiceApi extends HttpService {

  def route(userActor: ActorRef)(implicit executionContext: ExecutionContext) =
    respondWithMediaType(`application/json`) {
      new UserEndpoint(userActor).route
    }
}
