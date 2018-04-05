package models

import slick.jdbc.MySQLProfile.api._
import models.Tables._
import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global
import com.typesafe.config.ConfigFactory
import scala.concurrent._



object UserDao {

  lazy val db = Database.forConfig("mysqldb")

  def getById(id: String) ={
    val query = Users.filter(user => user.id === id).result
    db.run(query)
  }

  def auth(id: String, password: String) = {
    val query = Users.filter(user => user.id === id && user.password === password).result
    db.run(query)
  }

  case class UserNewForm(id: String, name: String, password: String)

  def createUser(form: UserNewForm) = {
    val query = Users.map(user => (user.id, user.name, user.password)) += (form.id, form.name, form.password)
    db.run(query)
  }
}