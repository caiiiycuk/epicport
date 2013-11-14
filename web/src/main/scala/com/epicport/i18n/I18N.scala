package com.epicport.i18n

object I18N {
  private final val alternatePattern = """^/\w+/""".r
  
  final val languages = Set[String]("en", "ru")
  
  def alternateUri(baseUri: String): (String) => String = {
    (lang: String) =>
      alternatePattern.replaceFirstIn(baseUri, s"/$lang/")
  }
}