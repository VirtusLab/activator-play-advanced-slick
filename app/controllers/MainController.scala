package controllers

import play.api.mvc.{Action, Controller}

object MainController extends Controller {

  def index = Action {
    Ok(views.html.index.render("Hello from Scala"))
  }

}