package service

import model.{Comments, Users}
import org.virtuslab.unicorn.LongUnicornPlay.driver.simple._
import play.api.db.slick.DB
import play.api.test.{PlaySpecification, WithApplication}
import repositories.CommentRepository

class CommentsServiceTest extends PlaySpecification {

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