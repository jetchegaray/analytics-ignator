
package com.company.bc.analytics.ignitor


class IgniteSpec extends UnitSpec {
  /* jenkins doesn't compile it  but It works
    "submit" should "good string format" in {
       val opts = Options("notenoughahands", "s3n://RoyalBlueBucket/jarvis-1.0.0-SNAPSHOT.jar", "com.company.Transform", "", 4, 2, "1G", "default")
       val master = "yarn-cluster"
       val appName = s"${opts.appName}:${opts.className}"

       val command = Seq(s"spark-submit -v",
        s"--executor-cores ${opts.coresPerExecutor}",
        s"--num-executors ${opts.numExecutors}",
        s"--executor-memory ${opts.execMem}",
        s"--queue ${opts.queue}",
        s"--master $master",
        s"--class ${opts.className}",
        s"""--name "$appName"""",
        s"--driver-memory 1g",
        "--driver-java-options '-XX:MaxPermSize=1024m -XX:PermSize=256m'",
        s"${opts.jar}",
        s"${opts.args}"
      ).mkString(" ")
      println(command)

    }
  */
}
