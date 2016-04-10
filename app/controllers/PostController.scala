package controllers

import javax.inject.{Inject, Singleton}

import app.models.Tables._
import play.api.db.slick._
import play.api.mvc._
import slick.driver._
import play.api.libs.concurrent.Execution.Implicits.defaultContext

@Singleton
class PostController @Inject() (dbConfigProvider: DatabaseConfigProvider) extends Controller {

  val dbConfig = dbConfigProvider.get[JdbcProfile]
  import dbConfig.driver.api._

  def show(slugTitle: String) = Action.async {
    val postResult = dbConfig.db.run(Posts.filter(_.slugTitle === slugTitle).result)
    postResult.map(posts => Ok(views.html.post(posts.head)))
  }

}