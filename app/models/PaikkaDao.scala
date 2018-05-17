package models

import slick.jdbc.MySQLProfile.api._
import models.Tables._
import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global


object PaikkaDao {

  val db = Database.forConfig("mysqldb")

  def getById(id: Int) = {
    val query = Paikka.filter(paikka => paikka.id === id).result
    db.run(query)
  }

  case class PaikkaNewForm(name: String, kana: String, text: Option[String] = None, postalCode: String, address: String)

  def createPaikka(form: PaikkaNewForm) = {
    val query = Paikka returning Paikka.map(_.id) +=
      PaikkaRow(0,form.name, form.kana, form.text, form.postalCode, form.address)
    db.run(query)
  }

}
