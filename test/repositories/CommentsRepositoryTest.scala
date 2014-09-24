package repositories

import model._
import org.joda.time.DateTime
import org.virtuslab.unicorn.LongUnicornPlay.driver.simple._
import play.api.db.slick.DB
import play.api.test.{PlaySpecification, WithApplication}

class CommentsRepositoryTest extends PlaySpecification {

  "Comments Service" should {

    "save and query for comments and users" in new WithApplication {
      DB.withSession { implicit session: Session =>
        val userQuery = TableQuery[Users]
        val tableQuery = TableQuery[Comments] { tag: Tag =>
          new Comments(userQuery, tag)
        }
        val userRepo = new UserRepository(userQuery)
        val commentRepo = new CommentRepository(tableQuery)
        
        /* your code here */
      }
    }
  }
}