package repositories

import org.virtuslab.unicorn.UnicornPlay._
import org.virtuslab.unicorn.UnicornPlay.driver.simple._
import scala.slick.lifted.TableQuery
import model.{CommentId, Comment, Comments}

/**
 * Created by Łukasz Dubiel on 20.06.14.
 */
class CommentRepository(query: TableQuery[Comments]) extends BaseIdRepository[CommentId, Comment, Comments](query) {


}
