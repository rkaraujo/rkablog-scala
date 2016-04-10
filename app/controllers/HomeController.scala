package controllers

import javax.inject._

import app.models.Tables._
import play.api.db.slick._
import play.api.mvc._
import slick.driver._
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future

@Singleton
class HomeController @Inject() (dbConfigProvider: DatabaseConfigProvider) extends Controller {

  val POSTS_PER_PAGE = 5

  val dbConfig = dbConfigProvider.get[JdbcProfile]
  import dbConfig.driver.api._

  def index(page: Int) = Action.async { implicit request =>
    val baseQuery = Posts.filter(!_.publishedAt.isEmpty)

    val countPosts = baseQuery.countDistinct.result
    val queryPosts = baseQuery.sortBy(_.publishedAt.desc).drop(page * POSTS_PER_PAGE).take(POSTS_PER_PAGE).result

    val zipQuery = for {
      (count, posts) <- countPosts zip queryPosts
    } yield (count, posts)

    val countAndPostsResult: Future[(Int, Seq[_root_.app.models.Tables.PostsRow])] = dbConfig.db.run(zipQuery)
    countAndPostsResult.map({ countAndPosts =>
      val (count, posts) = countAndPosts

      val newerPage = if (page > 0) Some(page - 1) else None
      val olderPage = if (count > (page + 1) * POSTS_PER_PAGE) Some(page + 1) else None

      Ok(views.html.index(posts, newerPage, olderPage))
    })
  }

}
