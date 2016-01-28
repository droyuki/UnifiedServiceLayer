package edu.nccu.iotlab.usl.util

/**
  * Created by WeiChen on 2016/1/8.
  */

import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.{HColumnDescriptor, HTableDescriptor, TableName, HBaseConfiguration}
import org.apache.hadoop.hbase.client._
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

    //create table
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
      //get table
      val table = conn.getTable(userTable)

      try {
        //插入key = id001 的資料
        val p = new Put("id001".getBytes)
        //為put操作指定 column 和 value （舊的put.add 方法被deprecated了）
        p.addColumn("basic".getBytes, "name".getBytes, "iotlab".getBytes)
        table.put(p)

        //查詢
        val g = new Get("id001".getBytes)
        val result = table.get(g)
        val value = Bytes.toString(result.getValue("basic".getBytes, "name".getBytes))
        println("GET id001 :" + value)

        //掃描
        val s = new Scan()
        s.addColumn("basic".getBytes, "name".getBytes)
        val scanner = table.getScanner(s)

        try {
          for (r <- scanner) {
            println("Found row: " + r)
            println("Found value: " + Bytes.toString(r.getValue("basic".getBytes, "name".getBytes)))
          }
        } finally {
          scanner.close()
        }

        //Delete data
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
