package controllers

import javax.inject._

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import slick.driver.MySQLDriver.api._
import models.Tables._

import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.util.{Failure, Success}

import scala.concurrent.duration._
import scala.language.postfixOps

@Singleton
class RestaurantController @Inject()(cc: ControllerComponents) extends AbstractController(cc){

//  def index(area: Option[String], keyword: Option[String]) = Action.async{
//    val db = Database.forConfig("mysqldb")
//    val restaurants = db.run(Restaurants.filter(restaurant => (restaurant.address like "%" + area.getOrElse("") + "%") &&
//      (restaurant.text like "%" + keyword.getOrElse("") + "%")).result)
//    restaurants.map(restaurant => Ok(views.html.restaurant.restaurantlist(restaurant)))
//
//  }

  def restaurantDetail(id:Int) = Action.async{
    val db = Database.forConfig("mysqldb")
    val restaurant = db.run(Restaurants.filter(restaurant => restaurant.id === id).result)
    restaurant.map(restaurant => Ok(views.html.restaurant.restaurant(restaurant.head)))
  }

  def addMap() = Action{
    Ok(views.html.restaurant.restaurantadd())
  }

  case class MapContent(content: String)

  val mapContent = Form(
    mapping(
      "content" -> nonEmptyText
    )(MapContent.apply)(MapContent.unapply)
  )

  def contentPost = Action{ implicit request =>
    mapContent.bindFromRequest.fold(
      errors => Ok(views.html.main()),
      form => {   // geocoderでもらった、郵便番号と住所を取得する
        val postal_code = form.content.split(' ').apply(0).takeRight(8)
        val address = form.content.split(' ').apply(1)
        Redirect("/restaurant/new").withSession(request.session + ("postal_code" -> postal_code) + ("address" -> address))
      }
    )
  }

  def restaurantAdd = Action{implicit request =>
    //セッションが存在するとき
    val posbool = request.session.get("postal_code").isEmpty
    val addressbool = request.session.get("address").isEmpty
    (posbool,addressbool) match {
      case (false,false) => Ok(views.html.restaurant.restaurantaddform())
      case _ => Redirect("/main")
    }
  }

  case class RestaurantNewForm(name: String, kana: String,text: Option[String])

  val newForm = Form(
    mapping(
      "name" -> nonEmptyText,
      "kana" -> nonEmptyText,
      "text" -> optional(text),
    )(RestaurantNewForm.apply)(RestaurantNewForm.unapply)
  )

  def register = Action{implicit request =>
    newForm.bindFromRequest.fold(
      errors => Ok(views.html.restaurant.restaurantadd()),
      form => {
        // insertしたrestaurantのid取得
        val futureId :Future[Int] = restaurantCreate(form, request)
        // futureで値を取得できたらリダイレクト
        Await.ready(futureId, 20 second)

        if(futureId.isCompleted){
          val id = futureId.value.get.get
          Redirect(s"/restaurant/detail/${id}").withSession(request.session - "postal_code").withSession(request.session -"address")
        }else{
          Redirect("/main")

        }

      }
    )
  }



  def restaurantCreate(form: RestaurantNewForm, request: Request[AnyContent]) :Future[Int] ={
    val db = Database.forConfig("mysqldb")
    val id: Future[Int] = (request.session.get("postal_code"), request.session.get("address")) match{
        // セッションにpostal_codeとaddressが存在するとき
      case (Some(postal_code),Some(address)) =>
        val action = Restaurants returning Restaurants.map(_.id) +=
          RestaurantsRow(0,form.name, form.kana, form.text, postal_code, address)
        db.run(action)
          //　セッションがないときの処理
//      case (_,_) =>
    }

    return id
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

  case class KarttanaCreate(star:Int, sana: String)
  val karttanaCreateForm = Form(
    mapping(
      "star" -> number,
      "sana" -> nonEmptyText
    )(KarttanaCreate.apply)(KarttanaCreate.unapply)
  )

  def karttanaCreate(form: KarttanaCreate,restaurant_id: Int,request:Request[AnyContent]){
    val user_id = request.session.get("user_id").getOrElse("")
    val db = Database.forConfig("mysqldb")
    db.run(Karttana.map(karttana=> (karttana.userId, karttana.restaurantId, karttana.star, karttana.sana))
      += ((user_id, restaurant_id, form.star, form.sana)))
  }
}