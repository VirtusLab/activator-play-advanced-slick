package model

import org.joda.time.DateTime
import scala.slick.session.Session
import org.virtuslab.unicorn.ids._

case class CommentId(id: Long) extends AnyVal with BaseId

object CommentId extends IdCompanion[CommentId]


case class Comment(id: Option[CommentId],
                   text: String,
                   authorId: UserId,
                   date: DateTime) extends WithId[CommentId]


object Comments extends IdTable[CommentId, Comment]("COMMENTS") {

  def text = column[String]("TEXT")

  // you can use your type-safe ID here - it will be mapped to long in database
  def authorId = column[UserId]("AUTHOR")

  def author = foreignKey("COMMENTS_AUTHOR_FK", authorId, Users)(_.id)

  def date = column[DateTime]("DATE")

  def base = text ~ authorId ~ date

  override def * = id.? ~: base <> (Comment.apply _, Comment.unapply _)

  override def insertOne(elem: Comment)(implicit session: Session): CommentId =
    saveBase(base, Comment.unapply _)(elem)

}