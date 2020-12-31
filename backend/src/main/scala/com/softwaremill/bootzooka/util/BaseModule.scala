package com.softwaremill.bootzooka.util

import com.softwaremill.bootzooka.config.Config

trait BaseModule {
  def config: Config
}
