package repositories

import com.google.inject.{Inject, Singleton}
import model.{UserId, UserRow}
import org.virtuslab.unicorn.{LongUnicornPlayJDBC, UnicornWrapper}

/**
  * A place for all objects directly connected with database.
  *
  * Put your user queries here.
  * Having them in separate in this trait keeps `UserRepository` neat and tidy.
  */
trait UserBaseRepositoryComponent {
  self: UnicornWrapper[Long] =>
  import unicorn._
  import unicorn.driver.api._


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

  object Users {
    val query = TableQuery[Users]
  }

  trait UserQueries{

    protected lazy val userByEmailQuery = for {
      email <- Parameters[String]
      user <- Users.query if user.email === email
    } yield user

  }

  class UserBaseRepository
    extends BaseIdRepository[UserId, UserRow, Users](TableQuery[Users])
      with UserQueries {

    import scala.concurrent.ExecutionContext.Implicits.global

    def findUserByEmail(email: String): DBIO[Option[UserRow]] = {
      userByEmailQuery(email).result.map { foundUsers =>
        if(foundUsers.size > 1) throw new IllegalStateException("...")
        foundUsers.headOption
      }
    }

  }

  val userBaseRepository = new UserBaseRepository

}
@Singleton()
class UserRepository @Inject() (val unicorn: LongUnicornPlayJDBC)
  extends UserBaseRepositoryComponent with UnicornWrapper[Long] {

  import unicorn.driver.api._
  import scala.concurrent.ExecutionContext.Implicits.global

  def findUserByEmail(email: String): DBIO[Option[UserRow]] = {
    userBaseRepository.findUserByEmail(email)
  }

  def save(user: UserRow): DBIO[UserId] = {
    userBaseRepository.save(user)
  }
}