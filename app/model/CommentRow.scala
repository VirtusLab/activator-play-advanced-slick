package model

import org.virtuslab.unicorn.LongUnicornPlayIdentifiers._
import org.joda.time.DateTime
import org.virtuslab.unicorn.{BaseId, WithId}
import play.api.libs.json.Json

case class CommentId(id: Long) extends AnyVal with BaseId[Long]

object CommentId extends IdCompanion[CommentId]

case class CommentRow(id: Option[CommentId],
                      text: String,
                      authorId: UserId,
                      date: DateTime) extends WithId[Long, CommentId]

object CommentRow{
  implicit val format = Json.format[CommentRow]
}

