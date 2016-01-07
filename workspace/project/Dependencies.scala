import sbt._

object Version {
  val scala     = "2.11.7"
  val akka      = "2.3.11"
  val mockito   = "1.10.19"
  val scalaTest = "2.2.4"
  val spark     = "1.6.0"
}

object Library {
  val scala                 = "org.scala-lang"    %     "scala-reflect"                % Version.scala
  val akkaActor             = "com.typesafe.akka" %%    "akka-actor"                   % Version.akka
  val akkaTestKit           = "com.typesafe.akka" %%    "akka-testkit"                 % Version.akka
  val mockitoAll            = "org.mockito"       %     "mockito-all"                  % Version.mockito
  val scalaTest             = "org.scalatest"     %%    "scalatest"                    % Version.scalaTest
  val sparkStreaming        = "org.apache.spark"  %%    "spark-streaming"              % Version.spark
  val sparkCore             = "org.apache.spark"  %%    "spark-core"                   % Version.spark
  val sparkSql              = "org.apache.spark"  %%    "spark-sql"                    % Version.spark
  val sparkGraphX           = "org.apache.spark"  %%    "spark-graphx"                 % Version.spark
  val sparkMlLib            = "org.apache.spark"  %%    "spark-mllib"                  % Version.spark
  val sparkStreamKafka      = "org.apache.spark"  %%    "spark-streaming-kafka"        % Version.spark
  val sparkStreamTwitter    = "org.apache.spark"  %     "spark-streaming-twitter_2.10" % Version.spark
}

object Dependencies {

  import Library._

  val SparkLib = Seq(
    scala,
    sparkCore % "provided",
    sparkStreaming % "provided",
    sparkSql % "provided",
    sparkGraphX % "provided",
    sparkMlLib % "provided",
    sparkStreamKafka,
    sparkStreamTwitter,
    scalaTest % "test",
    akkaActor,
    akkaTestKit,
    mockitoAll % "test"
  )
}
