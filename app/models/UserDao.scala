package models

import slick.jdbc.MySQLProfile.api._
import models.Tables._
import scala.util.{Failure, Success}
import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global
import com.typesafe.config.ConfigFactory
import scala.concurrent._
import scala.concurrent.duration._



trait UserDao {

  lazy val db = Database.forConfig("mysqldb")

  def getById(id: String) ={
    val query = Users.filter(user => user.id === id).result
    val result = db.run(query)
    try Await.result(result,5 seconds)
    catch { case e: TimeoutException => false }
  }

  def auth(id: String, password: String)={
    val query = Users.filter(user => user.id === id && user.password === password).result
    val result = db.run(query)
    try Await.result(result,5 seconds).nonEmpty
    catch { case e: TimeoutException => false }
  }

}