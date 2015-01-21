package filters

import controllers.Default
import play.api.Logger
import play.api.mvc.{Result, RequestHeader, Filter}

case class CORSFilter() extends Filter {

  import scala.concurrent._
  import ExecutionContext.Implicits.global

  lazy val allowedDomain = play.api.Play.current.configuration.getString("cors.allowed.domain")

  def isPreFlight(r: RequestHeader) = (
    r.method.toLowerCase.equals("options")
      &&
      r.headers.get("Access-Control-Request-Method").nonEmpty
    )

  def apply(f: (RequestHeader) => Future[Result])(request: RequestHeader): Future[Result] = {
    Logger.info("[cors] filtering request to add cors")
    if (isPreFlight(request)) {
      Logger.info("[cors] request is preflight")
      Logger.info(s"[cors] default allowed domain is $allowedDomain")
      Future.successful(Default.Ok.withHeaders(
        "Access-Control-Allow-Origin" -> allowedDomain.orElse(request.headers.get("Origin")).getOrElse(""),
        "Access-Control-Allow-Methods" -> request.headers.get("Access-Control-Request-Method").getOrElse("*"),
        "Access-Control-Allow-Headers" -> request.headers.get("Access-Control-Request-Headers").getOrElse(""),
        "Access-Control-Allow-Credentials" -> "true"
      ))
    } else {
      Logger.info("[cors] request is normal")
      Logger.info(s"[cors] default allowed domain is $allowedDomain")
      f(request).map {
        _.withHeaders(
          "Access-Control-Allow-Origin" -> allowedDomain.orElse(request.headers.get("Origin")).getOrElse(""),
          "Access-Control-Allow-Methods" -> request.headers.get("Access-Control-Request-Method").getOrElse("*"),
          "Access-Control-Allow-Headers" -> request.headers.get("Access-Control-Request-Headers").getOrElse(""),
          "Access-Control-Allow-Credentials" -> "true"
        )
      }
    }
  }

}
