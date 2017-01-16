package repositories

import com.google.inject.{Inject, Singleton}
import model._
import org.joda.time.DateTime
import org.virtuslab.unicorn.{LongUnicornPlayJDBC, UnicornWrapper}

trait CommentBaseRepositoryComponents {
  self: UnicornWrapper[Long] with UserBaseRepositoryComponent =>

  import unicorn._
  import unicorn.driver.api._

  class Comments(tag: Tag) extends IdTable[CommentId, CommentRow](tag, "COMMENTS") {

    protected override val idColumnName = "ID"

    def text = column[String]("TEXT")

    // you can use your type-safe ID here - it will be mapped to long in database
    def authorId = column[UserId]("AUTHOR")

    def author = foreignKey("COMMENTS_AUTHOR_FK", authorId, Users.query)(_.id)

    def date = column[DateTime]("DATE")

    override def * = (id.? , text , authorId , date) <> ((CommentRow.apply _).tupled, CommentRow.unapply)
  }

  object Comments {
    val query = TableQuery[Comments]
  }

  trait CommentQueries {

    /* your code here */

  }

  class CommentBaseRepository extends BaseIdRepository[CommentId, CommentRow, Comments](Comments.query) {
    def findByAuthorId(authorId: UserId): DBIO[Seq[CommentRow]] = {
      query.filter(_.authorId === authorId).result
    }
  }

  val commentBaseRepository = new CommentBaseRepository


}

@Singleton()
class CommentRepository @Inject() (val unicorn: LongUnicornPlayJDBC)
  extends UserBaseRepositoryComponent
    with CommentBaseRepositoryComponents
    with UnicornWrapper[Long] {

  import unicorn.driver.api._

  import scala.concurrent.ExecutionContext.Implicits.global

  def findAllByUserEmail(email: String): DBIO[Seq[CommentRow]] = {
    userBaseRepository.findUserByEmail(email)
      .flatMap{userOpt =>
         userOpt
           .flatMap(_.id)
           .map(id => commentBaseRepository.findByAuthorId(id))
           .getOrElse(DBIO.successful(Seq.empty))
      }
  }
}

