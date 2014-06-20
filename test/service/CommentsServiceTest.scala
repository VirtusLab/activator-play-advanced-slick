package service

import org.specs2.mutable.Specification
import play.api.test.WithApplication
import org.virtuslab.unicorn.UnicornPlay.driver.simple._
import model.{Users, Comments}
import play.api.db.slick._
import org.virtuslab.unicorn.UnicornPlay._
import repositories.CommentRepository

class CommentsServiceTest extends Specification {

  "Comments Service" should {

    "save and query for comments and users" in new WithApplication {
      DB.withSession { implicit session: Session =>
        val userQuery = TableQuery[Users]
        val tableQuery = TableQuery[Comments] { tag: Tag =>
          new Comments(userQuery, tag)
        }
        val repo = new CommentRepository(tableQuery)

      }
    }
  }
}