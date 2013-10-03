package service

import org.specs2.mutable.Specification
import play.api.test.WithApplication
import play.api.db.slick.DB
import model.User
import scala.slick.session.Session

class UserServiceTest extends Specification {

  "Users Service" should {

    "save and query users in" in new WithApplication {
      DB.withSession {
        implicit session: Session =>
          object UsersService extends UsersService

          val user = User(None, "test@email.com", "Krzysztof", "Nowak")
          val userId = UsersService save user
          val userOpt = UsersService findById userId

          userOpt.map(_.email) must be_=== (Some(user.email))
          userOpt.map(_.firstName) must be_=== (Some(user.firstName))
          userOpt.map(_.lastName) must be_=== (Some(user.lastName))
          userOpt.flatMap(_.id) must not be_=== None
      }
    }

  }
}