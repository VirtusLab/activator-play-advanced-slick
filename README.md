Advanced play-slick Typesafe Activator template
===============================================

Slick (the Scala Language-Integrated Connection Kit) is a framework for type-safe, composable data access in Scala. This template combines Play! Framework with Slick and adds tools to use type-safe IDs for your classes so you can no longer join on bad id field or mess up order of fields in mappings. It also provides a way to create service layer with methods (like querying all, querying by id, saving or deleting) for all classes with such IDs in just 4 lines of code.

Idea for type-safe ids was derived from Slick creator's [presentation on ScalaDays 2013](http://www.parleys.com/play/51c2e20de4b0d38b54f46243/chapter63/about).

Examples
========

Defining entities
-----------------

```scala
package model

import org.virtuslab.unicorn.{BaseId, WithId}
import org.virtuslab.unicorn.LongUnicornPlayIdentifiers._

/** Id class for type-safe joins and queries. */
case class UserId(id: Long) extends AnyVal with BaseId[Long]

/** Companion object for id class, extends IdCompanion
  * and brings all required implicits to scope when needed.
  */
object UserId extends IdCompanion[UserId]

/** User entity.
  *
  * @param id user id
  * @param email user email address
  * @param lastName lastName
  * @param firstName firstName
  */
case class UserRow(id: Option[UserId],
                   email: String,
                   firstName: String,
                   lastName: String) extends WithId[Long, UserId]
```

Defining repositories
-----------------

```scala
package repositories

import model.{UserId, UserRow}
import org.virtuslab.unicorn.{UnicornPlay, UnicornWrapper}

/**
  * A place for all objects directly connected with database.
  *
  * Put your user queries here.
  * Having them in separate in this trait keeps `UserRepository` neat and tidy.
  */
trait UserBaseRepositoryComponent {
  self: UnicornWrapper[Long] =>
  import unicorn._
  import unicorn.driver.api._


  /** Table definition for users. */
  class Users(tag: Tag) extends IdTable[UserId, UserRow](tag, "USERS") {

    /** By definition id column is inserted as lowercase 'id',
      * if you want to change it, here is your setting.
      */
    protected override val idColumnName = "ID"

    def email = column[String]("EMAIL")

    def firstName = column[String]("FIRST_NAME")

    def lastName = column[String]("LAST_NAME")

    override def * = (id.?, email, firstName, lastName) <>(UserRow.tupled, UserRow.unapply)

  }

  object Users {
    val query = TableQuery[Users]
  }

  trait UserQueries{

    protected lazy val userByEmailQuery = for {
      email <- Parameters[String]
      user <- Users.query if user.email === email
    } yield user

  }

  class UserBaseRepository
    extends BaseIdRepository[UserId, UserRow, Users](TableQuery[Users])
      with UserQueries {

    import scala.concurrent.ExecutionContext.Implicits.global

    def findUserByEmail(email: String): DBIO[Option[UserRow]] = {
      userByEmailQuery(email).result.map { foundUsers =>
        if(foundUsers.size > 1) throw new IllegalStateException("...")
        foundUsers.headOption
      }
    }

  }

  val userBaseRepository = new UserBaseRepository

}

class UserRepository(protected val unicorn: UnicornPlay[Long])
  extends UserBaseRepositoryComponent with UnicornWrapper[Long] {

  import unicorn.driver.api._

  def findUserByEmail(email: String): DBIO[Option[UserRow]] = {
    userBaseRepository.findUserByEmail(email)
  }
}

```

Usage
-----

```scala
package repositories

import model.UserRow

import scala.concurrent.ExecutionContext.Implicits.global

class UsersRepositoryTest extends BasePlayTest with UserBaseRepositoryComponent {

  "Users Repository" should "save and query users" in runWithRollback {

    val user = UserRow(None, "test@email.com", "Krzysztof", "Nowak")
    val action = for {
      _ <- userBaseRepository.create
      userId <- userBaseRepository.save(user)
      userOpt <- userBaseRepository.findById(userId)
    } yield userOpt

    action.map { userOpt =>
      userOpt.map(_.email) shouldEqual Some(user.email)
      userOpt.map(_.firstName) shouldEqual Some(user.firstName)
      userOpt.map(_.lastName) shouldEqual Some(user.lastName)
      userOpt.flatMap(_.id) should not be (None)
    }
  }
}
```

Contributors
------------
The activator is based on the work by [VirtusLab](http://www.virtuslab.com) team.
Main authors are:
* [Jerzy Müller](https://github.com/Kwestor)
* [Krzysztof Romanowski](https://github.com/romanowski)
* [Paweł Batko](https://github.com/pbatko)
* [Krzysztof Borowski](https://github.com/liosedhel)
* Rafał Pokrywka

The project is a fork of super slick play activator template. 

Feel free to use it, test it and to contribute!
