package forms

import java.util.Date

import models.{KartallaDao, PaikkaDao}
import play.api.data.Form
import play.api.data.Forms._

object PaikkaForm {

  case class MapContent(content: String)
  case class FollowKartalla(userId: String, star: Int, sana: String, paikkaId: Int, createdAt: Date, lat: Double, lng: Double)
  case class Paikka(id: Int, name: String, kana: String,tag: String, text: Option[String] = None, postalCode: String, address: String)
  case class CreatePaikkaForm(name: String, kana: String,tag: String, text: Option[String] = None, postalCode: String, address: String)

  val mapContent = Form(
    mapping(
      "content" -> nonEmptyText
    )(MapContent.apply)(MapContent.unapply)
  )

  val createForm = Form(
    mapping(
      "name" -> nonEmptyText(maxLength = 20),
      "kana" -> nonEmptyText(maxLength = 30),
      "tag" -> nonEmptyText(maxLength = 20),
      "text" -> optional(text(maxLength = 255)),
      "postal_code" -> nonEmptyText,
      "address" -> nonEmptyText
    )(CreatePaikkaForm.apply)(CreatePaikkaForm.unapply)
  )


  val createKartallaForm = Form(
    mapping(
      "star" -> number,
      "sana" -> nonEmptyText
    )(KartallaDao.CreateKartalla.apply)(KartallaDao.CreateKartalla.unapply)
  )



}
