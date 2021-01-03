package com.softwaremill.bootzooka.country

import cats.data.NonEmptyList
import com.softwaremill.bootzooka.http.Http
import com.softwaremill.bootzooka.util.ServerEndpoints

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
