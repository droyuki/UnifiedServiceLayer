package edu.nccu.iotlab

import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.twitter.TwitterUtils

/**
  * Created by WeiChen on 2016/1/13.
  */
object TwitterStreaming extends CreateSparkContext {
  def main(args: Array[String]) {
    if (args.length != 4) {
      System.err.println("Usage: Twitter Streaming <checkpointDirectory> <timeFrame> <lang> [<filters>]")
      System.exit(1)
    }
    val Array(checkpointDirectory, timeFrame, lang) = args.take(3)
    val filters = args.takeRight(args.length - 3)

    //Twitter app key
    val consumerKey = "FqyZBWRTCLEyS8IgOqRwOwCh5"
    val consumerSecret = "ZWx4KgBpuJ9AaIARHpt59dM2K7fEpWgTDSGgY8YaevJINGONVA"
    val accessToken = "329689547-6BPOJ13eK8YFkHhjY3OTBbes8ZMwsV0reRrf0KKL"
    val accessTokenSecret = "GGtK134NLxXSnWBStQeQ5tPLoIWqnXor8Y4nHemgwDy6v"
    System.setProperty("twitter4j.oauth.consumerKey", consumerKey)
    System.setProperty("twitter4j.oauth.consumerSecret", consumerSecret)
    System.setProperty("twitter4j.oauth.accessToken", accessToken)
    System.setProperty("twitter4j.oauth.accessTokenSecret", accessTokenSecret)

    def function2CreateContext(AppName: String, checkpointDirectory: String, timeFrame: String, lang: String, filters: Array[String]): StreamingContext = {
      val ssc = createStreamingContext(AppName, checkpointDirectory, timeFrame.toLong)
      val tStream = TwitterUtils.createStream(ssc, None)
      val filterStream = tStream.filter(_.getLang == lang)
      filterStream.foreachRDD { rdd =>
        if (rdd.count() > 0) {
          rdd.foreach { status =>
            //val words = status.getText.split(" ").mkString(", ")
            //println(status.getPlace.getName)
            print("[INFO]Get Status:")
            println(status.getText)
          }
        } else {
          println("Nothing match filter.")
        }
      }
      ssc
    }
    val ssc = StreamingContext.getOrCreate(checkpointDirectory,
      () => {
        function2CreateContext("Twitter Streaming", checkpointDirectory, timeFrame, lang, filters)
      }
    )
    ssc.start()
    ssc.awaitTermination()
  }

}
