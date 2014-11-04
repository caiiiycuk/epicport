package com.epicport.action.android

import xitrum.annotation.GET
import xitrum.FutureAction

import org.json4s._
import org.json4s.jackson.JsonMethods._

object Update {
  final val DUNE2_VERSION_FILE = "public/android/dune2/version.json"
}

@GET("/android/update/dune2")
class Dune2Update extends FutureAction {

  import Update._

  def execute() = try {
    val version = paramo("version").getOrElse("{}")
    val parsedVersion = parse(version)

    val versionNameCandidate = parsedVersion \\ "version" match {
      case JString(version) => version
      case _ => ""
    }

    val versionName = parseFile(DUNE2_VERSION_FILE) \\ "version" match {
      case JString(version) => version
      case _ => throw new IllegalStateException(s"Version not set in file $DUNE2_VERSION_FILE")
    }
    
    if (versionNameCandidate != versionName) {
      respondFile(DUNE2_VERSION_FILE)
    } else {
      respondText("{}")
    }
  } catch {
    case t: Throwable => {
      log.warn("Unable to parser param version, cause: " + t.getMessage())
      respondFile(DUNE2_VERSION_FILE)
    }
  }

  private def parseFile(file: String) = {
    val source = scala.io.Source.fromFile(file)
    val contents = source.mkString
    source.close()

    parse(contents)
  }

}