package com.softwaremill.bootzooka.candidate

import cats.data.NonEmptyList
import com.softwaremill.bootzooka.http.Http
import com.softwaremill.bootzooka.infrastructure.Json._
import com.softwaremill.bootzooka.util.ServerEndpoints

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
      .map(_.tag("candidatos"))
}

object CandidateApi {

  case class Candidato(nombre: String, partido: String, twitter: Option[String], departamento: String, municipio: Option[String], cargo: String)
}
