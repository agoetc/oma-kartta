package models

import slick.jdbc.MySQLProfile.api._
import models.Tables._
import forms.PaikkaForm.CreatePaikkaForm
import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global


object PaikkaDao {

  val db = Database.forConfig("mysqldb")

  def getById(id: Int) = {
    val query = Paikka.filter(paikka => paikka.id === id).result
    db.run(query)
  }


  def createPaikka(form: CreatePaikkaForm, userId: String) = {
    val query = Paikka returning Paikka.map(_.id) +=
      PaikkaRow(0, userId, form.name, form.kana, form.tag, form.text, form.postalCode, form.address)
    db.run(query)
  }

}
