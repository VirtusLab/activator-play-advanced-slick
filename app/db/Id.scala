package db

import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.{MappedTypeMapper, BaseTypeMapper, NumericTypeMapper}
import play.api.mvc.{ QueryStringBindable, PathBindable }
import play.api.data.format.{ Formats, Formatter }
import play.api.i18n.Messages
import scala.slick.session.Session

/**
 * Base trait for all ids in system.
 * It is existential trait so it can have only defs.
 *
 * @author Krzysztof Romanowski, Jerzy Müller
 */
trait BaseId extends Any {
  def id: Long
}

/**
 * Base class for companion objects for id classes.
 * Adding this will allow you not to import mapping from your table class every time you need it.
 *
 * @param table table for this id
 * @tparam I type of Id
 * @tparam A type of table
 * @author Krzysztof Romanowski, Jerzy Müller
 */
abstract class IdMapping[I <: BaseId, A <: Id[I]](table: IdTable[I, A]) {

  /** Ordering for ids - it is normal simple ordering on inner integers ascending. */
  implicit lazy val ordering: Ordering[I] = Ordering.by[I, Long](_.id)

  /** Type mapper for tables. */
  implicit lazy val mapping: BaseTypeMapper[I] with NumericTypeMapper = table.mapping

  /**
   * Type mapper for route files.
   * @param longBinder path bindable for Long type.
   * @return path bindable for I
   */
  implicit def pathBindable(implicit longBinder: PathBindable[Long]): PathBindable[I] =
    longBinder.transform(table.idCreateFunc, _.id)

  /** Implicit for mapping id to routes params for play */
  implicit val toPathBindable: QueryStringBindable[I] =
    QueryStringBindable.bindableLong.transform(table.idCreateFunc, _.id)

  /**
   * table name should be placed in messages under entity.tableName
   * @return Form formatter for I.
   */
  implicit lazy val idMappingFormatter: Formatter[I] = new Formatter[I] {

    override val format = Some(("format.numeric", Nil))

    override def bind(key: String, data: Map[String, String]) =
      Formats.longFormat.bind(key, data).right.map(table.idCreateFunc).left.map(
        errors =>
        {
          if (data.get(key).forall(_.isEmpty))
            errors.map(_.copy(message = "id.empty", args = Seq(Messages(s"entity.${table.tableName}"))))
          else
            errors.map(_.copy(message = "id.invalid", args = Seq(Messages(s"entity.${table.tableName}"))))
        }
      )

    override def unbind(key: String, value: I): Map[String, String] =
      Map(key -> value.id.toString)
  }
}

/**
 * Base class for all entities that contains an id.
 *
 * @author Krzysztof Romanowski
 */
trait Id[I] {
  /**
   * @return id of entity (optional, entities does not have ids before save)
   */
  def id: Option[I]
}

/**
 * Base class for all tables that contains an id.
 *
 * @param schemaName name of schema (optional)
 * @param tableName name of the table
 * @param idCreateFunc function from int to I (id type of this table)
 * @tparam A type of table
 * @author Krzysztof Romanowski, Jerzy Müller
 */
abstract class IdTable[I <: BaseId, A <: Id[I]](schemaName: Option[String], tableName: String, val idCreateFunc: Long => I)
  extends BaseTable[A](schemaName, tableName)
  with SavingMethods[I, A, IdTable[I, A]] {

  /**
   * Auxiliary constructor without schema name.
   * @param tableName name of table
   */
  def this(tableName: String, idCreateFunc: Long => I) = this(None, tableName, idCreateFunc)

  /**
   * @return id column representation of this table
   */
  final def id = column[I]("id", O.PrimaryKey, O.AutoInc)

  /**
   * @return Mapping for id.
   */
  implicit final def mapping: BaseTypeMapper[I] with NumericTypeMapper =
    new MappedTypeMapper[I, Long] with BaseTypeMapper[I] with NumericTypeMapper {
      def map(t: I): Long = t.id

      def comap(u: Long): I = idCreateFunc(u)
    }

  /**
   * Method for inserting one element, to be implemented by subtypes.
   *
   * @param elem element to insert
   * @param session implicit session
   * @return
   */
  def insertOne(elem: A)(implicit session: Session): I
}

/**
 * Base trait for all tables. If you want to add some helpers methods for tables, here is the place.
 *
 * @param schemaName name of schema (optional)
 * @param tableName name of the table
 * @tparam A type of table
 * @author Krzysztof Romanowski, Jerzy Müller
 */
abstract class BaseTable[A](schemaName: Option[String], tableName: String)
  extends Table[A](schemaName, tableName)
  with CustomTypeMappers {

  /**
   * Auxiliary constructor without schema name.
   * @param tableName name of table
   */
  def this(tableName: String) = this(None, tableName)
}

