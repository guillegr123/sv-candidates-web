package com.its.svcandidates2021

import com.its.svcandidates2021.config.Config
import com.its.svcandidates2021.infrastructure.CorrelationId
import com.its.svcandidates2021.metrics.Metrics
import com.typesafe.scalalogging.StrictLogging
import monix.eval.Task
import monix.execution.Scheduler.Implicits.global
import sttp.client3.SttpBackend

object Main extends StrictLogging {
  def main(args: Array[String]): Unit = {
    CorrelationId.init()
    Metrics.init()
    Thread.setDefaultUncaughtExceptionHandler((t, e) => logger.error("Uncaught exception in thread: " + t, e))

    val initModule = new InitModule {}
    initModule.logConfig()

    val mainTask =
      initModule.baseSttpBackend.use { _baseSttpBackend =>
        val modules = new MainModule {
          override def baseSttpBackend: SttpBackend[Task, Any] = _baseSttpBackend
          override def config: Config = initModule.config
        }

        modules.httpApi.resource.use(_ => Task.never)
      }
    mainTask.runSyncUnsafe()
  }
}
