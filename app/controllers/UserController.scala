package controllers

import javax.inject._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import slick.driver.MySQLDriver.api._
import models.Tables._
import scala.concurrent._
import ExecutionContext.Implicits.global



class UserController @Inject()(cc: ControllerComponents) extends AbstractController(cc){

  case class UserNewForm(id: String, name: String, password: String)

    val newForm = Form(
      mapping(
        "id" -> nonEmptyText,
        "name" -> nonEmptyText,
        "password" ->nonEmptyText
      )(UserNewForm.apply)(UserNewForm.unapply)
    )

  def userCreate(form: UserNewForm) {
    val db = Database.forConfig("mysqldb")
    db.run(Users.map(user => (user.id, user.name, user.password)) += (form.id, form.name, form.password))
  }

  def register = Action{ implicit request =>
    newForm.bindFromRequest().fold(
      errors => Ok(views.html.signup()),
      form => {
        userCreate(form)
        Redirect("/main")
      }
    )
  }

  def userDetail(id: String) = Action.async{
    val db = Database.forConfig("mysqldb")
    val user = db.run(Users.filter(user => user.id === id).result)
    user.map(user => Ok(views.html.user.user(user.head)))
  }



  def index(username: Option[String], userID: Option[String]) = Action.async{
    val db = Database.forConfig("mysqldb")
    val users = db.run(Users.filter(user => (user.name like "%" + username.getOrElse("") + "%") &&
      (user.id like "%" + userID.getOrElse("") + "%")).result)
    users.map(user => Ok(views.html.restaurant.restaurantlist(user)))
  }
}