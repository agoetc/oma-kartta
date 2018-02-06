package controllers

import javax.inject._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import slick.driver.MySQLDriver.api._
import models.Tables._
import scala.concurrent._
import ExecutionContext.Implicits.global

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

  def add = Action{
  Ok(views.html.restaurant.restaurantadd())
  }

  case class RestaurantNewForm(name: String, kana: String, phone: Int, text: Option[String], postal_code: Int, address: String)

  val newForm = Form(
    mapping(
     "name" -> text,
     "kana" -> text,
     "phone" -> number,
      "text" -> optional(text),
     "postal_code" -> number,
      "address" -> text
    )(RestaurantNewForm.apply)(RestaurantNewForm.unapply)
  )

  def register = Action{implicit request =>
    newForm.bindFromRequest.fold(
      errors => Ok(views.html.restaurant.restaurantadd()),
      form => {
        restaurantCreate(form)
        Redirect("/restaurant")
      }
    )
  }


  def restaurantCreate(form: RestaurantNewForm) {
    val db = Database.forConfig("mysqldb")
//    db.run(Restaurants.map(restaurant => (restaurant.name, restaurant.kana, restaurant.phone, restaurant.text, restaurant.postalCode, restaurant.address))
//      += ((form.name, form.kana, form.phone, form.text, form.postal_code, form.address)))
  }



  def karttanaAdd(id: Int) = Action{implicit request =>

    karttanaCreateForm.bindFromRequest.fold(
      errors =>
        Ok(views.html.restaurant.restaurantadd()),
      form => {
        karttanaCreate(form,id,request)
        Redirect("/restaurant")
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
    val user_id = request.session.get("user_id")getOrElse(Redirect("/signin")).toString()
    val db = Database.forConfig("mysqldb")
    db.run(Karttana.map(karttana=> (karttana.userId, karttana.restaurantId, karttana.star, karttana.sana))
      += ((user_id, restaurant_id, form.star, form.sana)))
  }
}