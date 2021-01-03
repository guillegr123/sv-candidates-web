package com.its.svcandidates2021

import com.its.svcandidates2021.config.{Config, ConfigModule}

package object test {
  val DefaultConfig: Config = new ConfigModule {}.config
}
