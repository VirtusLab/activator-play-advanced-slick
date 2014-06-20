package service

import org.specs2.mutable.Specification
import play.api.test.WithApplication
import play.api.db.slick.DB
import model.{Users, User}
import org.virtuslab.unicorn.UnicornPlay._
import org.virtuslab.unicorn.UnicornPlay.driver.simple._
import repositories.UserRepository

class UsersServiceTest extends Specification {

  "Users Service" should {

    "save and query users" in new WithApplication {
      DB.withSession {
        implicit session: Session =>
          val userQueries = TableQuery[Users]
          val userRepository = new UserRepository(userQueries)

          val user = User(None, "test@email.com", "Krzysztof", "Nowak")
          val userId = userRepository save user
          val userOpt = userRepository findById userId

          userOpt.map(_.email) must beSome(user.email)
          userOpt.map(_.firstName) must beSome(user.firstName)
          userOpt.map(_.lastName) must beSome(user.lastName)
          userOpt.flatMap(_.id) must not be_=== None
      }
    }
  }
}