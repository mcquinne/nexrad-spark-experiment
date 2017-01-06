package mcquinne.nexrad

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

object HelloWorld {
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("HelloWorld")
    val sc = new SparkContext(conf)

    println(sc.parallelize(Array(1, 2, 3, 4, 5)).sum())
    sc.stop()
  }
}
