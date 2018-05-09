package controllers


import javax.inject._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models._
import utils.AuthenticatedAction

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.postfixOps

@Singleton
class IndexController @Inject()(cc: ControllerComponents ,authenticatedAction: AuthenticatedAction) extends AbstractController(cc) {

  val authForm = Form(
    mapping(
      "id" -> nonEmptyText,
      "password" -> nonEmptyText
    )(AuthForm.apply)(AuthForm.unapply))

  case class AuthForm(id: String, password: String)

  def index = Action {
    Ok(views.html.index())
  }

  def signin = Action {
    Ok(views.html.signin())
  }

  def signup = Action {
    Ok(views.html.signup())
  }

  def checkSignin = Action.async { implicit request =>
    authForm.bindFromRequest().fold(
      errors => Future(BadRequest(views.html.signin())),
      form => {
        UserDao.auth(form.id, form.password).map { auth =>
          auth match {
            case Nil => Redirect("/signin")
            case _ => Redirect("/main").withSession("user_id" -> form.id)
          }
        }
      }
    )
  }

  def signout = authenticatedAction { implicit request =>
    Redirect(routes.IndexController.index()).withNewSession
  }

  def main = authenticatedAction {
    Ok(views.html.main())
  }

}
