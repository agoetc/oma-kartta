package controllers

import javax.inject._

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import slick.driver.MySQLDriver.api._
import models.Tables._
import play.api.libs.json._

import scala.util.{Failure, Success}
import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.concurrent.duration._
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

  // 入力されたuserを検索、存在すればtrueを返す
  def userCheck(form: AuthForm,request: Request[AnyContent]):Boolean ={
    val db = Database.forConfig("mysqldb")
    val user = db.run(Users.filter(user => user.id === form.id && user.password === form.password).result)
    Await.ready(user, 20 second)
    user.value.get.get.nonEmpty
  }

  def signincheck = Action { implicit request =>
    authForm.bindFromRequest().fold(
      errors => Ok(views.html.signin()),
      form =>{
        userCheck(form,request) match {
          case true => Redirect("/main").withSession ("user_id" -> form.id)
          case false => Redirect("/signin").flashing("error" -> "")
        }
      }
    )
  }

  def signout = Action{ implicit request =>
    Redirect(routes.IndexController.index()).withNewSession
  }

  def main =  Action{ implicit request =>
    val db = Database.forConfig("mysqldb")
    val user_id = request.session.get("user_id")
    // フォローしているユーザーのカルタナを検索するクエリ
    val query = for {
      relation <- Relation if relation.followId === user_id
      karttana <- Karttana if karttana.userId === relation.followerId
    } yield (karttana)
    val results = db.run(query.result)
    Await.result(results, 20 seconds)

    
    Ok(views.html.main())
  }
}
