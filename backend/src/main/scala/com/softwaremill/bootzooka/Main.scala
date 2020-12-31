package com.softwaremill.bootzooka

import com.softwaremill.bootzooka.config.Config
import com.softwaremill.bootzooka.infrastructure.CorrelationId
import com.softwaremill.bootzooka.metrics.Metrics
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
