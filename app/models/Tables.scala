package models
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.jdbc.MySQLProfile
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Karttana.schema ++ Relation.schema ++ Restaurants.schema ++ UserReviews.schema ++ Users.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Karttana
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param restaurantId Database column restaurant_id SqlType(INT)
   *  @param userId Database column user_id SqlType(VARCHAR), Length(255,true)
   *  @param star Database column star SqlType(INT)
   *  @param sana Database column sana SqlType(VARCHAR), Length(255,true)
   *  @param createdAt Database column created_at SqlType(TIMESTAMP)
   *  @param updatedAt Database column updated_at SqlType(TIMESTAMP) */
  case class KarttanaRow(id: Int, restaurantId: Int, userId: String, star: Int, sana: String, createdAt: java.sql.Timestamp, updatedAt: java.sql.Timestamp)
  /** GetResult implicit for fetching KarttanaRow objects using plain SQL queries */
  implicit def GetResultKarttanaRow(implicit e0: GR[Int], e1: GR[String], e2: GR[java.sql.Timestamp]): GR[KarttanaRow] = GR{
    prs => import prs._
    KarttanaRow.tupled((<<[Int], <<[Int], <<[String], <<[Int], <<[String], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }
  /** Table description of table karttana. Objects of this class serve as prototypes for rows in queries. */
  class Karttana(_tableTag: Tag) extends profile.api.Table[KarttanaRow](_tableTag, Some("omakartta"), "karttana") {
    def * = (id, restaurantId, userId, star, sana, createdAt, updatedAt) <> (KarttanaRow.tupled, KarttanaRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(restaurantId), Rep.Some(userId), Rep.Some(star), Rep.Some(sana), Rep.Some(createdAt), Rep.Some(updatedAt)).shaped.<>({r=>import r._; _1.map(_=> KarttanaRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column restaurant_id SqlType(INT) */
    val restaurantId: Rep[Int] = column[Int]("restaurant_id")
    /** Database column user_id SqlType(VARCHAR), Length(255,true) */
    val userId: Rep[String] = column[String]("user_id", O.Length(255,varying=true))
    /** Database column star SqlType(INT) */
    val star: Rep[Int] = column[Int]("star")
    /** Database column sana SqlType(VARCHAR), Length(255,true) */
    val sana: Rep[String] = column[String]("sana", O.Length(255,varying=true))
    /** Database column created_at SqlType(TIMESTAMP) */
    val createdAt: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("created_at")
    /** Database column updated_at SqlType(TIMESTAMP) */
    val updatedAt: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("updated_at")
  }
  /** Collection-like TableQuery object for table Karttana */
  lazy val Karttana = new TableQuery(tag => new Karttana(tag))

  /** Entity class storing rows of table Relation
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param followId Database column follow_id SqlType(VARCHAR), Length(255,true)
   *  @param followerId Database column follower_id SqlType(VARCHAR), Length(255,true) */
  case class RelationRow(id: Int, followId: String, followerId: String)
  /** GetResult implicit for fetching RelationRow objects using plain SQL queries */
  implicit def GetResultRelationRow(implicit e0: GR[Int], e1: GR[String]): GR[RelationRow] = GR{
    prs => import prs._
    RelationRow.tupled((<<[Int], <<[String], <<[String]))
  }
  /** Table description of table relation. Objects of this class serve as prototypes for rows in queries. */
  class Relation(_tableTag: Tag) extends profile.api.Table[RelationRow](_tableTag, Some("omakartta"), "relation") {
    def * = (id, followId, followerId) <> (RelationRow.tupled, RelationRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(followId), Rep.Some(followerId)).shaped.<>({r=>import r._; _1.map(_=> RelationRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column follow_id SqlType(VARCHAR), Length(255,true) */
    val followId: Rep[String] = column[String]("follow_id", O.Length(255,varying=true))
    /** Database column follower_id SqlType(VARCHAR), Length(255,true) */
    val followerId: Rep[String] = column[String]("follower_id", O.Length(255,varying=true))
  }
  /** Collection-like TableQuery object for table Relation */
  lazy val Relation = new TableQuery(tag => new Relation(tag))

  /** Entity class storing rows of table Restaurants
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param name Database column name SqlType(VARCHAR), Length(255,true)
   *  @param kana Database column kana SqlType(VARCHAR), Length(255,true)
   *  @param text Database column text SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param postalCode Database column postal_code SqlType(VARCHAR), Length(8,true)
   *  @param address Database column address SqlType(VARCHAR), Length(255,true) */
  case class RestaurantsRow(id: Int, name: String, kana: String, text: Option[String] = None, postalCode: String, address: String)
  /** GetResult implicit for fetching RestaurantsRow objects using plain SQL queries */
  implicit def GetResultRestaurantsRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[String]]): GR[RestaurantsRow] = GR{
    prs => import prs._
    RestaurantsRow.tupled((<<[Int], <<[String], <<[String], <<?[String], <<[String], <<[String]))
  }
  /** Table description of table restaurants. Objects of this class serve as prototypes for rows in queries. */
  class Restaurants(_tableTag: Tag) extends profile.api.Table[RestaurantsRow](_tableTag, Some("omakartta"), "restaurants") {
    def * = (id, name, kana, text, postalCode, address) <> (RestaurantsRow.tupled, RestaurantsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), Rep.Some(kana), text, Rep.Some(postalCode), Rep.Some(address)).shaped.<>({r=>import r._; _1.map(_=> RestaurantsRow.tupled((_1.get, _2.get, _3.get, _4, _5.get, _6.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(VARCHAR), Length(255,true) */
    val name: Rep[String] = column[String]("name", O.Length(255,varying=true))
    /** Database column kana SqlType(VARCHAR), Length(255,true) */
    val kana: Rep[String] = column[String]("kana", O.Length(255,varying=true))
    /** Database column text SqlType(VARCHAR), Length(255,true), Default(None) */
    val text: Rep[Option[String]] = column[Option[String]]("text", O.Length(255,varying=true), O.Default(None))
    /** Database column postal_code SqlType(VARCHAR), Length(8,true) */
    val postalCode: Rep[String] = column[String]("postal_code", O.Length(8,varying=true))
    /** Database column address SqlType(VARCHAR), Length(255,true) */
    val address: Rep[String] = column[String]("address", O.Length(255,varying=true))
  }
  /** Collection-like TableQuery object for table Restaurants */
  lazy val Restaurants = new TableQuery(tag => new Restaurants(tag))

  /** Entity class storing rows of table UserReviews
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param userId Database column user_id SqlType(VARCHAR), Length(255,true)
   *  @param review Database column review SqlType(VARCHAR), Length(255,true) */
  case class UserReviewsRow(id: Int, userId: String, review: String)
  /** GetResult implicit for fetching UserReviewsRow objects using plain SQL queries */
  implicit def GetResultUserReviewsRow(implicit e0: GR[Int], e1: GR[String]): GR[UserReviewsRow] = GR{
    prs => import prs._
    UserReviewsRow.tupled((<<[Int], <<[String], <<[String]))
  }
  /** Table description of table user_reviews. Objects of this class serve as prototypes for rows in queries. */
  class UserReviews(_tableTag: Tag) extends profile.api.Table[UserReviewsRow](_tableTag, Some("omakartta"), "user_reviews") {
    def * = (id, userId, review) <> (UserReviewsRow.tupled, UserReviewsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(userId), Rep.Some(review)).shaped.<>({r=>import r._; _1.map(_=> UserReviewsRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column user_id SqlType(VARCHAR), Length(255,true) */
    val userId: Rep[String] = column[String]("user_id", O.Length(255,varying=true))
    /** Database column review SqlType(VARCHAR), Length(255,true) */
    val review: Rep[String] = column[String]("review", O.Length(255,varying=true))
  }
  /** Collection-like TableQuery object for table UserReviews */
  lazy val UserReviews = new TableQuery(tag => new UserReviews(tag))

  /** Entity class storing rows of table Users
   *  @param id Database column id SqlType(VARCHAR), PrimaryKey, Length(255,true)
   *  @param name Database column name SqlType(VARCHAR), Length(255,true)
   *  @param password Database column password SqlType(VARCHAR), Length(255,true) */
  case class UsersRow(id: String, name: String, password: String)
  /** GetResult implicit for fetching UsersRow objects using plain SQL queries */
  implicit def GetResultUsersRow(implicit e0: GR[String]): GR[UsersRow] = GR{
    prs => import prs._
    UsersRow.tupled((<<[String], <<[String], <<[String]))
  }
  /** Table description of table users. Objects of this class serve as prototypes for rows in queries. */
  class Users(_tableTag: Tag) extends profile.api.Table[UsersRow](_tableTag, Some("omakartta"), "users") {
    def * = (id, name, password) <> (UsersRow.tupled, UsersRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), Rep.Some(password)).shaped.<>({r=>import r._; _1.map(_=> UsersRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(VARCHAR), PrimaryKey, Length(255,true) */
    val id: Rep[String] = column[String]("id", O.PrimaryKey, O.Length(255,varying=true))
    /** Database column name SqlType(VARCHAR), Length(255,true) */
    val name: Rep[String] = column[String]("name", O.Length(255,varying=true))
    /** Database column password SqlType(VARCHAR), Length(255,true) */
    val password: Rep[String] = column[String]("password", O.Length(255,varying=true))
  }
  /** Collection-like TableQuery object for table Users */
  lazy val Users = new TableQuery(tag => new Users(tag))
}
