package com.its.svcandidates2021.candidate

import cats.data.NonEmptyList
import com.its.svcandidates2021.http.Http
import com.its.svcandidates2021.infrastructure.Json._
import com.its.svcandidates2021.util.ServerEndpoints

class CandidateApi(http: Http, candidateService: CandidateService) {
  import CandidateApi._
  import http._

  private val CandidatePath = "candidatos"

  private val listCandidatesEndpoint = baseEndpoint.get
    .in(CandidatePath)
    .out(jsonBody[List[Candidato]])
    .serverLogic {
      case _ =>
        (for {
          candidates <- candidateService.listCandidates
        } yield candidates.map {
          case Candidate(name, party, twitter, province, municipality, position) => Candidato(name, party, twitter, province, municipality, position)
        }).toOut
    }

  val endpoints: ServerEndpoints =
    NonEmptyList
      .of(
        listCandidatesEndpoint
      )
      .map(_.tag("Candidatos"))
}

object CandidateApi {

  case class Candidato(nombre: String, partido: String, twitter: Option[String], departamento: String, municipio: Option[String], cargo: String)
}
