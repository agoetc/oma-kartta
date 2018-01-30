package controllers

import javax.inject._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import slick.driver.MySQLDriver.api._
import models.Tables._

@Singleton
class RestaurantController @Inject()(cc: ControllerComponents) extends AbstractController(cc){

  def index = Action {
//    val db = Database.forConfig("mysqldb")
//    val area = params.get("ares")
//    val keyword = params.get("keyword")
//    val restaurants = db.run(Restaurants.filter(restaurant => (restaurant.address like "%" + area.getOrElse("") + "%") ||
//      (restaurant.text like "%" + keyword.getOrElse("") + "%")).result)
    Ok(views.html.restaurant.restaurantlist())
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
    db.run(Restaurants.map(restaurant => (restaurant.name, restaurant.kana, restaurant.phone, restaurant.text, restaurant.postalCode, restaurant.address))
      += ((form.name, form.kana, form.phone, form.text, form.postal_code, form.address)))
  }

}