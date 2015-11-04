package repositories

import org.virtuslab.unicorn.LongUnicornPlay._
import org.virtuslab.unicorn.LongUnicornPlay.driver.api._
import model.{UserRow, Users, UserId}
import slick.lifted.TableQuery

class UserRepository(query: TableQuery[Users])
  extends BaseIdRepository[UserId, UserRow, Users](query)
