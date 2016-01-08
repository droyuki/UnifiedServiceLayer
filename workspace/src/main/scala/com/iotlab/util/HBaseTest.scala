package com.iotlab.util

/**
  * Created by WeiChen on 2016/1/8.
  */

import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.{HColumnDescriptor, HTableDescriptor, TableName, HBaseConfiguration}
import org.apache.hadoop.hbase.client._
import org.apache.spark.SparkContext
import scala.collection.JavaConversions._

object HBaseTest {
  private val zookeeperQuorumIP = "zookeeper1,zookeeper2,zookeeper3"
  val table = new scala.collection.mutable.HashMap[String, Table]()
  //val conf = new HBaseUtils().getConnection
  val TABLE_NAME = "TestData"
  val COLUMN_FAMILY_NAME = "cf"


  def main(args: Array[String]) {
    val config = HBaseConfiguration.create()
    config.set("hbase.zookeeper.quorum", zookeeperQuorumIP)
    config.set("hbase.zookeeper.property.clientPort", "2181")
    //config.set("hbase.master", "hadoopmaster:16000")
    val conn = ConnectionFactory.createConnection(config)
    val admin = conn.getAdmin


    val userTable = TableName.valueOf("TestTable")

    //创建 user 表
    val tableDescr = new HTableDescriptor(userTable)
    tableDescr.addFamily(new HColumnDescriptor("basic".getBytes))
    println("Creating table `TestTable`. ")
    if (admin.tableExists(userTable)) {
      admin.disableTable(userTable)
      admin.deleteTable(userTable)
    }
    admin.createTable(tableDescr)
    println("Done!")

    try {
      //获取 user 表
      val table = conn.getTable(userTable)

      try {
        //准备插入一条 key 为 id001 的数据
        val p = new Put("id001".getBytes)
        //为put操作指定 column 和 value （以前的 put.add 方法被弃用了）
        p.addColumn("basic".getBytes, "name".getBytes, "wuchong".getBytes)
        //提交
        table.put(p)

        //查询某条数据
        val g = new Get("id001".getBytes)
        val result = table.get(g)
        val value = Bytes.toString(result.getValue("basic".getBytes, "name".getBytes))
        println("GET id001 :" + value)

        //扫描数据
        val s = new Scan()
        s.addColumn("basic".getBytes, "name".getBytes)
        val scanner = table.getScanner(s)

        try {
          for (r <- scanner) {
            println("Found row: " + r)
            println("Found value: " + Bytes.toString(r.getValue("basic".getBytes, "name".getBytes)))
          }
        } finally {
          //确保scanner关闭
          scanner.close()
        }

        //删除某条数据,操作方式与 Put 类似
        val d = new Delete("id001".getBytes)
        d.addColumn("basic".getBytes, "name".getBytes)
        table.delete(d)

      } finally {
        if (table != null) table.close()
      }

    } finally {
      conn.close()
    }
  }
}
