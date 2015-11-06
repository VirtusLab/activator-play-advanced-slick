package repositories

import model._
import org.joda.time.DateTime

import scala.concurrent.ExecutionContext.Implicits.global

class CommentsRepositoryTest extends BasePlayTest {

  "Comments Service" should "save and query for comments and users" in runWithRollback {
    //given
    val userRepo = new UserRepository(Users.query)
    val commentRepo = new CommentRepository()

    val createSchema = for {
      _ <- userRepo.create
      _ <- commentRepo.create
    } yield ()

    val userRow = UserRow(None, "test@example.com", "Krzysztof", "Nowak")

    def buildComments(authorId: UserId) = Seq(CommentRow(None, "It's nice", authorId, DateTime.now),
      CommentRow(None, "It's very nice", authorId, DateTime.now),
      CommentRow(None, "Those comments are not spam", authorId, DateTime.now))

    /* your code here */

    ???
  }
}