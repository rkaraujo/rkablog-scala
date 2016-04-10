package util

import org.mindrot.jbcrypt.BCrypt

import scala.io.Source

object CheckUsersFile {

  val USERS_FILE = "/export/rkablog/users.txt"
  val FIELDS_SEPARATOR = ":"

  def checkLine(line: String): Boolean = {
    val lines = Source.fromFile(USERS_FILE).getLines()
    lines.contains(line)
  }

  def checkUsernamePassword(username: String, password: String): Boolean = {
    val lines = Source.fromFile(USERS_FILE).getLines()
    lines
      .map(_.split(FIELDS_SEPARATOR))
      .map(splitted => (splitted(0).trim(), splitted(1).trim))
      .filter(userPassword => userPassword._1 == username && BCrypt.checkpw(password, userPassword._2))
      .nonEmpty
  }

  def getUsernameLine(username: String): Option[String] = {
    val lines = Source.fromFile(USERS_FILE).getLines()
    lines.find(_.startsWith(username))
  }
}
