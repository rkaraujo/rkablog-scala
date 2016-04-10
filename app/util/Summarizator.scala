package util

object Summarizator {

  def summarize(text: String, maxLength: Int): String = {
    val firstParagraph: String = text.split("\\n")(0)
    if (firstParagraph.length < maxLength) {
      return firstParagraph
    }

    val paragraphCut: String = firstParagraph.substring(0, maxLength)

    val lastIndexOfPunctuation: Int = Array( '.', '?', '!' )
      .map(punctuation => paragraphCut.lastIndexOf(punctuation))
      .max

    val lastIndexOfComma: Int = paragraphCut.lastIndexOf(',')

    if (lastIndexOfPunctuation > lastIndexOfComma) {
      paragraphCut.substring(0, lastIndexOfPunctuation)
    } else {
      paragraphCut.substring(0, lastIndexOfComma) + " ..."
    }
  }

}
