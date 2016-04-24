package controllers.admin

import util.Slugfier
import java.sql.{Date, Timestamp}
import javax.inject.{Inject, Singleton}

import scala.concurrent._
import scala.concurrent.duration._
import app.models.Tables
import app.models.Tables.PostsRow
import play.api.data._
import play.api.data.Forms._
import play.api.db.slick.DatabaseConfigProvider
import play.api.mvc.{Action, Controller}
import slick.driver.JdbcProfile
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.i18n._

@Singleton
class AdminPostController @Inject() (val messagesApi: MessagesApi, dbConfigProvider: DatabaseConfigProvider) extends Controller with I18nSupport {

  val postForm = Form(
    mapping(
      "id" -> optional(number),
      "content" -> nonEmptyText,
      "title" -> nonEmptyText,
      "pageDescription" -> optional(text)
    ) (PostData.apply) (PostData.unapply)
  )

  val dbConfig = dbConfigProvider.get[JdbcProfile]
  import dbConfig.driver.api._

  def list() = Action.async { implicit  request =>
    val postsResult = dbConfig.db.run(Tables.Posts.sortBy(p => (p.publishedAt.desc.nullsFirst, p.createdAt.desc)).result)
    postsResult.map { posts =>
      Ok(views.html.admin.list(posts))
    }
  }

  def show(id: String) = Action.async { implicit request =>
    val maybeId: Option[String] = Option(id)
    if (maybeId.isDefined) {
      val postResult = dbConfig.db.run(Tables.Posts.filter(_.id === maybeId.get.toInt).result)
      postResult.map(posts => {
        val post = posts.head
        val postData = PostData(post.id, post.content, post.title, post.pageDescription)
        val filledPostForm = postForm.fill(postData)
        Ok(views.html.admin.show(filledPostForm))
      })
    } else {
      Future.successful(Ok(views.html.admin.show(postForm)))
    }
  }

  def save() = Action { implicit request =>
    postForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.admin.show(formWithErrors))
      },
      post => {
        val postResult = dbConfig.db.run(Tables.Posts.filter(_.id === post.id).result)
        val dbPosts = Await.result(postResult, 10.seconds)
        var postRow: PostsRow = null
        if (dbPosts.isEmpty) {
          val postRow = PostsRow(None, post.content, new Timestamp(System.currentTimeMillis()), None, Slugfier.slugfy(post.title), post.title, new Timestamp(System.currentTimeMillis()), post.pageDescription)
          dbConfig.db.run(Tables.Posts += postRow).map(_ => ())
        } else {
          val dbPost = dbPosts.head
          val postRow = PostsRow(dbPost.id, post.content, dbPost.createdAt, dbPost.publishedAt, Slugfier.slugfy(post.title), post.title, new Timestamp(System.currentTimeMillis()), post.pageDescription)
          dbConfig.db.run(Tables.Posts.filter(_.id === dbPost.id).update(postRow)).map(_ -> ())
        }
        Redirect("/admin/post.html?id=1").flashing("message" -> "Post saved")
      }
    )
  }

  def publish(id: Int) = Action { implicit request =>
    dbConfig.db.run(Tables.Posts
      .filter(_.id === id)
      .map(p => (p.publishedAt))
      .update(Some(new Date(System.currentTimeMillis()))))
    Redirect("/admin/list.html").flashing("message" -> "Post published")
  }

}
