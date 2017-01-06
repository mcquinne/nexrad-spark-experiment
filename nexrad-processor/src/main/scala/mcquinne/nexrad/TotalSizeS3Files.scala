package mcquinne.nexrad

import com.amazonaws.services.s3._
import com.amazonaws.auth.BasicAWSCredentials
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import scala.collection.JavaConversions._

object TotalSizeS3Files extends App {
  val key = sys.env.getOrElse("AWS_ACCESS_KEY_ID", "ID")
  val secret = sys.env.getOrElse("AWS_SECRET_ACCESS_KEY", "KEY")
  val bucket = sys.env.getOrElse("BUCKET_NAME", "noaa-nexrad-level2")
  val protocol = sys.env.getOrElse("S3_PROTOCOL", "http")
  val host = sys.env.getOrElse("S3_HOST", "s3.amazonaws.com")
  val port = sys.env.getOrElse("S3_PORT", "80")
  val pageSize = sys.env.getOrElse("PAGE_SIZE", "1000").toInt
  val fullResults = if (sys.env.getOrElse("FULL_RESULTS", "") == "") false else true

  val endpoint = s"$protocol://$host:$port"
  val prefixes = PrefixGenerator.generate(fullResults)
  val requestInfos = prefixes.map(prefix => (key, secret, endpoint, bucket, prefix))

  val conf = new SparkConf().setAppName(getClass.getSimpleName)
  val sc = new SparkContext(conf)
  val matchingObjects = sc.parallelize(requestInfos)
      .map(info => {
        val s3 = new AmazonS3Client(new BasicAWSCredentials(info._1, info._2))
        s3.setEndpoint(info._3)
        s3.listObjects(info._4, info._5)
      })
      .flatMap(listing => iterableAsScalaIterable(listing.getObjectSummaries))

  val totalCount = matchingObjects.count()
  val totalSize = matchingObjects.map(_.getSize).sum()

  println(s"Matched $totalCount files with total size: $totalSize bytes")
  sc.stop()
}
