name := "workspace"

version := "1.0"

scalaVersion := "2.11.7"
fork := true

libraryDependencies ++= Dependencies.SparkLib

//avoid version conflict
libraryDependencies ++= Seq(
  "mysql" % "mysql-connector-java" % "5.1.6",
  "org.scala-lang.modules" %% "scala-xml" % "1.0.4",
  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4",
  "net.jpountz.lz4" % "lz4" % "1.3.0",
  "log4j" % "log4j" % "1.2.17",
  "org.slf4j" % "slf4j-api" % "1.7.10",
  "org.xerial.snappy" % "snappy-java" % "1.1.2"
)

//some libs will force upgrade scala version.
ivyScala := ivyScala.value map {
  _.copy(overrideScalaVersion = true)
}

baseAssemblySettings
assemblyMergeStrategy in assembly := {
  case m if m.toLowerCase.endsWith("manifest.mf") => MergeStrategy.discard
  case m if m.toLowerCase.matches("meta-inf.*\\.sf$") => MergeStrategy.discard
  case "log4j.properties" => MergeStrategy.discard
  case m if m.toLowerCase.startsWith("meta-inf/services/") => MergeStrategy.filterDistinctLines
  case "reference.conf" => MergeStrategy.concat
  case _ => MergeStrategy.first
}

test in assembly := {}

fork in run := true