package com.its.svcandidates2021.country

import cats.data.NonEmptyList
import com.its.svcandidates2021.http.Http
import com.its.svcandidates2021.util.ServerEndpoints

class CountryApi(http: Http, countryService: CountryService) {
  import http._

  private val CountryPath = "pais"

  private val listSubdivisionsEndpoint = baseEndpoint.get
    .in(CountryPath / "subdivisiones")
    .out(jsonBody[Map[String, List[String]]])
    .serverLogic {
      case _ => countryService.listSubdivisions.toOut
    }

  val endpoints: ServerEndpoints =
    NonEmptyList
      .of(
        listSubdivisionsEndpoint
      )
      .map(_.tag("País"))
}
