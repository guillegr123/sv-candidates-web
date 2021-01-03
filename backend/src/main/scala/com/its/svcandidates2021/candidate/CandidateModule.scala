package com.its.svcandidates2021.candidate

import com.its.svcandidates2021.http.Http
import com.its.svcandidates2021.util.BaseModule
import monix.eval.Task
import sttp.client3.SttpBackend

trait CandidateModule extends BaseModule {
  lazy val candidateService = new CandidateService(baseSttpBackend, config.google);
  lazy val candidateApi = new CandidateApi(http, candidateService)
  def baseSttpBackend: SttpBackend[Task, Any]

  def http: Http
}
