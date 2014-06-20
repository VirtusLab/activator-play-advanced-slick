package repositories

import org.virtuslab.unicorn.UnicornPlay._
import org.virtuslab.unicorn.UnicornPlay.driver.simple._
import model.{User, Users, UserId}
import scala.slick.lifted.TableQuery

/**
 * Created by Łukasz Dubiel on 20.06.14.
 */
class UserRepository(query: TableQuery[Users]) extends BaseIdRepository[UserId, User, Users](query) {

}
