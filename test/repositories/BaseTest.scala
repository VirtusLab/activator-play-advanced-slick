package repositories

import org.scalatest._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}
import org.virtuslab.unicorn.{HasJdbcDriver, LongUnicornPlay, Unicorn}
import org.virtuslab.unicorn.LongUnicornPlay.driver.api._
import play.api.Play
import play.api.test.FakeApplication
import slick.dbio.DBIOAction

import scala.concurrent.Await
import scala.concurrent.duration._


trait BaseTest[Underlying] extends FlatSpecLike with Matchers with BeforeAndAfterEach with ScalaFutures {

  val unicorn: Unicorn[Underlying] with HasJdbcDriver

  val dbURL = "jdbc:h2:mem:unicorn"

  val dbDriver = "org.h2.Driver"

  lazy val DB = unicorn.driver.backend.Database.forURL(dbURL, driver = dbDriver)

  implicit val defaultPatience =
    PatienceConfig(timeout = Span(5, Seconds), interval = Span(500, Millis))

  case class IntentionalRollbackException() extends Exception("Transaction intentionally aborted")

  def runWithRollback[R, S <: slick.dbio.NoStream, E <: slick.dbio.Effect](action: DBIOAction[R, S, E]): Unit = {
    try {
      val block = action andThen DBIO.failed(new IntentionalRollbackException())
      val future = DB.run(block.transactionally)
      Await.result(future, 5.seconds)
    } catch {
      case e: IntentionalRollbackException => // Success
    }
  }

}


trait BasePlayTest
  extends FlatSpecLike
  with OptionValues
  with Matchers
  with BeforeAndAfterEach
  with BaseTest[Long]
  with BeforeAndAfterAll {

  private val testDb = Map(
    "slick.dbs.default.driver" -> "slick.driver.H2Driver$",
    "slick.dbs.default.db.driver" -> "org.h2.Driver",
    "slick.dbs.default.db.url" -> "jdbc:h2:mem:play",
    "slick.dbs.default.db.user" -> "sa",
    "slick.dbs.default.db.password" -> ""
  )

  implicit val app: FakeApplication = {
    val fake = new FakeApplication(additionalConfiguration = testDb)
    Play.start(fake)
    fake
  }

  override lazy val unicorn: Unicorn[Long] with HasJdbcDriver = LongUnicornPlay

  import unicorn.driver.api._

  override protected def beforeEach(data: TestData): Unit = {
    DB.run(sqlu"""DROP ALL OBJECTS""")
    super.beforeEach()
  }

  override protected def afterAll(): Unit = {
    Play.stop(app)
    super.afterEach()
  }

}