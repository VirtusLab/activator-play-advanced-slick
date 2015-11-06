package repositories

import org.virtuslab.unicorn.LongUnicornPlay._
import org.virtuslab.unicorn.LongUnicornPlay.driver.api._
import model.{UserRow, Users, UserId}

/**
  * A place for user queries.
  *
  * Put your user queries here.
  * Having them in separate in this trait keeps `UserRepository` neat and tidy.
  */
private[repositories] trait UserQueries {

  protected lazy val userByEmailQuery = for {
    email <- Parameters[String]
    user <- Users.query if user.email === email
  } yield user
}

/**
  * Repository for users.
  *
  * It brings all base service methods with it from [[org.virtuslab.unicorn.repositories.IdRepositories.BaseIdRepository]].
  * You can add your methods as well.
  */
class UserRepository
  extends BaseIdRepository[UserId, UserRow, Users](Users.query)
  with UserQueries {

  import scala.concurrent.ExecutionContext.Implicits.global

  def findUserByEmail(email: String): DBIO[Option[UserRow]] = {
    userByEmailQuery(email).result.map { foundUsers =>
      if(foundUsers.size > 1) throw new IllegalStateException("...")
      foundUsers.headOption
    }
  }

}
