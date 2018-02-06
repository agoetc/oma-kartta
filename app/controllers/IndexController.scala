package controllers

import javax.inject._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import slick.driver.MySQLDriver.api._
import models.Tables._
import scala.util.{ Success, Failure }
import scala.concurrent._
import ExecutionContext.Implicits.global

@Singleton
class IndexController @Inject()(cc: ControllerComponents) extends AbstractController(cc){

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

  def userCheck(form: AuthForm,request: Request[AnyContent]) {
    val db = Database.forConfig("mysqldb")
    val user = db.run(Users.filter(user => user.id === form.id && user.password === form.password).result)

    user.onComplete {
      case Success(r) =>
      case Failure(t) => Ok(views.html.signin(t))
    }
  }

  def signincheck = Action { implicit request =>
    authForm.bindFromRequest().fold(
      errors => Ok(views.html.signin()),
      form =>{
        userCheck(form,request)
        Redirect("/main").withSession("user_id" -> form.id)
      }
    )
  }

  def signout = Action{ implicit request =>
    Redirect(routes.IndexController.index()).withNewSession
  }

  def main =  Action{ implicit request =>
    Ok(views.html.main())
  }
}
