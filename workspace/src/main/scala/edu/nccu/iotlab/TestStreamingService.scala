package edu.nccu.iotlab

import org.apache.spark.rdd.RDD

/**
  * Created by WeiChen on 2016/1/15.
  */
object TestStreamingService {
  /**
    * Streaming Service Example
    * App Name: "TestKafka"
    * Kafka topic: "data.test"
    * Time frame: 1 sec
    * print the length of an RDD in each time frame.
    */
  def main(args:Array[String]) {
    val myNonStreamingService = Service("TestKafka", "data.test", 1,
      (rdd: RDD[(String, String)]) => rdd.map(_._2).foreach(println(_))
    )
    myNonStreamingService.run()
  }
}
