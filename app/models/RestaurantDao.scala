package models

import slick.jdbc.MySQLProfile.api._
import models.Tables._

import scala.util.{Failure, Success}
import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global
import com.typesafe.config.ConfigFactory
import controllers.RestaurantController

import scala.concurrent.duration._


object RestaurantDao {

  lazy val db = Database.forConfig("mysqldb")

  def getById(id: Int) = {
    val query = Restaurants.filter(restaurant => restaurant.id === id).result
    db.run(query)
  }

  case class RestaurantNewForm(name: String, kana: String, text: Option[String] = None, postalCode: String, address: String)

  def createRestaurant(form: RestaurantNewForm) = {
    val action = Restaurants returning Restaurants.map(_.id) +=
      RestaurantsRow(0,form.name, form.kana, form.text, form.postalCode, form.address)
    db.run(action)
  }

}
