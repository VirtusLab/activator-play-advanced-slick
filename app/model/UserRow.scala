package model

import org.virtuslab.unicorn.LongUnicornPlay._
import org.virtuslab.unicorn.LongUnicornPlay.driver.simple._

/** Id class for type-safe joins and queries. */
case class UserId(id: Long) extends AnyVal with BaseId

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
                   lastName: String) extends WithId[UserId]

/** Table definition for users. */
class Users(tag: Tag) extends IdTable[UserId, UserRow](tag, "USERS") {

  /** By definition id column is inserted as lowercase 'id',
    * if you want to change it, here is your setting.
    */
  protected override val idColumnName = "ID"

  def email = column[String]("EMAIL")

  def firstName = column[String]("FIRST_NAME")

  def lastName = column[String]("LAST_NAME")

  override def * = (id.?, email, firstName, lastName) <>(UserRow.tupled, UserRow.unapply)

}