package db

import java.sql.{ Date, Timestamp }
import org.joda.time.{ LocalDate, DateTime }
import scala.slick.lifted.MappedTypeMapper

/**
 * Custom Type mappers for Slick.
 *
 * @author Jerzy Müller, Krzysztof Romanowski
 */
trait CustomTypeMappers {

  /** Type mapper for [[org.joda.time.DateTime]] */
  implicit val dateTimeMapper = MappedTypeMapper.base[DateTime, Timestamp](
    dt => new Timestamp(dt.getMillis),
    ts => new DateTime(ts.getTime)
  )

  /** Type mapper for [[org.joda.time.LocalDate]] */
  implicit val localDateMapper = MappedTypeMapper.base[LocalDate, Date](
    dt => new Date(dt.toDate.getTime),
    d => new LocalDate(d.getTime)
  )
}

/** Object for [[db.CustomTypeMappers]] if you prefer import rather than extend. */
object CustomTypeMappers extends CustomTypeMappers