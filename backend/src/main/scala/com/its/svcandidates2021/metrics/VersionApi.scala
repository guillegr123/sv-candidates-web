package com.its.svcandidates2021.metrics

import com.its.svcandidates2021.http.{Error_OUT, Http}
import com.its.svcandidates2021.infrastructure.Json._
import com.its.svcandidates2021.version.BuildInfo
import monix.eval.Task
import sttp.model.StatusCode
import sttp.tapir.server.ServerEndpoint

/**
  * Defines an endpoint which exposes the current application version information.
  */
class VersionApi(http: Http) {
  import VersionApi._
  import http._

  val versionEndpoint: ServerEndpoint[Unit, (StatusCode, Error_OUT), Version_OUT, Any, Task] = baseEndpoint.get
    .in("version")
    .out(jsonBody[Version_OUT])
    .serverLogic { _ =>
      Task.now(Version_OUT(BuildInfo.builtAtString, BuildInfo.lastCommitHash)).toOut
    }
}

object VersionApi {
  case class Version_OUT(buildDate: String, buildSha: String)
}
