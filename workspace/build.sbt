name := "workspace"

version := "1.0"

scalaVersion := "2.11.7"
assemblyJarName in assembly := "UnifiedServices.jar"
fork := true

libraryDependencies ++= Dependencies.SparkLib
resolvers += "Apache HBase" at "https://repository.apache.org/content/repositories/releases"
resolvers += "mvnrepository" at "http://mvnrepository.com/artifact/"
resolvers += "central" at "http://repo1.maven.org/maven2/"

//avoid version conflict
libraryDependencies ++= Seq(
  toGroupID("org.apache.hbase") % "hbase" % "1.0.0",
  toGroupID("org.apache.hbase") % "hbase-common" % "1.0.0",
  toGroupID("org.apache.hbase") % "hbase-client" % "1.0.0",
  "org.apache.hadoop" % "hadoop-common" % "2.6.3",
  "mysql" % "mysql-connector-java" % "5.1.6",
  "org.scala-lang.modules" %% "scala-xml" % "1.0.4",
  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4",
  "net.jpountz.lz4" % "lz4" % "1.3.0",
  "log4j" % "log4j" % "1.2.17",
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