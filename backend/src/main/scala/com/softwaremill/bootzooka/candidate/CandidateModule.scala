package com.softwaremill.bootzooka.candidate

import com.softwaremill.bootzooka.http.Http
import com.softwaremill.bootzooka.util.BaseModule
import monix.eval.Task
import sttp.client3.SttpBackend

trait CandidateModule extends BaseModule {
  lazy val candidateService = new CandidateService(baseSttpBackend, config.google);
  lazy val candidateApi = new CandidateApi(http, candidateService)
  def baseSttpBackend: SttpBackend[Task, Any]

  def http: Http
}
