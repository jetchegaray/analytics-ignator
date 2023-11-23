
package com.company.bc.analytics.ignitor

/**
  * Represents options sent to spark-submit
  *
  * @param appName          shows up in the history interface
  * @param jar              location of the spark jar e.g. s3n://Bucket/path/to/app.jar or hdfs://dir/of/app.jar For now, only s3n and hdfs is supported.
  *                         If a local (file://) is needed, will have to be added as a local resource to the appContext. So far, not needed.
  * @param className        class that contains a main method and is the entry point to your spark app
  * @param args             string of arguments that your app jar will take. We pass in one long string so if you want multiple args, just space separate
  *                         them e.g. "debug 1 2 3" would pass 4 arguments to the className's main method.
  *                         max size of arguments passed to command is 130885 bytes... enjoy. including spark-submit and the logging stuff(mandatory),
  *                         that is about 130834 additional characters. Probably not enough for all of the JSON needed by BCSS... hmmm
  * @param coresPerExecutor how many cores per executor this job will use
  * @param numExecutors     the number of executors, nodes, that will be utilized
  * @param execMem          amount of memory that each executor will get
  * @param queue            the queue that will be used. This can be used for priority and other features.
  */
case class Options(
                    appName: String,
                    jar: String,
                    className: String,
                    args: String,
                    coresPerExecutor: Int,
                    numExecutors: Int,
                    execMem: String,
                    queue: String
                  )
