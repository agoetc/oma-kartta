package controllers


import javax.inject._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models._
import play.api.data.validation.Constraints
import utils.AuthenticatedAction

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.postfixOps
import scala.util.{Failure, Success}

@Singleton
class IndexController @Inject()(
                                 cc: ControllerComponents,
                                 authenticatedAction: AuthenticatedAction,
                                 messagesAction: MessagesActionBuilder,
                               ) extends AbstractController(cc) {

  val newForm = Form(
    mapping(
      "id" -> nonEmptyText(minLength = 4,maxLength = 20)
                                .verifying(
                                  Constraints.pattern("\\w*".r,
                                  error = "形式が不正です。記号は _ が使用できます")),
      "name" -> nonEmptyText(maxLength = 20),
      "password" ->nonEmptyText(minLength = 6,maxLength = 20)
    )(UserDao.UserNewForm.apply)(UserDao.UserNewForm.unapply)
  )

  val authForm = Form(
    mapping(
      "id" -> nonEmptyText,
      "password" -> nonEmptyText
    )(AuthForm.apply)(AuthForm.unapply))

  case class AuthForm(id: String, password: String)

  private def auth(id: String, password: String) = {
    UserDao.auth(id, password).map { auth =>
      auth match {
        case Nil => Redirect("/signin")
        case _ => Redirect("/main").withSession("user_id" -> id)
      }
    }
  }

  def index = Action {
    Ok(views.html.index())
  }

  def signin = Action {
    Ok(views.html.signin())
  }

  def signup = messagesAction { implicit request: MessagesRequest[AnyContent] =>
    Ok(views.html.signup(newForm))
  }

  def checkSignin = Action.async { implicit request =>
    authForm.bindFromRequest().fold(
      errors => Future(BadRequest(views.html.signin())),
      form => this.auth(form.id, form.password)
    )
  }

  def createUser = messagesAction.async { implicit request: MessagesRequest[AnyContent] =>
    newForm.bindFromRequest().fold(
      errors =>
        Future(BadRequest(views.html.signup(errors))),
      form =>
          UserDao.createUser(form).flatMap {
            case Failure(e) => Future(Redirect("/signup").flashing("errorMessage" -> "エラーが発生しました。お手数ですがもう一度入力してください"))
            case Success(_) => this.auth(form.id, form.password)
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
