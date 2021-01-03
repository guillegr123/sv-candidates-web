package com.its.svcandidates2021.country

import com.its.svcandidates2021.http.Http
import com.its.svcandidates2021.util.BaseModule
import monix.eval.Task
import sttp.client3.SttpBackend

trait CountryModule extends BaseModule {
  lazy val countryService = new CountryService(baseSttpBackend);
  lazy val countryApi = new CountryApi(http, countryService)
  def baseSttpBackend: SttpBackend[Task, Any]

  def http: Http
}
