// Most Scala projects are published to Sonatype, but Sonatype is not default
// and it takes several hours to sync from Sonatype to Maven Central
resolvers += "SonatypeReleases" at "http://oss.sonatype.org/content/repositories/releases/"

// Run sbt/sbt eclipse to create Eclipse project file
addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.3.0")

// Run sbt/sbt gen-idea to create IntelliJ project file
addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.5.2")

// Run sbt/sbt xitrum-package to prepare for deploying to production environment
addSbtPlugin("tv.cntt" % "xitrum-package" % "1.6")

// For compiling Scalate templates in the compile phase of SBT
addSbtPlugin("com.mojolly.scalate" % "xsbt-scalate-generator" % "0.4.2")
