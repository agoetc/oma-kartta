package controllers

import javax.inject._
import play.api.mvc._
import play.api.libs.json.Writes._
import play.api.libs.json._
import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.language.postfixOps
import com.koddi.geocoder.Geocoder
import com.typesafe.config.ConfigFactory
import utils.AuthenticatedAction
import forms.PaikkaForm._
import models._

@Singleton
class PaikkaController @Inject()(
                                  cc: ControllerComponents,
                                  messagesAction: MessagesActionBuilder,
                                  authenticatedAction: AuthenticatedAction
                                ) extends AbstractController(cc)
{
  def paikkaDetail(id:Int) = authenticatedAction.async { implicit request =>
    val userId = request.session.get("user_id").getOrElse("")
    val paikka = PaikkaDao.getById(id)
    val kartalla = KartallaDao.getByUserIdAndPaikkaId(userId,id)

    for {
      paikka <- paikka
      kartalla <- kartalla
    } yield {
      paikka match {
        case Nil => BadRequest(views.html.error.error("404", "ページが見つかりませんでした"))
        case _ => Ok(views.html.paikka.paikka(paikka.head))
      }
    }
  }

  def addMap() = authenticatedAction { implicit request =>
    Ok(views.html.paikka.add())
  }

  def createPaikka = messagesAction.async { implicit request: MessagesRequest[AnyContent] =>
    val result =
      for {
        userId <- request.session.get("user_id")
        json <- request.body.asJson
      } yield {
        createForm.bind(json).fold(
          errors =>
            Future(Ok(errors.errorsAsJson)),
         form =>
           PaikkaDao.createPaikka(form, userId).map(id =>
             Ok(Json.toJson(Map("ok" -> id)))
           )
        )
      }
    result match {
      case Some(result) => result
      case None => Future(BadRequest)
    }
  }

      def addKartalla(id: Int) = authenticatedAction { implicit request =>
    createKartallaForm.bindFromRequest.fold(
      errors =>
        Redirect(s"/paikka/${id}").flashing("errorMessage" -> "エラーが発生しました"),
      form => {
        //　サインインしていればカルタラ作成
        request.session.get("user_id") match {
          case Some(user_id) => KartallaDao.createKartalla(form, id, user_id)
          case None => Redirect("/")
        }
        Redirect(s"/paikka/${id}").flashing("message" -> "カルタラを登録しました")
      }
    )
  }

  def getKartalla =  Action.async { implicit request =>
    val user_id = request.session.get("user_id").getOrElse("")
    // フォローしているユーザーのカルタラを取得
    val results = KartallaDao.getFollowKartalla(user_id)
    results.map { kartalla =>
      val geo = Geocoder.create(ConfigFactory.load().getString("apiKey"))
      val followKartalla : Seq[FollowKartalla] = for(kartalla <- kartalla) yield {
        geo.lookup(kartalla._6).head.geometry.location
        val location = geo.lookup(kartalla._6).head.geometry.location
        FollowKartalla(kartalla._1, kartalla._2, kartalla._3, kartalla._4, kartalla._5, location.latitude, location.longitude)
      }
      implicit val followKartallaFormat = Json.format[FollowKartalla]
      Ok(Json.toJson(followKartalla))
    }
  }

  def getPaikka(id: Int) = Action.async { implicit request =>
    val results = PaikkaDao.getById(id)
    results.map { paikkas =>
      paikkas match {
        case nonEmpty =>
          val paikka = paikkas.head
          val sendPaikka = Paikka(paikka.id, paikka.name, paikka.kana, paikka.tag, paikka.text, paikka.postalCode, paikka.address)
          implicit val paikkaFormat = Json.format[Paikka]
          Ok(Json.toJson(sendPaikka))
        case _ =>  BadRequest(views.html.error.error("500", "内部エラーが発生しました"))
      }
    }
  }

}