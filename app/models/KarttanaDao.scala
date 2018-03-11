package models

import slick.jdbc.MySQLProfile.api._
import models.Tables._

import scala.util.{Failure, Success}
import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global
import com.typesafe.config.ConfigFactory

import scala.concurrent._
import scala.concurrent.duration._


object KarttanaDao {

  lazy val db = Database.forConfig("mysqldb")

  def getByUserId(id: String) = {
    val query = Karttana.filter(karttana => karttana.userId === id).result
    val result = db.run(query)
    try Await.result(result, 20 seconds)
    catch {
      case e: TimeoutException => false
    }
  }

  def getFollowKarttana(user_id: String) = {
    val query = for {
      relation <- Relation if relation.followId === user_id
      karttana <- Karttana if karttana.userId === relation.followerId
      restaurant <- Restaurants if restaurant.id === karttana.restaurantId
    } yield (karttana.userId, karttana.star, karttana.sana, karttana.restaurantId, karttana.createdAt, restaurant.address)
    val results = db.run(query.result)
    results
  }
}