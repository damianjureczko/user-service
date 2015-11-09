package api

import akka.actor._
import com.softwaremill.macwire.tagging.@@
import core.UserTag
import spray.http.MediaTypes._
import spray.routing._

object UserServiceActor {

  def props(userActor: ActorRef @@ UserTag) = Props(new UserServiceActor(userActor))
}

/**
 * Main actor receiving request to user service.
 *
 * @param userActor global actor handling user related requests
 */
class UserServiceActor(userActor: ActorRef @@ UserTag) extends Actor with UserServiceApi {

  def actorRefFactory: ActorContext = context

  def receive: Receive = runRoute(route(userActor))
}

/**
 * Defines API of user service.
 */
trait UserServiceApi extends HttpService {

  def route(userActor: ActorRef @@ UserTag)(implicit actorRefFactory: ActorRefFactory) =
    respondWithMediaType(`application/json`) {
      new UserEndpoint(userActor).route
    }
}
