package controllers

import javax.inject._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import slick.driver.MySQLDriver.api._
import models.Tables._



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
}