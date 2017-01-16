package repositories

import model._
import org.joda.time.DateTime
import org.virtuslab.unicorn.UnicornPlay

import scala.concurrent.ExecutionContext.Implicits.global

class CommentsRepositoryTest extends BasePlayTest with CommentBaseRepositoryComponents with UserBaseRepositoryComponent{

  "Comments Repository" should "save and query for comments and users" in runWithRollback {
    //given

    val createSchema = for {
      _ <- userBaseRepository.create
      _ <- commentBaseRepository.create
    } yield ()

    val userRow = UserRow(None, "test@example.com", "Krzysztof", "Nowak")

    def buildComments(authorId: UserId) = Seq(CommentRow(None, "It's nice", authorId, DateTime.now),
      CommentRow(None, "It's very nice", authorId, DateTime.now),
      CommentRow(None, "Those comments are not spam", authorId, DateTime.now))

    /* your code here */

    ???
  }
}