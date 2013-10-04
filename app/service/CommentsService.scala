package service

import model.{ UserId, Comments, Comment, CommentId }
import scala.slick.lifted.Parameters
import scala.slick.session.Session
import play.api.db.slick.Config.driver.simple._

trait CommentsQueries extends BaseIdQueries[CommentId, Comment] {
  override def table = Comments

  // put your queries here
}

trait CommentsService extends BaseIdService[CommentId, Comment] with CommentsQueries {
  // put your service methods here
}