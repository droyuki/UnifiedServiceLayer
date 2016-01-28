/**
  * Created by WeiChen on 2016/1/15.
  */
package ecu.nccu.iotlab.usl.util

/**
  * Created by WeiChen on 2015/9/2.
  */

import org.apache.spark.{SparkConf, SparkContext}
import org.scalatest.FunSuite

class SparkTest extends FunSuite {
  def localTest(name : String)(f : SparkContext => Unit) : Unit = {
    this.test(name) {
      val conf = new SparkConf()
        .setAppName(name)
        .setMaster("local")
        .set("spark.default.parallelism", "1")
      val sc = new SparkContext(conf)
      try {
        f(sc)
      } finally {
        sc.stop()
      }
    }
  }
}

