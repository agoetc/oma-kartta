package controllers

import javax.inject._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models.UserDao
import scala.concurrent._
import scala.language.postfixOps
import models.Tables._
import slick.jdbc.MySQLProfile.api._
import ExecutionContext.Implicits.global


class UserController @Inject()(cc: ControllerComponents) extends AbstractController(cc){

  val db = Database.forConfig("mysqldb")

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

  def userDetail(id: String) = Action.async { implicit request =>
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

  def mypage() = Action { implicit request =>
    val authId = request.session.get("user_id").getOrElse("")
    Redirect(s"/user/${authId}")
  }


  def index(username: Option[String], userID: Option[String]) = Action.async {
    val users = UserDao.searchUser(username.getOrElse(""), userID.getOrElse(""))
    users.map(user =>
      Ok(views.html.restaurant.userlist(user))
    )
  }

  def userfollow(id:String) = Action { implicit request =>
    val authId = request.session.get("user_id").getOrElse("")
    //すでにフォローしてたらフォローできへんようにするやつ↓
    UserDao.userauth(authId,id).map {auth=>
      auth match {
        case Nil =>
          val query = Relation.map(relation => (relation.followId, relation.followerId)) += (authId,id)
          db.run(query)
        case _ => print("すでにフォローしてる")
      }
    }
    Redirect(s"/user/${id}")
  }


//  def followlist(id: String) = Action { implicit request =>
//    //テーブルRelationにあるログインしている人(ID)の行を探す
//    val followlistID = db.run(Relation.filter(relation => relation.followId === id).result)
//    Await.ready(followlistID, 20 second)
//    //ログインしている人がテーブルUsreに存在しているか探す
//    val user = db.run(Users.filter(user => user.id === id).result)
//    //ログイン
//    val follow = db.run(Relation.filter(relation => relation.followId === authid).result)
////    user.map(user =>
////      authid match {
////        case authid if authid == user.head.id => Ok(views.html.user.followlist(followlistID))
////      }
////    )
//    Redirect("")
//  }


}