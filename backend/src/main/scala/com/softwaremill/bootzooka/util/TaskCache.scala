package com.softwaremill.bootzooka.util

import monix.eval.Task
import monix.execution.Scheduler
import monix.execution.atomic.Atomic

import java.util.concurrent.TimeUnit
import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration
import scala.util.Failure

object TaskCache {
  def cache[A](expiresIn: FiniteDuration)(tf: => Task[A])(implicit sc: Scheduler): Task[A] = {
    final case class State(f: Future[A], expiresAt: Long)
    val ref = Atomic(null: State)

    def updateAndGet(): Future[A] = synchronized {
      ref.set(State(tf.runToFuture, sc.clockRealTime(TimeUnit.MILLISECONDS) + expiresIn.toMillis))
      ref.get().f
    }

    Task.deferFuture(ref.get() match {
      case null                                                            => updateAndGet()
      case State(_, exp) if exp <= sc.clockRealTime(TimeUnit.MILLISECONDS) => updateAndGet()
      case State(f, _) if f.isCompleted                                    => {
        f.value.get match {
          case Failure(_) => updateAndGet()
          case _ => f
        }
      }
      case State(f, _)                                                     => f
    })
  }

  def memoize[A](tf: => Task[A]): Task[A] = {
    tf.memoizeOnSuccess
  }
}
