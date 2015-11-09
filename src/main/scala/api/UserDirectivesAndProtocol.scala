package api

import core.UserActor.{GetUserResult, GetUsersResult}
import core.model._
import spray.http.StatusCodes._
import spray.http.{MediaTypes, StatusCode, StatusCodes}
import spray.httpx.SprayJsonSupport
import spray.httpx.marshalling.ToResponseMarshaller
import spray.json._
import spray.routing._

trait UserDirectivesAndProtocol extends Directives with DefaultJsonProtocol with SprayJsonSupport {

  def pageParams: Directive1[PageParams] = parameter(('skip.as[Int].?, 'limit.as[Int].?)).as(PageParams)

  implicit val userJsonFormat = jsonFormat2(User)

  implicit val userResultJsonFormat = new RootJsonWriter[GetUserResult] {
    override def write(obj: GetUserResult): JsValue = obj.user.toJson
  }

  implicit val usersResultJsonFormat = new RootJsonWriter[GetUsersResult] {
    override def write(obj: GetUsersResult): JsValue = obj.users.toJson
  }

  private def successToResponse: PartialFunction[OperationSuccess, (StatusCode, String)] = {
    case UserCreated(email) =>
      StatusCodes.Created -> s""" { "$email" : "created" } """

    case UserDeleted =>
      StatusCodes.NoContent -> ""
  }

  implicit val successMarshaller =
    ToResponseMarshaller.delegate[OperationSuccess, (StatusCode, String)](MediaTypes.`application/json`)(successToResponse)

  private def errorToResponse: PartialFunction[OperationError, (StatusCode, String)] = {
    case ServiceInternalError(message) =>
      StatusCodes.InternalServerError -> errorBody(message)

    case UserConflict(message) =>
      StatusCodes.Conflict -> errorBody(message)

    case UserNotFound(message) =>
      StatusCodes.NotFound -> errorBody(message)
  }

  private def errorBody(message: String): String = s""" { "errorMessage" : "$message" } """

  implicit val errorMarshaller =
    ToResponseMarshaller.delegate[OperationError, (StatusCode, String)](MediaTypes.`application/json`)(errorToResponse)

  def completeWithInternalServerError(ex: Throwable): Route =
    complete(InternalServerError -> errorBody(ex.getMessage))


}
