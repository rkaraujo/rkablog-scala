package controllers.admin

import java.util.UUID
import javax.inject.{Inject, Singleton}

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.i18n.I18nSupport
import play.api.i18n.MessagesApi
import play.api.cache._
import scala.concurrent.duration._
import util.CheckUsersFile

/**
  * Created by Renato on 09/04/2016.
  */
@Singleton
class AdminAuthenticationController @Inject() (val messagesApi: MessagesApi, cache: CacheApi) extends Controller with I18nSupport {

  val loginForm = Form(
    mapping(
      "username" -> nonEmptyText,
      "password" -> nonEmptyText
    ) (LoginData.apply) (LoginData.unapply)
  )

  def showLogin() = Action {
    Ok(views.html.admin.login(loginForm))
  }

  def login() = Action(parse.form(loginForm, onErrors = (formWithErrors: Form[LoginData]) => BadRequest(views.html.admin.login(formWithErrors)))) { implicit request =>
    val loginData = request.body

    var message = "nao logado"

    val isUsernamePasswordValid: Boolean = CheckUsersFile.checkUsernamePassword(loginData.username, loginData.password)
    if (isUsernamePasswordValid) {

      val userSession = UUID.randomUUID().toString()
      cache.set(loginData.username + "-session", userSession, 4.hours)

      val authCookieValue = loginData.username + ":" + userSession

      Redirect(routes.AdminPostController.show()).flashing("message" -> message).withCookies(Cookie("AUTH", authCookieValue))
    } else {
      Redirect(routes.AdminAuthenticationController.showLogin())
    }
  }

}
