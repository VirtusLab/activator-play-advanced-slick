package model

import org.virtuslab.unicorn.UnicornPlay._
import org.virtuslab.unicorn.UnicornPlay.driver.simple._
import org.joda.time.DateTime

case class CommentId(id: Long) extends AnyVal with BaseId

object CommentId extends IdCompanion[CommentId]


case class Comment(id: Option[CommentId],
                   text: String,
                   authorId: UserId,
                   date: DateTime) extends WithId[CommentId]


class Comments(users: TableQuery[Users], tag: Tag) extends IdTable[CommentId, Comment](tag, "COMMENTS") {

  def text = column[String]("TEXT")

  // you can use your type-safe ID here - it will be mapped to long in database
  def authorId = column[UserId]("AUTHOR")

  def author = foreignKey("COMMENTS_AUTHOR_FK", authorId, users)(_.id)

  def date = column[DateTime]("DATE")

  override def * = (id.? , text , authorId , date) <> (Comment.tupled, Comment.unapply)
}