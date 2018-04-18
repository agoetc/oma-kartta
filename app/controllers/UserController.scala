package controllers

import javax.inject._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models.UserDao
import scala.concurrent._
import scala.language.postfixOps
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

    for {
      user <- user
      follow <- follow
      follower <- follower
    } yield {
      authId match {
        case authid if authid == user.head.id =>
          Ok(views.html.user.mypage(user.head, follow.length, follower.length))
        case _ =>
          Ok(views.html.user.user(user.head))
      }
    }
  }


  def index(username: Option[String], userID: Option[String]) = Action.async {
    val users = UserDao.searchUser(username.getOrElse(""), userID.getOrElse(""))
    users.map(user =>
      Ok(views.html.restaurant.userlist(user))
    )
  }
}