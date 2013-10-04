package model

import db.{IdTable, Id, IdCompanion, BaseId}
import scala.slick.session.Session

/** Id class for type-safe joins and queries. */
case class UserId(id: Long) extends AnyVal with BaseId

/** Companion object for id class, extends IdMapping
  * and brings all required implicits to scope when needed.
  */
object UserId extends IdCompanion(Users)

/** User entity.
  *
  * @param id user id
  * @param email user email address
  * @param lastName lastName
  * @param firstName firstName
  */
case class User(id: Option[UserId],
                email: String,
                firstName: String,
                lastName: String) extends Id[UserId]

/** Table definition for users. */
object Users extends IdTable[UserId, User]("users", UserId.apply) {

  def email = column[String]("email", O.NotNull)

  def firstName = column[String]("first_name", O.NotNull)

  def lastName = column[String]("last_name", O.NotNull)

  def base = email ~ firstName ~ lastName

  def * = id.? ~: base <> (User.apply _, User.unapply _)

  def insertOne(elem: User)(implicit session: Session): UserId =
    saveBase(base, User.unapply _)(elem)
}