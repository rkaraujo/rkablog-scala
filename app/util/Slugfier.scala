package util

/**
  * Created by Renato on 24/04/2016.
  */
object Slugfier {

  def slugfy(str: String): String = {
    str.replace(".", "")
      .replaceAll("\\W", " ")
      .trim
      .replaceAll("\\s+", "-")
      .toLowerCase
  }

}
