name := """iytewall"""

version := "1.0"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.12.4"

crossScalaVersions := Seq("2.11.12", "2.12.4")

resolvers += Resolver.sbtPluginRepo("releases")

libraryDependencies += guice
libraryDependencies += javaJdbc
libraryDependencies += ws

// Test Database
libraryDependencies += "com.h2database" % "h2" % "1.4.196"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1"

libraryDependencies += "com.auth0" % "java-jwt" % "3.3.0"

libraryDependencies += "org.postgresql" % "postgresql" % "42.2.1"

