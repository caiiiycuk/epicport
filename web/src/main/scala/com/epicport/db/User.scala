package com.epicport.db

import java.security.MessageDigest

import scala.Array.canBuildFrom
import scala.slick.jdbc.GetResult
import scala.slick.jdbc.StaticQuery.interpolation
import scala.slick.session.Database
import scala.slick.session.Database.threadLocalSession

import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.json4s.JsonDSL._

case class User(email: String, name: String) {
  def identity = s"epicport.com/u/$email"

  def toJson =
    compact(render(("identity" -> identity) ~ ("first_name" -> name)))
}

object User {

  private val digest = MessageDigest.getInstance("SHA1")

  implicit val getUserResult = GetResult(r => User(r.nextString, r.nextString))

  def byEmail(email: String)(implicit db: Database): Option[User] = {
    sql"""select email, name from users where email = $email"""
      .as[User].firstOption
  }

  def byEmail(email: String, password: String)(implicit db: Database): Option[User] = {
    val hashedPassword = hashPassword(password)
    
    sql"""select email, name from users where email = $email and password = $hashedPassword"""
      .as[User].firstOption
  }

  def create(email: String, name: String, password: String)(implicit db: Database): User = {
    val hashedPassword = hashPassword(password)

    sqlu"""insert into users(email, name, password) values($email, $name, $hashedPassword)"""
      .execute

    User(email, name)
  }

  def hashPassword(password: String) =
    digest.digest(password.getBytes).map("%02X".format(_)).mkString

}