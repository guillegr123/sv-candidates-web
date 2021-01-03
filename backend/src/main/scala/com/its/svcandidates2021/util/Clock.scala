package com.its.svcandidates2021.util

import java.time.Instant

import monix.eval.Task

trait Clock {
  def now(): Task[Instant]
}
