package models

import slick.jdbc.MySQLProfile.api._
import models.Tables._
import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global


object KarttanaDao {

  val db = Database.forConfig("mysqldb")

  def getByUserId(id: String) = {
    val query = Karttana.filter(karttana => karttana.userId === id).result
    db.run(query)
  }

  def getByUserIdAndRestaurantId(userId: String, restaurantId: Int) = {
    val query = Karttana.filter(karttana => karttana.userId === userId && karttana.restaurantId === restaurantId).result
    db.run(query)
  }

  def getFollowKarttana(user_id: String) = {
    val query = for {
      relation <- Relation if relation.followId === user_id
      karttana <- Karttana if karttana.userId === relation.followerId
      restaurant <- Restaurants if restaurant.id === karttana.restaurantId
    } yield (karttana.userId, karttana.star, karttana.sana, karttana.restaurantId, karttana.createdAt, restaurant.address)
    db.run(query.result)
  }

  case class CreateKarttana(star:Int, sana: String)

  def createKarttana(form: CreateKarttana ,restaurant_id: Int, user_id: String) = {
    val query = Karttana.map(karttana =>
      (karttana.userId, karttana.restaurantId, karttana.star, karttana.sana)) += ((user_id, restaurant_id, form.star, form.sana))
    db.run(query)
  }
}