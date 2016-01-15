package ecu.nccu.iotlab


import edu.nccu.iotlab.Service
import org.apache.log4j.{Level, LogManager}
import org.apache.spark.rdd.RDD
import utils.SparkTest

/**
  * Created by WeiChen on 2016/1/15.
  */
class FactoryTest extends SparkTest {
  localTest("Streaming Factory") { sc =>
    LogManager.getRootLogger.setLevel(Level.ERROR)
    val rdd = sc.parallelize(Array(("1", "msg_one"), ("2", "msg_two")))
    val testService = Service("TestService", "test_topic", 2, (rdd: RDD[(String, String)]) => rdd.map(_._2).foreach(println(_)))
    testService.printParametter()
    testService.forTest(rdd)
  }
}
