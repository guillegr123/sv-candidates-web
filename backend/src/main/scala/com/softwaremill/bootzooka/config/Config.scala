package com.softwaremill.bootzooka.config

import com.softwaremill.bootzooka.http.HttpConfig

/**
  * Maps to the `application.conf` file. Configuration for all modules of the application.
  */
case class Config(api: HttpConfig)
