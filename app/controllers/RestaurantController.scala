//package controllers
//
//import javax.inject._
//import play.api.mvc._
//
//
//class RestaurantController extends RestaurantControllerBase with ScalatraServlet with FormSupport with I18nSupport
//
//trait RestaurantControllerBase extends ScalatraServlet with FormSupport with I18nSupport {
//
//  get("/") {
//    val db = Database.forConfig("mysqldb")
//    val area = params.get("ares")
//    val keyword = params.get("keyword")
//    val restaurants = db.run(Restaurants.filter(restaurant => (restaurant.address like "%" + area.getOrElse("") + "%") ||
//      (restaurant.text like "%" + keyword.getOrElse("") + "%")).result).map { restaurants =>
//      oma.kartta.restaurant.html.restaurantlist(restaurants)
//    }
//
//  }
//
//  get("/new") {
//    oma.kartta.restaurant.html.restaurantadd()
//  }
//
//  post("/new") {
//    validate(newForm)(
//      errors => BadRequest(oma.kartta.restaurant.html.restaurantadd()),
//      form => restaurantCreate(form)
//    )
//  }
//
//
//  case class RestaurantNewForm(name: String, kana: String, phone: Int, text: Option[String], postal_code: Int, address: String)
//
//  val newForm = mapping(
//    "name" -> label("name", text(required, maxlength(50))),
//    "kana" -> label("kana", text(required, maxlength(50))),
//    "phone" -> label("phone",number(required, maxlength(11))),
//    "text" -> label("text", optional(text(maxlength(500)))),
//    "postal_code" -> label("postal_code", number(required,maxlength(7))),
//    "address" -> label("address", text(required))
//  )(RestaurantNewForm.apply)
//
//  def restaurantCreate(form: RestaurantNewForm) {
//    val db = Database.forConfig("mysqldb")
//    db.run(Restaurants.map(restaurant => (restaurant.name, restaurant.kana, restaurant.phone, restaurant.text, restaurant.postalCode, restaurant.address))
//      += ((form.name, form.kana, form.phone, form.text, form.postal_code, form.address)))
//    redirect("/restaurant")
//  }
//
//}