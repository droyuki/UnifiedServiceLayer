package edu.nccu.iotlab.usl

import kafka.serializer.StringDecoder
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Created by WeiChen on 2016/1/15.
  */
trait Service extends Serializable {
  val appName: String
  val kafkaTopicList: String
  val timeFrame: Long
  // foreachRDD: Function to process RDD[String] from DStream.
  val foreachRDD: (RDD[(String, String)]) => Unit

  def createStreamingContext(): StreamingContext = {
    val sparkConf = new SparkConf().setAppName(appName)
    val ssc = new StreamingContext(sparkConf, Seconds(timeFrame))
    val topic = kafkaTopicList.split(",").toSet
    val checkpointDir = "/checkpoint/" + appName
    ssc.checkpoint(checkpointDir)
    val kafkaParams = Map("metadata.broker.list" -> "zookeeper1:9092,zookeeper2:9092,zookeeper3:9092")
    val kafkaStream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topic)
    kafkaStream.foreachRDD(foreachRDD(_))
    ssc
  }

  def run() {
    val checkpointDir = "/checkpoint/" + appName
    val ssc = StreamingContext.getOrCreate(checkpointDir,
      () => {
        createStreamingContext()
      }
    )
    ssc.start()
    ssc.awaitTermination()
  }

  def getParametter(): (String, String, Long) = {
    println("------------------------")
    println("App name: " + appName)
    println("Kafka topic: " + kafkaTopicList)
    println("Time frame: " + timeFrame)
    (appName, kafkaTopicList, timeFrame)
  }

  def forTest(rdd: RDD[(String, String)]): Unit = {
    foreachRDD(rdd)
  }

}

class StreamingService(_appName: String, _kafkaTopicList: String, _timeFrame: Long, _foreachRDD: RDD[(String, String)] => Unit) extends Service {
  override val foreachRDD = _foreachRDD
  override val appName = _appName
  override val timeFrame = _timeFrame
  override val kafkaTopicList: String = _kafkaTopicList
}

object Service {
  def apply(appName: String, kafkaTopicList: String, timeFrame: Long, foreachRDD: RDD[(String, String)] => Unit): Service = {
    new StreamingService(appName, kafkaTopicList, timeFrame, foreachRDD)
  }
}