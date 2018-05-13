package models

import slick.jdbc.MySQLProfile.api._
import models.Tables._
import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global



object UserDao {

  val db = Database.forConfig("mysqldb")

  def getById(id: String) = {
    val query = Users.filter(user => user.id === id).result
    db.run(query)
  }

  def auth(id: String, password: String) = {
    val query = Users.filter(user => user.id === id && user.password === password).result
    db.run(query)
  }

  def createUser(form: forms.UserForm.CreateForm) = {
    val query = (Users.map(user => (user.id, user.name, user.password))
      += (form.id, form.name, form.password)).asTry
    db.run(query)
  }

  def getFollowByUserId(id: String) = {
    val query = Relation.filter(relation => relation.followId === id).result
    db.run(query)
  }

  def getFollowerByUserId(id: String) = {
    val query = Relation.filter(relation => relation.followerId === id).result
    db.run(query)
  }

  def searchUser(userName: String, userId: String) = {
    val query = Users.filter(user => (user.name like "%" + userName + "%") &&
      (user.id like "%" + userId + "%")).result
    db.run(query)
  }

}