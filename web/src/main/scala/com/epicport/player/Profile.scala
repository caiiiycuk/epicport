package com.epicport.player

import java.io.File
import com.twitter.chill.Base64
import xitrum.Action
import java.util.zip.CRC32

class Profile(identity: String) {

  import com.epicport.Configuration._

  private val userPrefix = Base64.encodeBytes(identity.getBytes).replaceAllLiterally("/", "_").replaceAllLiterally("+", "-")

  val folder = {
    val crc32 = new CRC32()
    crc32.update(identity.getBytes())
    val crcRoot = crc32.getValue.toHexString.grouped(3).foldLeft(profile)((profile, part) => new File(profile, part))
    
    new File(crcRoot.getParent(), userPrefix)
  }
  
  def mapAbsoluteFile(game: String, file: String) = 
    new File(new File(folder, game), file.replaceAllLiterally("/", "%2F"))
  
  def root(game: String) = new File(folder, game)
  
  def list(game: String) = { 
    val gameRoot = root(game)
    Option(gameRoot.list()).getOrElse(Array()).map(_.replaceAllLiterally("%2F", "/"))
  }

}

object Profile {
  import org.json4s._
  import org.json4s.jackson.JsonMethods._

  def fromJson(json: String) = {
    println(json)
    val parsed = parse(json)
    val identity = (parsed \ "identity").asInstanceOf[JString].s

    new Profile(identity)
  }

  def fromIdentity(identity: String) = new Profile(identity)

}


