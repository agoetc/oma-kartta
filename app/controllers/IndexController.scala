package controllers


import javax.inject._
import play.api.mvc._
import models._
import utils.AuthenticatedAction
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.postfixOps
import scala.util.{Failure, Success}
import forms.UserForm._

@Singleton
class IndexController @Inject()(
                                 cc: ControllerComponents,
                                 authenticatedAction: AuthenticatedAction,
                                 messagesAction: MessagesActionBuilder,
                               ) extends AbstractController(cc) {



  private def auth(id: String, password: String) = {
    UserDao.auth(id, password).map { auth =>
      auth match {
        case Nil =>
          Redirect("/signin")
          .flashing("errorMessage" -> "サインインに失敗しました。もう一度入力してください")
        case _ => Redirect("/main").withSession("user_id" -> id)
      }
    }
  }

  def index = Action {
    Ok(views.html.index())
  }

  def main = authenticatedAction {
    Ok(views.html.main())
  }

  def signin = messagesAction { implicit request: MessagesRequest[AnyContent] =>
    Ok(views.html.signin(signinForm))
  }

  def signup = messagesAction { implicit request: MessagesRequest[AnyContent] =>
    Ok(views.html.signup(createForm))
  }

  def signout = authenticatedAction { implicit request =>
    Redirect(routes.IndexController.index()).withNewSession
  }

  def checkSignin = messagesAction.async { implicit request: MessagesRequest[AnyContent] =>
    signinForm.bindFromRequest().fold(
      errors => Future(Ok(views.html.signin(errors))),
      form => this.auth(form.id, form.password)
    )
  }

  def createUser = messagesAction.async { implicit request: MessagesRequest[AnyContent] =>
    createForm.bindFromRequest().fold(
      errors =>
        Future(BadRequest(views.html.signup(errors))),
      form =>
          UserDao.createUser(form).flatMap {
            case Failure(e) =>
              Future(Redirect("/signup").flashing("errorMessage" -> "エラーが発生しました。お手数ですがもう一度入力してください"))
            case Success(_) => this.auth(form.id, form.password)
          }
    )
  }

}
