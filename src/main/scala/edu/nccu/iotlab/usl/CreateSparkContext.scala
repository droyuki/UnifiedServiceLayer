package edu.nccu.iotlab.usl

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Created by WeiChen on 2016/1/12.
  */
class CreateSparkContext {
  def createStreamingContext(appName: String, checkpointDirectory: String, timeFrame: Long): StreamingContext = {
    val sparkConf = new SparkConf().setAppName(appName)
    val ssc =  new StreamingContext(sparkConf, Seconds(timeFrame))
    ssc.checkpoint(checkpointDirectory)
    ssc
  }
}
