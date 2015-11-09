package core.model

/**
 * Parameters of single page of results.
 *
 * @param skip number of elements to skip from the beginning
 * @param limit number of elements that should be returned
 */
case class PageParams(skip: Option[Int], limit: Option[Int]) {
  require(getSkip >= 0, "Skip has to be greater then or equal to 0")
  require(getLimit > 0, "Limit has to be greater then 0")

  def getSkip = skip.getOrElse(0)

  def getLimit = limit.getOrElse(10)
}
