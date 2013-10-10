package service

import org.specs2.mutable.Specification
import play.api.test.WithApplication
import play.api.db.slick._
import scala.slick.session.Session
import model.{Comment, User}
import org.joda.time.DateTime

class CommentsServiceTest extends Specification {

  "Comments Service" should {

    "save and query for comments and users" in new WithApplication {
      DB.withSession {
        implicit session: Session =>
         // put test here
      }
    }
  }
}