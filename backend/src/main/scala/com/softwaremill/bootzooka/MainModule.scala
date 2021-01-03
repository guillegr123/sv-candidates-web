package com.softwaremill.bootzooka

import cats.data.NonEmptyList
import com.softwaremill.bootzooka.candidate.CandidateModule
import com.softwaremill.bootzooka.country.CountryModule
import com.softwaremill.bootzooka.http.{Http, HttpApi}
import com.softwaremill.bootzooka.infrastructure.InfrastructureModule
import com.softwaremill.bootzooka.metrics.MetricsModule
import com.softwaremill.bootzooka.util.ServerEndpoints

/**
  * Main application module. Depends on resources initialised in [[InitModule]].
  */
trait MainModule
    extends CandidateModule
    with CountryModule
    with MetricsModule
    with InfrastructureModule {

  lazy val http: Http = new Http()

  private lazy val endpoints: ServerEndpoints = candidateApi.endpoints concatNel countryApi.endpoints
  private lazy val adminEndpoints: ServerEndpoints = NonEmptyList.of(metricsApi.metricsEndpoint, versionApi.versionEndpoint)

  lazy val httpApi: HttpApi = new HttpApi(http, endpoints, adminEndpoints, collectorRegistry, config.api)
}
