package repositories

import org.virtuslab.unicorn.LongUnicornPlay._
import org.virtuslab.unicorn.LongUnicornPlay.driver.api._
import model._

private[repositories] trait CommentQueries {

  /* your code here */

}

class CommentRepository
  extends BaseIdRepository[CommentId, CommentRow, Comments](Comments.query)
  with CommentQueries {

  /* your code here */


}
