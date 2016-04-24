import javax.inject.Inject

import play.api.{Environment, Mode}
import play.api.http._
import play.api.mvc._
import play.api.routing.Router
import play.api.cache._

/*
  Simple authorization based on cookies
 */
class RequestHandler @Inject()(router: Router,
                               errorHandler: HttpErrorHandler, configuration: HttpConfiguration, filters: HttpFilters
  ) (cache: CacheApi) (env: Environment) extends DefaultHttpRequestHandler(router, errorHandler, configuration, filters) {

  def validSessionCookie(authCookieValue: String): Boolean = {
    val splitted = authCookieValue.split(":")
    if (splitted.length != 2) {
      return false
    }

    val username = splitted(0)
    val cookieSession = splitted(1)

    if (cookieSession.isEmpty) {
      return false
    }

    val cacheSessionOption = cache.get[String](username + "-session")
    if (cacheSessionOption.isEmpty) {
      return false
    }

    val cacheSession = cacheSessionOption.get
    return cookieSession == cacheSession
  }

  override def routeRequest(request: RequestHeader): Option[Handler] = {
    if (env.mode == Mode.Dev) {
      return super.routeRequest(request)
    }

    if (!request.uri.startsWith("/admin/") || request.uri == "/admin/login.html") {
      return super.routeRequest(request)
    }

    val authCookie = request.cookies.get("AUTH")
    if (authCookie.isEmpty || !validSessionCookie(authCookie.get.value)) {
      return Some(Action(Results.TemporaryRedirect("/admin/login.html")))
    }

    return super.routeRequest(request)
  }
}