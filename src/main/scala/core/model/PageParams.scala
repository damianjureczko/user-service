package core.model

case class PageParams(skip: Option[Int], limit: Option[Int]) {
  require(getSkip >= 0, "skip has to be greater then or equal to 0")
  require(getLimit > 0, "limit has to greater then 0")

  def getSkip = skip.getOrElse(0)

  def getLimit = limit.getOrElse(10)
}
