package com.epicport.secret

import java.security.NoSuchAlgorithmException

object Secret {

  private val regex = """(.*):(.*)""".r
  private val salt = "пашаГероЙИнтернета"
  
  def decodeSecret(encoded: Option[String]): Boolean = {
    val aMatch = encoded.flatMap {
      regex.findFirstMatchIn(_)
    }
    
    aMatch.map(m => {
      val timePart = m.group(1)
      val secretPart = m.group(2)
      val secretForPart = secret(timePart)
      
      secretPart == secretForPart
    }).getOrElse(false)
  }
    
    
  private def secret(timePart: String): String = {
    val MD5 = "MD5"
    
    try {
      val digest = java.security.MessageDigest.getInstance(MD5)
      digest.update(timePart.toString.getBytes)
      digest.update(salt.getBytes)
      val messageDigest = digest.digest()
      val hexString = new StringBuilder()
      for (aMessageDigest <- messageDigest) {
        var h = java.lang.Integer.toHexString(0xFF & aMessageDigest)
        while (h.length < 2) h = "0" + h
        hexString.append(h)
      }
      hexString.toString
    } catch {
      case e: NoSuchAlgorithmException => "nosuchalgorithm"
    }
  }
}
	

