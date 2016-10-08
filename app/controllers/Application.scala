package controllers

import play.api.libs.json._
import play.api.mvc._
import models.{Sum, Transact}
import models.Transact._
import play.api.libs.functional.syntax._

object Application extends Controller {

  /**
    * Formats Transact instance as JSON.
    */
  implicit val TransactionWrites: Writes[Transact] = (
      (JsPath \ "amount").write[Double] and
      (JsPath \ "type_name").write[String] and
      (JsPath \ "parent_id").writeNullable[Long]
    )(unlift(Transact.unapply))

  /**
    * Parses Transact JSON object
    */
  implicit val TransactionReads: Reads[Transact] = (
      (JsPath \ "amount").read[Double] and
      (JsPath \ "type_name").read[String] and
      (JsPath \ "parent_id").readNullable[Long]
    )(Transact.apply _)

  /**
    * Formats a Sum instance as JSON.
    */
  implicit val SumWrites = Json.writes[Sum]

  def getTransaction(id: Long) = Action {
    try {
      Ok(Json.toJson(Transact.getTransaction(id)))
    }
    catch{
      case e:NoSuchElementException => BadRequest("Transaction not found!")
    }
  }

  def saveTransaction(id: Long) = Action(parse.json) { implicit request =>
    val b = request.body.validate[Transact]
    b.fold(
      invalid = {
        errors => {
          BadRequest(Json.obj("status" -> "OK", "message" -> JsError.toFlatJson(errors)))
        }
      },
      valid = {
        Transact => {
          save(id, Transact)
          Ok(Json.obj("status" -> "OK"))
        }
      }
    )
  }

  def getIds(type_name: String) = Action {
    Ok(Json.toJson(Transact.getIds(type_name)))
  }

  def getSum(id: Long) = Action {
    Ok(Json.toJson(Transact.getSum(id)))
  }

}