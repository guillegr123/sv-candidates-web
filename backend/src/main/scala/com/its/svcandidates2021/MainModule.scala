package com.its.svcandidates2021

import cats.data.NonEmptyList
import com.its.svcandidates2021.candidate.CandidateModule
import com.its.svcandidates2021.country.CountryModule
import com.its.svcandidates2021.http.{Http, HttpApi}
import com.its.svcandidates2021.infrastructure.InfrastructureModule
import com.its.svcandidates2021.metrics.MetricsModule
import com.its.svcandidates2021.util.ServerEndpoints

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
