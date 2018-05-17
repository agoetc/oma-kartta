package forms

import java.util.Date

import models.{KartallaDao, PaikkaDao}
import play.api.data.Form
import play.api.data.Forms._

object PaikkaForm {

  case class MapContent(content: String)
  case class FollowKartalla(userId: String, star: Int, sana: String, paikkaId: Int, createdAt: Date, lat: Double, lng: Double)
  case class Paikka(id: Int, name: String, kana: String, text: Option[String] = None, postalCode: String, address: String)

  val mapContent = Form(
    mapping(
      "content" -> nonEmptyText
    )(MapContent.apply)(MapContent.unapply)
  )

  val newForm = Form(
    mapping(
      "name" -> nonEmptyText,
      "kana" -> nonEmptyText,
      "text" -> optional(text),
      "postal_code" -> nonEmptyText,
      "address" -> nonEmptyText
    )(PaikkaDao.PaikkaNewForm.apply)(PaikkaDao.PaikkaNewForm.unapply)
  )


  val createKartallaForm = Form(
    mapping(
      "star" -> number,
      "sana" -> nonEmptyText
    )(KartallaDao.CreateKartalla.apply)(KartallaDao.CreateKartalla.unapply)
  )



}
