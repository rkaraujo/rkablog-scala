/**
  * Created by Renato on 04/04/2016.
  */
object SlickGenerator extends App {
  slick.codegen.SourceCodeGenerator.main(
    Array("slick.driver.MySQLDriver", "com.mysql.jdbc.Driver", "jdbc:mysql://localhost/rkablog", "C:\\Development\\Scala\\workspace\\rkablog-scala", "app.models", "root", "a")
  )
}
