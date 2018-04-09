package controllers

import javax.inject._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import slick.driver.MySQLDriver.api._
import models.Tables._
import models.UserDao
import scala.concurrent._
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.{Failure, Success}
import ExecutionContext.Implicits.global



class UserController @Inject()(cc: ControllerComponents) extends AbstractController(cc){

    val newForm = Form(
      mapping(
        "id" -> nonEmptyText,
        "name" -> nonEmptyText,
        "password" ->nonEmptyText
      )(UserDao.UserNewForm.apply)(UserDao.UserNewForm.unapply)
    )


  def createUser = Action{ implicit request =>
    newForm.bindFromRequest().fold(
      errors => BadRequest(views.html.signup()),
      form => {
        UserDao.createUser(form)
        Redirect("/main")
      }
    )
  }

  def userDetail(id: String) = Action.async{ implicit request =>
    val user = UserDao.getById(id)
    val authId = request.session.get("user_id").getOrElse("")
    val follow = UserDao.getFollowByUserId(authId)
    val follower = UserDao.getFollowerByUserId(authId)

    user.map(user =>
      follow.map(follow =>
        follower.map(follower =>
          authId match {
            case authid if authid == user.head.id =>
              Ok(views.html.user.mypage(user.head, follow.length, follower.length))
            case _ => Ok(views.html.user.user(user.head))
          }
        )
      )
    ).flatten.flatten

  }



  def index(username: Option[String], userID: Option[String]) = Action.async{
    val db = Database.forConfig("mysqldb")
    val users = db.run(Users.filter(user => (user.name like "%" + username.getOrElse("") + "%") &&
      (user.id like "%" + userID.getOrElse("") + "%")).result)
    users.map(user => Ok(views.html.restaurant.userlist(user)))
  }
}