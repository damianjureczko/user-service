package core.model

/**
 * Marker trait for all generic successful operation inside the application.
 */
sealed trait OperationSuccess

/**
 * User was successfully created.
 */
case class UserCreated(email: String) extends OperationSuccess

/**
 * User was successfully deleted.
 */
case object UserDeleted extends OperationSuccess
