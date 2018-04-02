package controllers

import javax.inject._

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.libs.json._
import play.api.libs.json.Writes._
import slick.driver.MySQLDriver.api._

import models.Tables._

import scala.concurrent._
import ExecutionContext.Implicits.global

import scala.concurrent.duration._
import scala.language.postfixOps

import java.util.Date
import com.koddi.geocoder.Geocoder
import com.typesafe.config.ConfigFactory
import models._

@Singleton
class RestaurantController @Inject()(cc: ControllerComponents) extends AbstractController(cc){

  case class MapContent(content: String)

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
    )(RestaurantDao.RestaurantNewForm.apply)(RestaurantDao.RestaurantNewForm.unapply)
  )

  case class KarttanaCreate(star:Int, sana: String)

  val karttanaCreateForm = Form(
    mapping(
      "star" -> number,
      "sana" -> nonEmptyText
    )(KarttanaCreate.apply)(KarttanaCreate.unapply)
  )

  case class FollowKarttana(userId: String, star: Int, sana: String, restaurantId: Int, createdAt: Date, lat: Double, lng: Double)
  case class Restaurant(id: Int, name: String, kana: String, text: Option[String] = None, postalCode: String, address: String)


  def restaurantDetail(id:Int) = Action.async{ implicit request =>
    val restaurant = RestaurantDao.getById(id)
    restaurant.map(restaurant =>
      restaurant match {
        case Nil => BadRequest(views.html.error.error("404", "ページが見つかりませんでした"))
        case _ => Ok(views.html.restaurant.restaurant(restaurant.head))
      }
    )
  }

  def addMap() = Action {
    Ok(views.html.restaurant.restaurantadd())
  }


  def contentPost = Action{ implicit request =>
    mapContent.bindFromRequest.fold(
      errors => Ok(views.html.error.error("500", "内部エラーが発生しました")),
      form => {   // geocoderでもらった、郵便番号と住所を取得する
        val postal_code = form.content.split(' ').apply(0).takeRight(8)
        val address = form.content.split(' ').apply(1)
        Ok(views.html.restaurant.restaurantaddform(postal_code, address))
      }
    )
  }


  def createRestaurant = Action.async { implicit request =>
    newForm.bindFromRequest.fold(
      errors =>{
        Future(BadRequest(views.html.error.error("500", "内部エラー")))
      },
      form => {
        // insertしたrestaurantのid取得
        RestaurantDao.createRestaurant(form).map { id =>
          Redirect(s"/restaurant/detail/${id}")
        }
      }
    )
  }

  def karttanaAdd(id: Int) = Action{implicit request =>
    karttanaCreateForm.bindFromRequest.fold(
      errors =>
        Ok(views.html.restaurant.restaurantadd()),
      form => {
        karttanaCreate(form,id,request)
        Redirect(s"/restaurant/detail/${id}")
      }
    )
  }

  def karttanaCreate(form: KarttanaCreate,restaurant_id: Int,request:Request[AnyContent]){
    val user_id = request.session.get("user_id").getOrElse("")
    val db = Database.forConfig("mysqldb")
    db.run(Karttana.map(karttana=> (karttana.userId, karttana.restaurantId, karttana.star, karttana.sana))
      += ((user_id, restaurant_id, form.star, form.sana)))
  }


  def getKarttana =  Action.async { implicit request =>
    val user_id = request.session.get("user_id").getOrElse("")
    // フォローしているユーザーのカルタナを取得
    val results = KarttanaDao.getFollowKarttana(user_id)
    results.map { karttana =>
      val geo = Geocoder.create(ConfigFactory.load().getString("apiKey"))
      val followKarttana : Seq[FollowKarttana] = for(karttana <- karttana) yield {
        val location = geo.lookup(karttana._6).head.geometry.location
        FollowKarttana(karttana._1, karttana._2, karttana._3, karttana._4, karttana._5, location.latitude, location.longitude)
      }
      implicit val followKarttanaFormat = Json.format[FollowKarttana]
      Ok(Json.toJson(followKarttana))
    }
  }

  def getRestaurant(id: Int) = Action.async { implicit request =>
    val results = RestaurantDao.getById(id)
    results.map { restaurants =>
      val restaurant = restaurants.head
      val sendRestaurant = Restaurant(restaurant.id, restaurant.name, restaurant.kana, restaurant.text, restaurant.postalCode, restaurant.address)
      implicit val restaurantFormat = Json.format[Restaurant]
      Ok(Json.toJson(sendRestaurant))
    }
  }


}