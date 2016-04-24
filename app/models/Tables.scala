package app.models
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.driver.MySQLDriver
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.driver.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Posts.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Posts
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param content Database column content SqlType(TEXT)
   *  @param createdAt Database column created_at SqlType(DATETIME)
   *  @param publishedAt Database column published_at SqlType(DATETIME), Default(None)
   *  @param slugTitle Database column slug_title SqlType(VARCHAR), Length(255,true)
   *  @param title Database column title SqlType(VARCHAR), Length(255,true)
   *  @param updatedAt Database column updated_at SqlType(DATETIME)
   *  @param pageDescription Database column page_description SqlType(VARCHAR), Length(255,true), Default(None) */
  case class PostsRow(id: Option[Int], content: String, createdAt: java.sql.Timestamp, publishedAt: Option[java.sql.Date] = None, slugTitle: String, title: String, updatedAt: java.sql.Timestamp, pageDescription: Option[String] = None)
  /** GetResult implicit for fetching PostsRow objects using plain SQL queries */
  implicit def GetResultPostsRow(implicit e0: GR[Option[Int]], e1: GR[String], e2: GR[java.sql.Timestamp], e3: GR[Option[java.sql.Timestamp]], e4: GR[Option[String]]): GR[PostsRow] = GR{
    prs => import prs._
    PostsRow.tupled((<<?[Int], <<[String], <<[java.sql.Timestamp], <<?[java.sql.Date], <<[String], <<[String], <<[java.sql.Timestamp], <<?[String]))
  }
  /** Table description of table posts. Objects of this class serve as prototypes for rows in queries. */
  class Posts(_tableTag: Tag) extends Table[PostsRow](_tableTag, "posts") {
    def * = (id, content, createdAt, publishedAt, slugTitle, title, updatedAt, pageDescription) <> (PostsRow.tupled, PostsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(content), Rep.Some(createdAt), publishedAt, Rep.Some(slugTitle), Rep.Some(title), Rep.Some(updatedAt), pageDescription).shaped.<>({r=>import r._; _1.map(_=> PostsRow.tupled((_1.get, _2.get, _3.get, _4, _5.get, _6.get, _7.get, _8)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Option[Int]] = column[Option[Int]]("id", O.AutoInc, O.PrimaryKey)
    /** Database column content SqlType(TEXT) */
    val content: Rep[String] = column[String]("content")
    /** Database column created_at SqlType(DATETIME) */
    val createdAt: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("created_at")
    /** Database column published_at SqlType(DATETIME), Default(None) */
    val publishedAt: Rep[Option[java.sql.Date]] = column[Option[java.sql.Date]]("published_at", O.Default(None))
    /** Database column slug_title SqlType(VARCHAR), Length(255,true) */
    val slugTitle: Rep[String] = column[String]("slug_title", O.Length(255,varying=true))
    /** Database column title SqlType(VARCHAR), Length(255,true) */
    val title: Rep[String] = column[String]("title", O.Length(255,varying=true))
    /** Database column updated_at SqlType(DATETIME) */
    val updatedAt: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("updated_at")
    /** Database column page_description SqlType(VARCHAR), Length(255,true), Default(None) */
    val pageDescription: Rep[Option[String]] = column[Option[String]]("page_description", O.Length(255,varying=true), O.Default(None))

    /** Uniqueness Index over (slugTitle) (database name index_slug_title) */
    val index1 = index("index_slug_title", slugTitle, unique=true)
  }
  /** Collection-like TableQuery object for table Posts */
  lazy val Posts = new TableQuery(tag => new Posts(tag))
}
