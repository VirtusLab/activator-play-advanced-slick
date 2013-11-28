package service

import org.specs2.mutable.Specification
import play.api.test.WithApplication
import play.api.db.slick.DB
import model.User
import scala.slick.session.Session

class UsersServiceTest extends Specification {

  "Users Service" should {

    "save and query users" in new WithApplication {
      DB.withSession {
        implicit session: Session =>
          object UsersService extends UsersService

          val user = User(None, "test@email.com", "Krzysztof", "Nowak")
          val userId = UsersService save user
          val userOpt = UsersService findById userId

          userOpt.map(_.email) must beSome(user.email)
          userOpt.map(_.firstName) must beSome(user.firstName)
          userOpt.map(_.lastName) must beSome(user.lastName)
          userOpt.flatMap(_.id) must not be_=== None
      }
    }
  }
}