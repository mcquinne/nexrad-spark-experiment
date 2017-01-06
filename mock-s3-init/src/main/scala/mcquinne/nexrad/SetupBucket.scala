package mcquinne.nexrad

import java.io.File

import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.{AmazonS3Client, S3ClientOptions}

object SetupBucket extends App {
  val key = sys.env.getOrElse("AWS_ACCESS_KEY_ID", "ID")
  val secret = sys.env.getOrElse("AWS_SECRET_ACCESS_KEY", "KEY")
  val bucketName = sys.env.getOrElse("BUCKET_NAME", "noaa-nexrad-level2")
  val protocol = sys.env.getOrElse("S3_PROTOCOL", "http")
  val host = sys.env.getOrElse("S3_HOST", "s3.amazonaws.com")
  val port = sys.env.getOrElse("S3_PORT", "80")

  val s3 = new AmazonS3Client(new BasicAWSCredentials(key, secret))
  s3.setS3ClientOptions(S3ClientOptions.builder()
      .setPathStyleAccess(true)
      .disableChunkedEncoding()
      .build()
  )
  s3.setEndpoint(s"$protocol://$host:$port")

  val bucket = s3.createBucket(bucketName)
  println(s"Created: $bucket")

  var numFiles = 0
  val dataDir = new File("/data")
  dataDir.listFiles().foreach((f) => {
    val result = s3.putObject(bucketName, f.getName, f)
    println(s"Put file $f to bucket object $result")
    numFiles += 1
  })

  println(s"Initialized: $bucket with $numFiles files")
}
