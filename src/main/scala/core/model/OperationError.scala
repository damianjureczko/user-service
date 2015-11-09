package core.model

/**
 * Base class for all errors which might happen in the application and be returned from actors.
 * @param message details about error
 */
sealed abstract class OperationError(message: String) {

  def getMessage: String = message
}

/**
 * Generic internal error.
 */
case class ServiceInternalError(message: String) extends OperationError(message)

/**
 * User already exists error.
 */
case class UserConflict(message: String) extends OperationError(message)

/**
 * User not found error.
 */
case class UserNotFound(message: String) extends OperationError(message)
