package controllers

import javax.inject._
import play.api.mvc._
import models.{UserDao, RelationDao}
import utils.AuthenticatedAction
import scala.concurrent._
import scala.language.postfixOps
import ExecutionContext.Implicits.global


class UserController @Inject()(cc: ControllerComponents, authenticatedAction: AuthenticatedAction) extends AbstractController(cc){


  def userDetail(id: String) = authenticatedAction.async { implicit request =>
    val user = UserDao.getById(id)
    val authId = request.session.get("user_id").getOrElse("")
    val follow = UserDao.getFollowByUserId(id)
    val follower = UserDao.getFollowerByUserId(id)

    for {
      user <- user
      follow <- follow
      follower <- follower
    } yield {
      user match {
        case user if user.nonEmpty =>
          authId match {
            case authid if authid == user.head.id =>
              Ok(views.html.user.mypage(user.head, follow.length, follower.length))
            case _ =>
              Ok(views.html.user.user(user.head, follow.length, follower.length))
          }
        case _ =>
          BadRequest(views.html.error.error("404", "ページが見つかりませんでした"))
      }
    }
  }

  def mypage() = authenticatedAction { implicit request =>
    val authId = request.session.get("user_id").getOrElse("")
    Redirect(s"/user/${authId}")
  }


  def index(username: Option[String], userID: Option[String]) = authenticatedAction.async {
    val users = UserDao.searchUser(username.getOrElse(""), userID.getOrElse(""))
    users.map(user =>
      Ok(views.html.paikka.userlist(user))
    )
  }

  def followUser(id: String) = Action.async { implicit request =>
    val authId = request.session.get("user_id").getOrElse("")
    RelationDao.isFollow(authId,id).map {auth =>
      auth match {
        case Nil =>
          RelationDao.addFollow(authId,id)
          Redirect(s"/user/${id}")
        case _ => Redirect(s"/user/${id}")
      }
    }
  }

  def followlist(id: String) = Action.async {
    val follows = RelationDao.getFollowDetails(id)
    follows.map { follow =>
      Ok(views.html.user.followlist(follow))
    }
  }

  def followerlist(id: String) = Action.async {
    val followers = RelationDao.getFollowerDetails(id)
    followers.map { follower =>
      Ok(views.html.user.followerlist(follower))
    }
  }

}