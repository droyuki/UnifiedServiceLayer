package edu.nccu.iotlab.example
/**
  * Streaming Service Example
  * App Name: "TestKafka"
  * Kafka topic: "data.test"
  * Time frame: 1 sec
  * print the length of RDD in each time frame.
  */

import edu.nccu.iotlab.Service
import org.apache.spark.rdd.RDD

object StreamingService {

  def main(args:Array[String]): Unit = {
    val myService = Service("TestKafka", "data.test", 1,
      (rdd: RDD[(String, String)]) => rdd.map(_._2).foreach(println(_))
    )
    myService.run()
  }
}