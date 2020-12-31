package com.softwaremill.bootzooka

import com.softwaremill.bootzooka.config.{Config, ConfigModule}

package object test {
  val DefaultConfig: Config = new ConfigModule {}.config
}
