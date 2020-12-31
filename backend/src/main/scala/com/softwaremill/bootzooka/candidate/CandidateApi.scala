package com.softwaremill.bootzooka.candidate

import cats.data.NonEmptyList
import com.softwaremill.bootzooka.http.Http
import com.softwaremill.bootzooka.infrastructure.Json._
import com.softwaremill.bootzooka.util.ServerEndpoints
import monix.eval.Task

class CandidateApi(http: Http) {
  import CandidateApi._
  import http._

  private val CandidatePath = "candidates"

  private val listCandidatesEndpoint = baseEndpoint.get
    .in(CandidatePath)
    .out(jsonBody[List[Candidate_OUT]])
    .serverLogic {
      case _ =>
        (for {
          candidates <- Task { List[Candidate_OUT]() }
        } yield candidates).toOut
    }

  val endpoints: ServerEndpoints =
    NonEmptyList
      .of(
        listCandidatesEndpoint
      )
      .map(_.tag("candidate"))
}

object CandidateApi {

  case class Candidate_OUT(name: String)
}
