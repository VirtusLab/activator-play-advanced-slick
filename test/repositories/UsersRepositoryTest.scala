package repositories

import model.{UserRow, Users}
import org.virtuslab.unicorn.LongUnicornPlay.driver.simple._
import play.api.db.slick.DB
import play.api.test.{PlaySpecification, WithApplication}

class UsersRepositoryTest extends PlaySpecification {

  "Users Service" should {

    "save and query users" in new WithApplication {
      DB.withSession { implicit session: Session =>
        val usersQuery: TableQuery[Users] = TableQuery[Users]
        val usersRepository = new UserRepository(usersQuery)

        val user = UserRow(None, "test@email.com", "Krzysztof", "Nowak")
        val userId = usersRepository save user
        val userOpt = usersRepository findById userId

        userOpt.map(_.email) shouldEqual Some(user.email)
        userOpt.map(_.firstName) shouldEqual Some(user.firstName)
        userOpt.map(_.lastName) shouldEqual Some(user.lastName)
        userOpt.flatMap(_.id) should not be (None)
      }
    }
  }

}