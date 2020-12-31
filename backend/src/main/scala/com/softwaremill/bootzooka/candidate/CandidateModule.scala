package com.softwaremill.bootzooka.candidate

import com.softwaremill.bootzooka.http.Http
import com.softwaremill.bootzooka.util.BaseModule

trait CandidateModule extends BaseModule {
  lazy val candidateApi = new CandidateApi(http)

  def http: Http
}
