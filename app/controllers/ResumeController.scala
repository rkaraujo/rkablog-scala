package controllers

import javax.inject.{Inject, Singleton}

import play.api.mvc.{Action, Controller}

/**
  * Created by Renato on 27/03/2016.
  */
@Singleton
class ResumeController @Inject() extends Controller {

  def resume = Action {
    Ok(views.html.resume())
  }

}
