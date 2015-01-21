package controllers

import play.api.libs.json.Json
import play.api.mvc._

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def json = Action {
    Ok(Json.parse( """ {"id": 123, "name": "Gabi"} """))
  }

  def options = Action {
    Ok("Options Resource")
  }

}