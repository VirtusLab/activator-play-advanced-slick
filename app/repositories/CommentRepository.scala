package repositories

import org.virtuslab.unicorn.LongUnicornPlay._
import org.virtuslab.unicorn.LongUnicornPlay.driver.simple._
import scala.slick.lifted.TableQuery
import model.{CommentId, CommentRow, Comments}


class CommentRepository(query: TableQuery[Comments])
  extends BaseIdRepository[CommentId, CommentRow, Comments](query) {
      
    /* your code here */
}
