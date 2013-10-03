package service

import java.sql.SQLException
import play.api.db.slick.Config.driver.simple._
import slick.session.Session
import scala.slick.lifted.{ NumericTypeMapper, BaseTypeMapper }
import db.{Id, IdTable, BaseId}

/**
 * Base class for all queries.
 *
 * @tparam A type of element that is queried
 * @author Jerzy Müller
 */
trait BaseQueries[A] {

  /** @return table to operate on */
  def table: Table[A]

  /** query that returns all */
  protected lazy val allQuery = Query(table)
}

/**
 * Base class for all queries with an [[db.BaseId]].
 *
 * @tparam I type of id
 * @tparam A type of element that is queried
 * @author Jerzy Müller
 */
trait BaseIdQueries[I <: BaseId, A <: Id[I]] {

  /** @return table to operate on; it must be an IdTable */
  def table: Table[A] with IdTable[I, A]

  /** query that returns all */
  protected lazy val allQuery = Query(table)

  /** @return type mapper for I, required for querying */
  implicit def mapping: BaseTypeMapper[I] with NumericTypeMapper = table.mapping

  /** Query element by id, parametrized version. */
  protected lazy val byIdQuery = for {
    id <- Parameters[I]
    o <- table if o.id === id
  } yield o

  /** Query all ids. */
  protected lazy val allIdsQuery = allQuery.map(_.id)

  /** Query element by id, method version. */
  protected def byIdFunc(id: I) = allQuery.filter(_.id === id)

  /** Query by multiple ids. */
  protected def byIdsQuery(ids: Seq[I]) = allQuery.filter(_.id inSet ids)

}

/**
 * Base trait for services.
 *
 * @tparam A type of entity
 * @author Jerzy Müller
 */
trait BaseService[A] {
  self: BaseQueries[A] =>

  /**
   * @param session implicit session param for query
   * @return all elements of type A
   */
  def findAll()(implicit session: Session): Seq[A] = allQuery.list()

  /**
   * Deletes all elements in table.
   * @param session implicit session param for query
   * @return number of deleted elements
   */
  def deleteAll()(implicit session: Session): Int = allQuery.delete

  /**
   * Saves one element. Warning - if element already exist, it's not updated.
   *
   * @param elem element to save
   * @param session implicit database session
   * @return elem itself
   */
  def save(elem: A)(implicit session: Session): A = {
    if (!exists(elem)) {
      table.insert(elem)
    }
    elem
  }

  /**
   * @param elem element to check for
   * @param session implicit database session
   * @return true if element exists in database
   */
  protected def exists(elem: A)(implicit session: Session): Boolean
}

/**
 * Base trait for services where we use [[db.BaseId]]s.
 *
 * @tparam I type of id
 * @tparam A type of entity
 * @author Jerzy Müller
 */
trait BaseIdService[I <: BaseId, A <: Id[I]] {
  self: BaseIdQueries[I, A] =>

  /**
   * @param session implicit session param for query
   * @return all elements of type A
   */
  def findAll()(implicit session: Session): Seq[A] = allQuery.list()

  /**
   * Deletes all elements in table.
   * @param session implicit session param for query
   * @return number of deleted elements
   */
  def deleteAll()(implicit session: Session): Int = allQuery.delete

  /**
   * Finds one element by id.
   *
   * @param id id of element
   * @param session implicit session
   * @return Option(element)
   */
  def findById(id: I)(implicit session: Session): Option[A] = byIdQuery(id).firstOption

  /**
   * Finds elements by given ids.
   *
   * @param ids ids of element
   * @param session implicit session
   * @return Seq(element)
   */
  def findByIds(ids: Seq[I])(implicit session: Session): Seq[A] = byIdsQuery(ids).list

  /**
   * Deletes one element by id.
   *
   * @param id id of element
   * @param session implicit session
   * @return number of deleted elements (0 or 1)
   */
  def deleteById(id: I)(implicit session: Session): Int = byIdFunc(id).delete
    .ensuring(_ <= 1, "Delete by id removed more than one row")

  /**
   * @param session implicit session
   * @return Sequence of ids
   */
  def allIds()(implicit session: Session): Seq[I] = allIdsQuery.list()

  /**
   * Saves one element.
   *
   * @param elem element to save
   * @param session implicit session
   * @return Option(elementId)
   */
  def save(elem: A)(implicit session: Session): I = {
    elem.id.map {
      id =>
        val rowsUpdated = byIdFunc(id).update(elem)
        if (rowsUpdated == 1) id
        else throw new SQLException(s"Error during save in table: ${table.tableName}, " +
          s"for id: $id - $rowsUpdated rows updated, expected: 1. Entity: $elem")
    }.getOrElse(
      table.insertOne(elem)
    )
  }
}