package model

import org.joda.time.DateTime
import db.{WithId, IdTable, IdCompanion, BaseId}
import scala.slick.session.Session

case class CommentId(id: Long) extends AnyVal with BaseId

object CommentId extends IdCompanion[CommentId]


case class Comment(id: Option[CommentId],
                   text: String,
                   authorId: UserId,
                   date: DateTime) extends WithId[CommentId]


object Comments extends IdTable[CommentId, Comment]("comments") {

  def text = column[String]("text")

  // you can use your type-safe ID here - it will be mapped to long in database
  def authorId = column[UserId]("author")

  def author = foreignKey("comments_author_fk", authorId, Users)(_.id)

  def date = column[DateTime]("date")

  def base = text ~ authorId ~ date

  override def * = id.? ~: base <> (Comment.apply _, Comment.unapply _)

  override def insertOne(elem: Comment)(implicit session: Session): CommentId =
    saveBase(base, Comment.unapply _)(elem)

}