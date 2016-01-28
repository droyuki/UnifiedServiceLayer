import sbt._

object Version {
  val scalaTest = "2.2.4"
  val spark     = "1.6.0"
}

object Library {
  val scalaTest             = "org.scalatest"     %%    "scalatest"                    % Version.scalaTest
  val sparkStreaming        = "org.apache.spark"  %%    "spark-streaming"              % Version.spark
  val sparkCore             = "org.apache.spark"  %%    "spark-core"                   % Version.spark
  val sparkSql              = "org.apache.spark"  %%    "spark-sql"                    % Version.spark
  val sparkMlLib            = "org.apache.spark"  %%    "spark-mllib"                  % Version.spark
  val sparkStreamKafka      = "org.apache.spark"  %%    "spark-streaming-kafka"        % Version.spark
  val sparkStreamTwitter    = "org.apache.spark"  %%    "spark-streaming-twitter"      % Version.spark
}

object Dependencies {

  import Library._

  val SparkLib = Seq(
    sparkCore % "provided",
    sparkStreaming % "provided",
    sparkSql % "provided",
    sparkMlLib % "provided",
    sparkStreamKafka,
    sparkStreamTwitter,
    scalaTest % "test"
  )
}
