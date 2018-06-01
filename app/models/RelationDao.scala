package models

import slick.jdbc.MySQLProfile.api._
import models.Tables._
import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global



object RelationDao {

  val db = Database.forConfig("mysqldb")


  /*
   * フォローしているかの確認
   */
  def isFollow(authId: String, id: String) = {
    val query = Relation.filter(relation =>
      relation.followId === authId && relation.followerId === id).result
    db.run(query)
  }

  def addFollow(authId: String, id: String)= {
    val query = Relation.map(relation => (relation.followId, relation.followerId)) += (authId,id)
    db.run(query)
  }

  def getFollows(id: String)= {
    val query = Relation.filter(relation => relation.followId === id).result
    db.run(query)
  }

  def getFollowers(id: String)= {
    val query = Relation.filter(relation => relation.followerId === id).result
    db.run(query)
  }

  /*
   * フォローしているユーザーの情報を返す
   */
  def getFollowDetails(id: String)= {
    val query =
      (for (
      relation <- Relation;
      users <- Users if relation.followerId === users.id && relation.followId === id
    ) yield (users)).result
    db.run(query)
  }

  /*
   * フォロワーの情報を返す
   */
  def getFollowerDetails(id: String)= {
    val query =
      (for (
        relation <- Relation;
        users <- Users if relation.followId === users.id && relation.followerId === id
      ) yield (users)).result
    db.run(query)
  }

}
