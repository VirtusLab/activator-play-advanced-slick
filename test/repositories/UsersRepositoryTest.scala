package repositories

import model.{UserRow, Users}
import org.virtuslab.unicorn.LongUnicornPlay.driver.api._

import scala.concurrent.ExecutionContext.Implicits.global

class UsersRepositoryTest extends BasePlayTest {

  "Users Repository" should "save and query users" in runWithRollback {
    val usersRepository = new UserRepository()

    val user = UserRow(None, "test@email.com", "Krzysztof", "Nowak")
    val action = for {
      _ <- usersRepository.create
      userId <- usersRepository.save(user)
      userOpt <- usersRepository.findById(userId)
    } yield userOpt

    action.map { userOpt =>
      userOpt.map(_.email) shouldEqual Some(user.email)
      userOpt.map(_.firstName) shouldEqual Some(user.firstName)
      userOpt.map(_.lastName) shouldEqual Some(user.lastName)
      userOpt.flatMap(_.id) should not be (None)
    }
  }
}