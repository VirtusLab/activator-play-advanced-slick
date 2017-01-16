package controllers

import com.google.inject.Inject
import org.virtuslab.unicorn.LongUnicornPlayJDBC
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, Controller}
import repositories.{CommentRepository, UserRepository}

import scala.concurrent.ExecutionContext.Implicits.global

class MainController @Inject()(unicorn: LongUnicornPlayJDBC,
                               userRepository: UserRepository,
                               commentRepository: CommentRepository)()
  extends Controller {

  def index: Action[AnyContent] = Action {
    Ok(views.html.index.render("Hello from Scala"))
  }

  def findComments(email: String): Action[AnyContent] = Action.async(
    unicorn.db.run(
      commentRepository.findAllByUserEmail(email)
    ).map(comments => Ok(Json.toJson(comments)))
  )
}