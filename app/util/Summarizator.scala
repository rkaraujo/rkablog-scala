package util

object Summarizator {

  def summarize(text: String, maxLength: Int): String = {
    val firstParagraphStart = text.indexOf("<p>")
    val firstParagraphEnd = text.indexOf("</p>")
    if (firstParagraphStart < 0 || firstParagraphEnd < 0 ) {
      return "Text"
    }
    val firstParagraph: String = text.substring(firstParagraphStart + 3, firstParagraphEnd)
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
