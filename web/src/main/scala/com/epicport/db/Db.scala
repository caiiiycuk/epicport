package com.epicport.db

import scala.slick.driver.MySQLDriver.simple._

object Db {
  val db = Database.forURL("jdbc:mysql://localhost/epicport?useUnicode=true&characterEncoding=UTF-8",
    driver = "com.mysql.jdbc.Driver",
    user = "root",
    password = "simple")
}

trait Db {
  implicit val db = Db.db
}