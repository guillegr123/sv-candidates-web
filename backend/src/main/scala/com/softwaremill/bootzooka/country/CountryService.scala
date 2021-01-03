package com.softwaremill.bootzooka.country

import com.typesafe.scalalogging.StrictLogging
import monix.eval.Task
import sttp.client3._
import sttp.client3.circe._
import monix.execution.Scheduler

class CountryService(
                        baseSttpBackend: SttpBackend[Task, Any]
) extends StrictLogging {

  import com.softwaremill.bootzooka.util.TaskCache._

  implicit val scheduler = Scheduler.global

  val listSubdivisions: Task[Map[String, List[String]]] = memoize {
    basicRequest
      .get(uri"https://gist.githubusercontent.com/guillegr123/45cf7ba0ce6250d3ca2f109ac788934e/raw/177c22bd0b79b291a4647ac0694bbfef9a4bfa04/subdivisions_of_el_Salvador.json")
      .response(asJson[Map[String, List[String]]])
      .send(baseSttpBackend)
      .map(response => response.body)
      .map {
        case Left(ex) => throw ex
        case Right(value) => value
      }
  }
}
