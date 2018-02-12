package controllers

import javax.inject._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import slick.driver.MySQLDriver.api._
import models.Tables._
import scala.concurrent._
import scala.concurrent._
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.{Failure, Success}
import ExecutionContext.Implicits.global



class UserController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  case class UserNewForm(id: String, name: String, password: String)

  val newForm = Form(
    mapping(
      "id" -> nonEmptyText,
      "name" -> nonEmptyText,
      "password" -> nonEmptyText
    )(UserNewForm.apply)(UserNewForm.unapply)
  )

  def userCreate(form: UserNewForm) {
    val db = Database.forConfig("mysqldb")
    db.run(Users.map(user => (user.id, user.name, user.password)) += (form.id, form.name, form.password))
  }

  def register = Action { implicit request =>
    newForm.bindFromRequest().fold(
      errors => Ok(views.html.signup()),
      form => {
        userCreate(form)
        Redirect("/main")
      }
    )
  }

  def userDetail(id: String) = Action.async { implicit request =>
    val db = Database.forConfig("mysqldb")
    val user = db.run(Users.filter(user => user.id === id).result)
    val authid = request.session.get("user_id").getOrElse("")
    val follow = db.run(Relation.filter(relation => relation.followId === authid).result)
    val follower = db.run(Relation.filter(relation => relation.followId === authid).result)
    Await.ready(follow, 20 second)
    Await.ready(follower, 20 second)
    val followLen = follow.value.get.get.length
    val followerLen = follower.value.get.get.length
    println(followLen, followerLen)
    user.map(user =>
      authid match {
        case authid if authid == user.head.id => Ok(views.html.user.mypage(user.head, followLen, followerLen))
        case _ => Ok(views.html.user.user(user.head))
      }
    )
  }


  def index(username: Option[String], userID: Option[String]) = Action.async {
    val db = Database.forConfig("mysqldb")
    val users = db.run(Users.filter(user => (user.name like "%" + username.getOrElse("") + "%") &&
      (user.id like "%" + userID.getOrElse("") + "%")).result)
    users.map(user => Ok(views.html.restaurant.userlist(user)))
  }


  def followProcessing(id: String) = Action { implicit request =>
    val db = Database.forConfig("mysqldb")
    val followproID = request.session.get("user_id").getOrElse("")
    db.run(Relation.map(rel => (rel.followId, rel.followerId)) += (followproID, id))
    Redirect(s"/user/detail/${id}")
  }


}
