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

  case class AuthForm(id: String, password: String)

  val authForm = Form(
    mapping(
    "id" -> text,
    "password" -> text
  )(AuthForm.apply)(AuthForm.unapply))

  def userCheck(form: AuthForm) {

    //    val db = Database.forConfig("mysqldb")
    //    val user = db.run(Users.filter(user => user.id === form.id && user.password === form.password).result)
    //
    //    user.onComplete {
    //      case Success(r) => println(r)
    //      case Failure(t) => println(t.getMessage)
    //    }
  }

  def signin = Action{
//    authForm.bindFromRequest().fold(
//      errors => Ok(views.html.signin()),
//      form =>{
//        userCheck(form)
//        Ok(views.html.main())
//      }
//    )
    Ok(views.html.main())
  }

  def userDetail(id: String) = Action.async{
    val db = Database.forConfig("mysqldb")
    val user = db.run(Users.filter(user => user.id === id).result)
    user.map(user => Ok(views.html.user.user(user.head)))
  }

}