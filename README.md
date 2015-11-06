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

import org.virtuslab.unicorn.LongUnicornPlay._
import org.virtuslab.unicorn.LongUnicornPlay.driver.api._

/** Id class for type-safe joins and queries. */
case class UserId(id: Long) extends AnyVal with BaseId

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
                   lastName: String) extends WithId[UserId]

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
```

Defining repositories
-----------------

```scala
package repositories

import org.virtuslab.unicorn.LongUnicornPlay._
import org.virtuslab.unicorn.LongUnicornPlay.driver.api._
import model.{UserRow, Users, UserId}

/**
  * A place for user queries.
  *
  * Put your user queries here.
  * Having them in separate in this trait keeps `UserRepository` neat and tidy.
  */
private[repositories] trait UserQueries {

  protected lazy val userByEmailQuery = for {
    email <- Parameters[String]
    user <- Users.query if user.email === email
  } yield user
}

/**
  * Repository for users.
  *
  * It brings all base service methods with it from [[org.virtuslab.unicorn.repositories.IdRepositories.BaseIdRepository]].
  * You can add your methods as well.
  */
class UserRepository
  extends BaseIdRepository[UserId, UserRow, Users](Users.query)
  with UserQueries {

  import scala.concurrent.ExecutionContext.Implicits.global

  def findUserByEmail(email: String): DBIO[Option[UserRow]] = {
    userByEmailQuery(email).result.map { foundUsers =>
      if(foundUsers.size > 1) throw new IllegalStateException("...")
      foundUsers.headOption
    }
  }

}

```

Usage
-----

```scala
package repositories

import model.{UserRow, Users}
import org.virtuslab.unicorn.LongUnicornPlay.driver.api._

import scala.concurrent.ExecutionContext.Implicits.global

class UsersRepositoryTest extends BasePlayTest {

  "Users Repository" should "save and query users" in runWithRollback {
    val usersRepository = new UserRepository(Users.query)

    val user = UserRow(None, "test@email.com", "Krzysztof", "Nowak")
    val action = for {
      _ <- usersRepository.create
      userId <- usersRepository.save(user)
      userOpt <- usersRepository.findById(userId)
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
* Rafał Pokrywka

The project is a fork of super slick play activator template. 

Feel free to use it, test it and to contribute!
