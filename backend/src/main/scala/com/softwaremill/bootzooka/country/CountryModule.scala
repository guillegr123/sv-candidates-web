package com.softwaremill.bootzooka.country

import com.softwaremill.bootzooka.http.Http
import com.softwaremill.bootzooka.util.BaseModule
import monix.eval.Task
import sttp.client3.SttpBackend

trait CountryModule extends BaseModule {
  lazy val countryService = new CountryService(baseSttpBackend);
  lazy val countryApi = new CountryApi(http, countryService)
  def baseSttpBackend: SttpBackend[Task, Any]

  def http: Http
}
