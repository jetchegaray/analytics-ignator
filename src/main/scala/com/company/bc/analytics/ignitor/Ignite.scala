
package com.company.bc.analytics.ignitor

import java.net.URI
import java.util.stream.Collector

import com.google.common.collect.Lists
import org.apache.hadoop.yarn.api.ApplicationConstants
import org.apache.hadoop.yarn.api.records._
import org.apache.hadoop.yarn.client.api.YarnClient
import org.apache.hadoop.yarn.util.Records
import collection.JavaConversions._
import scala.collection.JavaConverters._

class Ignite(resourceManagerUri: URI) {

  private lazy val yc = {
    val yc = YarnClient.createYarnClient()
    yc.init(ConfParser.fetchHadoopConf(resourceManagerUri.toString))
    yc.start()
    yc
  }

  /**
    * passthrough for the ignitor call with default memory allocation
    * --driver-memory and --conf I hardcoded here because It will be use just for a while. The code that use this will be migrate to fury
    *
    * @param opts the options object that defines configurations for the spark job execution
    * @return the application id of the ignitor job
    */
  def ignite(opts: Options): String = {
    ignite(opts, 16)
  }

  /**
    * sends a small job that runs spark submit to get it to run on the spark server
    *
    * @param opts the options object that defines configurations for the spark job execution
    * @param mem  the amount of memory the ignitor should use for submitting the job (generally bigger uberjar = more memory)
    * @return the application id of the ignitor job
    */
  def ignite(opts: Options, mem: Int): String = {

    val app = yc.createApplication()
    val availableResources = app.getNewApplicationResponse.getMaximumResourceCapability
    println( s"""\n\tMem:${availableResources.getMemory}\n\tVirtualCores:${availableResources.getVirtualCores}\n""")

    val appContext = app.getApplicationSubmissionContext
    appContext.setApplicationName(opts.appName)
    appContext.setQueue("default")
    appContext.setResource(Resource.newInstance(mem, 1))
    appContext.setAMContainerSpec(makeContainer(opts))
    appContext.setApplicationType("IGNITOR")
    appContext.setApplicationTags(Set("IGNITOR").asJava)
    appContext.setMaxAppAttempts(1)

    yc.submitApplication(appContext)
    //perhaps return this? But it will be mostly useless. A short lived application that will merely execute the command spark-submit
    appContext.getApplicationId.toString
  }

  private def makeContainer(opts: Options) = {

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
      s"${opts.args}",
      s"1>${ApplicationConstants.LOG_DIR_EXPANSION_VAR}/stdout",
      s"2>${ApplicationConstants.LOG_DIR_EXPANSION_VAR}/stderr"
    ).mkString(" ")

    val container = Records.newRecord(classOf[ContainerLaunchContext])
    container.setCommands(List(command).asJava)
    println(s"${container.getCommands}")
    container
  }


  /**
    * Kill all the Ignitor process.
    *
    */
  def killThemAll() {

    val queueInfo = yc.getAllQueues().find(_.getQueueName() eq "default")

    val applications = queueInfo.get.getApplications.filter(
      app => (app.getApplicationType == "IGNITOR" &&  (app.getYarnApplicationState == YarnApplicationState.ACCEPTED) ||
        app.getYarnApplicationState == YarnApplicationState.RUNNING))

    applications.foreach{ app => yc.killApplication(app.getApplicationId)}

  }

}
