package repositories

import model.UserRow

import scala.concurrent.ExecutionContext.Implicits.global

class UsersRepositoryTest extends BasePlayTest with UserBaseRepositoryComponent {

  "Users Repository" should "save and query users" in runWithRollback {

    val user = UserRow(None, "test@email.com", "Krzysztof", "Nowak")
    val action = for {
      _ <- userBaseRepository.create
      userId <- userBaseRepository.save(user)
      userOpt <- userBaseRepository.findById(userId)
    } yield userOpt

    action.map { userOpt =>
      userOpt.map(_.email) shouldEqual Some(user.email)
      userOpt.map(_.firstName) shouldEqual Some(user.firstName)
      userOpt.map(_.lastName) shouldEqual Some(user.lastName)
      userOpt.flatMap(_.id) should not be (None)
    }
  }
}