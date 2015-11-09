import akka.actor.ActorSystem
import akka.io.IO
import api.UserServiceActor
import core.{MainConfig, UserActor}
import spray.can.Http
import repository.InMemoryUserRepository

/**
 * Main class of the user service application. It starts the Actor System and Spray can server.
 */
object UserServiceApp extends App with MainConfig {

  implicit val system: ActorSystem = ActorSystem("user-system")

  import system.dispatcher

  val userActor = system.actorOf(UserActor.props(new InMemoryUserRepository), "repository")

  val service = system.actorOf(UserServiceActor.props(userActor), "user-service")

  IO(Http) ! Http.Bind(service, "0.0.0.0", config.getInt("app.port"))
}