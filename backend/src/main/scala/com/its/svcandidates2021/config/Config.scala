package com.its.svcandidates2021.config

import com.its.svcandidates2021.http.HttpConfig

/**
  * Maps to the `application.conf` file. Configuration for all modules of the application.
  */
case class Config(api: HttpConfig, google: GoogleConfig)
