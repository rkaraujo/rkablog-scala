package controllers.admin

import javax.inject.{Inject, Singleton}

import play.api.mvc.{Action, Controller}

@Singleton
class AdminPostController @Inject() extends Controller {

  def show() = Action { implicit request =>
    Ok(views.html.admin.show())
  }

  def save() = Action {
    Redirect("/admin/show.html").flashing("message" -> "Sucesso")
  }

}
