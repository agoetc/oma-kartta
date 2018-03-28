package controllers


import javax.inject._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models._
import scala.language.postfixOps


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


  def signincheck = Action { implicit request =>
    authForm.bindFromRequest().fold(
      errors => Ok(views.html.signin()),
      form =>{
        UserDao.auth(form.id,form.password) match{
          case true => Redirect("/main").withSession ("user_id" -> form.id)
          case false => Redirect("/signin")
        }
      }
    )
  }

  def signout = Action{ implicit request =>
    Redirect(routes.IndexController.index()).withNewSession
  }

  def main = Action {
    Ok(views.html.main())
  }



}
