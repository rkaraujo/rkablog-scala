package controllers

import javax.inject.{Inject, Singleton}

import app.models.Tables._
import play.api.db.slick.DatabaseConfigProvider
import play.api.mvc._
import slick.driver._
import play.api.libs.concurrent.Execution.Implicits.defaultContext

/**
  * Created by Renato on 10/04/2016.
  */
@Singleton
class SitemapController @Inject() (dbConfigProvider: DatabaseConfigProvider) extends Controller {

  val dbConfig = dbConfigProvider.get[JdbcProfile]
  import dbConfig.driver.api._

  def sitemap = Action.async { implicit request =>
    def urls: List[String] = List(
      controllers.routes.HomeController.index().url,
      controllers.routes.ResumeController.resume().url
    )

    val query = Posts.filter(!_.publishedAt.isEmpty).sortBy(_.publishedAt)
    val queryResult = dbConfig.db.run(query.result)
    queryResult.map({ posts =>
      val postUrls = posts
        .map("/p/" + _.slugTitle + ".html")
        .toList
      Ok(views.xml.sitemap(urls ::: postUrls))
    })
  }

}
