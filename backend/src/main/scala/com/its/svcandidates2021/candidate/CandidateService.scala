package com.its.svcandidates2021.candidate

import com.its.svcandidates2021.config.GoogleConfig
import com.typesafe.scalalogging.StrictLogging
import monix.eval.Task
import sttp.client3._
import sttp.client3.circe._
import io.circe.generic.auto._
import scala.concurrent.duration.DurationInt
import monix.execution.Scheduler
import scala.language.postfixOps

class CandidateService(
                        baseSttpBackend: SttpBackend[Task, Any],
                        config: GoogleConfig
) extends StrictLogging {

  import com.its.svcandidates2021.util.TaskCache._

  implicit val scheduler = Scheduler.global

  val listCandidates: Task[List[Candidate]] = cache(30 minutes) {
    basicRequest
      .get(uri"${config.sheetUrl}?majorDimension=ROWS&valueRenderOption=FORMULA&key=${config.apiKey}")
      .response(asJson[SheetDataResponse])
      .send(baseSttpBackend)
      .map(response => response.body)
      .map {
        case Left(ex) => throw ex
        case Right(sheetData) => sheetData.values.drop(1).takeWhile {
          case name :: _ => name != null && !name.isEmpty
          case Nil => false
        } map {
          case name :: party :: twitter :: _ :: _ :: province :: municipality :: position ::  _ => Candidate(
            name,
            party,
            twitter match {
              case "" => None
              case "N/A" => None
              case twitterHandle => Some(s"https://twitter.com/${twitterHandle.replace("@", "")}")
            },
            province,
            municipality match {
              case "" => None
              case "N/A" => None
              case municipality => Some(municipality)
            },
            position
          )
          case _ :: _ => ???
          case Nil => ???
        }
      }
  }
}

case class SheetDataResponse(range: String, majorDimension: String, values: List[List[String]])

case class Candidate(name: String, party: String, twitter: Option[String], province: String, municipality: Option[String], position: String)
