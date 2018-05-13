package forms

import play.api.data.Forms._
import play.api.data.Form
import play.api.data.validation.Constraints

object UserForm {

  case class UserNewForm(id: String, name: String, password: String)

  val newForm = Form(
    mapping(
      "id" -> nonEmptyText(minLength = 4,maxLength = 20)
        .verifying(
          Constraints.pattern("\\w*".r,
            error = "形式が不正です。記号は _ が使用できます")),
      "name" -> nonEmptyText(maxLength = 20),
      "password" ->nonEmptyText(minLength = 6,maxLength = 20)
    )(UserNewForm.apply)(UserNewForm.unapply))


  case class AuthForm(id: String, password: String)

  val authForm = Form(
    mapping(
      "id" -> nonEmptyText,
      "password" -> nonEmptyText
    )(AuthForm.apply)(AuthForm.unapply))

}