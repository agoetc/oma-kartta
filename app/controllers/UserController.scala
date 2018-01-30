//package controllers
//
//import javax.inject._
//import play.api.mvc._
//
//  class UserController extends UserControllerBase with ScalatraServlet with FormSupport with I18nSupport
//
//trait UserControllerBase extends ScalatraServlet with FormSupport with I18nSupport {
//
//  case class UserNewForm(id: String, name: String, password: String)
//
//  val newForm = mapping(
//    "id" -> label("id", text(required, maxlength(20))),
//    "name" -> label("name", text(required, maxlength(20))),
//    "password" -> label("password", text(required, maxlength(20))),
//  )(UserNewForm.apply)
//
//
//  def userCreate(form: UserNewForm) {
//    val db = Database.forConfig("mysqldb")
//    db.run(Users.map(user => (user.id, user.name, user.password)) += (form.id, form.name, form.password))
//    redirect("/")
//  }
//
//  post("/register") {
//    validate(newForm)(
//      errors => BadRequest(oma.kartta.html.signup()),
//      form => userCreate(form)
//    )
//  }
//
//  case class UserAuthentForm(id: String, password: String)
//
//  val authentForm = mapping(
//    "id" -> label("id", text(required, maxlength(20))),
//    "password" -> label("password", text(required, maxlength(20))),
//  )(UserAuthentForm.apply)
//
//  def userCheck(form: UserAuthentForm) {
//
//    val db = Database.forConfig("mysqldb")
//    val user = db.run(Users.filter(user => user.id === form.id && user.password === form.password).result)
//
//    user.onComplete {
//      case Success(r) => println(r)
//      case Failure(t) => println(t.getMessage)
//    }
//  }
//
//  post("/Memlog") {
//    validate(authentForm)(
//      errors => BadRequest(oma.kartta.html.signin()), // 入力チェックに引っかかったら
//      form => userCheck(form) // 入力チェックに成功したら
//    )
//
//  }
//
//}