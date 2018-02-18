package controllers

import javax.inject._

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import slick.driver.MySQLDriver.api._
import models.Tables._
import play.api.libs.json.{JsValue, Json}
import play.api.libs.json.Writes._
import java.util.Date

import com.koddi.geocoder.Geocoder
import com.typesafe.config.ConfigFactory
import models._

import scala.concurrent._
import scala.concurrent.duration._
import scala.language.postfixOps


@Singleton
class IndexController @Inject()(cc: ControllerComponents) extends AbstractController(cc) with UserDao{

  def index = Action{
    Ok(views.html.index())
  }

  def signin = Action{
    Ok(views.html.signin())
  }

  def signup = Action{
    Ok(views.html.signup())
  }

  case class AuthForm(id: String, password: String)

  val authForm = Form(
    mapping(
      "id" -> nonEmptyText,
      "password" -> nonEmptyText
    )(AuthForm.apply)(AuthForm.unapply))


  def signincheck = Action { implicit request =>
    authForm.bindFromRequest().fold(
      errors => Ok(views.html.signin()),
      form =>{
        auth(form.id,form.password) match{
          case true => Redirect("/main").withSession ("user_id" -> form.id)
          case false => Redirect("/signin")
        }
      }
    )
  }

  def signout = Action{ implicit request =>
    Redirect(routes.IndexController.index()).withNewSession
  }


  case class FollowKarttana(userId: String, star: Int, sana: String, restaurantId: Int, createdAt: Date, lat: Double, lng: Double)

  def main =  Action{ implicit request =>
    val user_id = request.session.get("user_id").getOrElse("")
    // フォローしているユーザーのカルタナを取得
    val results = KarttanaDao.getFollowKarttana(user_id)

    // レストランの住所をgeocodingしてcase classに入れる
    val geo = Geocoder.create(ConfigFactory.load().getString("apiKey"))
    val followKarttana:Seq[FollowKarttana] = for(karttana <- results) yield {
      val location = geo.lookup(karttana._6).head.geometry.location
      FollowKarttana(karttana._1, karttana._2, karttana._3, karttana._4, karttana._5, location.latitude, location.longitude)
    }

    implicit val followKarttanaFormat = Json.format[FollowKarttana]
    val resultJson = Json.toJson(followKarttana)

    Ok(views.html.main())
  }


}
