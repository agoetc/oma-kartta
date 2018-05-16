package models

import slick.jdbc.MySQLProfile.api._
import models.Tables._
import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global


object KartallaDao {

  val db = Database.forConfig("mysqldb")

  def getByUserId(id: String) = {
    val query = Kartalla.filter(kartalla => kartalla.userId === id).result
    db.run(query)
  }

  def getByUserIdAndPaikkaId(userId: String, paikkaId: Int) = {
    val query = Kartalla.filter(kartalla => kartalla.userId === userId && kartalla.paikkaId === paikkaId).result
    db.run(query)
  }

  def getFollowKartalla(user_id: String) = {
    val query = for {
      relation <- Relation if relation.followId === user_id
      kartalla <- Kartalla if kartalla.userId === relation.followerId
      paikka <- Paikka if paikka.id === kartalla.paikkaId
    } yield (kartalla.userId, kartalla.star, kartalla.sana, kartalla.paikkaId, kartalla.createdAt, paikka.address)
    db.run(query.result)
  }

  case class CreateKartalla(star:Int, sana: String)

  def createKartalla(form: CreateKartalla ,paikka_id: Int, user_id: String) = {
    val query = Kartalla.map(kartalla =>
      (kartalla.userId, kartalla.paikkaId, kartalla.star, kartalla.sana)) += ((user_id, paikka_id, form.star, form.sana))
    db.run(query)
  }
}