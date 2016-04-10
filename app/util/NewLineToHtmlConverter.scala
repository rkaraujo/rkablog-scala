package util

/**
  * Created by Renato on 08/04/2016.
  */
object NewLineToHtmlConverter {

  def convert(text: String): String = {
    text.split("\\n")
      .filter(_.length > 0)
      .map(text => "<p>" + text + "</p>")
      .mkString
  }

}
