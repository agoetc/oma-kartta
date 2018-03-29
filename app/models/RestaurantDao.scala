package models

import slick.jdbc.MySQLProfile.api._
import models.Tables._

import scala.util.{Failure, Success}
import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global
import com.typesafe.config.ConfigFactory

import scala.concurrent._
import scala.concurrent.duration._


object RestaurantDao {

  lazy val db = Database.forConfig("mysqldb")

  def getById(id: Int) = {
    val query = Restaurants.filter(restaurant => restaurant.id === id).result
    db.run(query)
  }

}
