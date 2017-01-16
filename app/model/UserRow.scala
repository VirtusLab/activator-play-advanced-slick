package model

import org.virtuslab.unicorn.{BaseId, WithId}
import org.virtuslab.unicorn.LongUnicornPlayIdentifiers._

/** Id class for type-safe joins and queries. */
case class UserId(id: Long) extends AnyVal with BaseId[Long]

/** Companion object for id class, extends IdCompanion
  * and brings all required implicits to scope when needed.
  */
object UserId extends IdCompanion[UserId]

/** User entity.
  *
  * @param id user id
  * @param email user email address
  * @param lastName lastName
  * @param firstName firstName
  */
case class UserRow(id: Option[UserId],
                   email: String,
                   firstName: String,
                   lastName: String) extends WithId[Long, UserId]