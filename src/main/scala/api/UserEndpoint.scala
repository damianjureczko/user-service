package api

import akka.actor.{ActorRef, ActorRefFactory}
import com.softwaremill.macwire.tagging.@@
import core.UserActor._
import core.UserTag
import core.model.User

/**
 * Endpoint to handle request to /users/... path.
 *
 * @param userActor global actor handling user related requests
 * @param actorRefFactory factory to create new actors per request
 */
class UserEndpoint(userActor: ActorRef @@ UserTag)(implicit actorRefFactory: ActorRefFactory) extends UserDirectivesAndProtocol {

  val route =
    path("users") {
      get {
        pageParams { page =>
          ctx => actorRefFactory.actorOf(UserRequestActor.props(ctx, userActor)) ! GetUsers(page)
        }
      } ~
        post {
          entity(as[User]) { user =>
            ctx => actorRefFactory.actorOf(UserRequestActor.props(ctx, userActor)) ! CreateUser(user)
          }
        }
    } ~
      path("users" / Segment) { email =>
        get {
          ctx => actorRefFactory.actorOf(UserRequestActor.props(ctx, userActor)) ! GetUser(email)
        } ~
          delete {
            ctx => actorRefFactory.actorOf(UserRequestActor.props(ctx, userActor)) ! DeleteUser(email)
          }
      }

}
