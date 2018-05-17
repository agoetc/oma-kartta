package forms

import play.api.data.Forms._
import play.api.data.Form
import play.api.data.validation.Constraints

object UserForm {

  case class CreateForm(id: String, name: String, password: String)
  case class SigninForm(id: String, password: String)

  val createForm = Form(
    mapping(
      "id" -> nonEmptyText(minLength = 4,maxLength = 20)
        .verifying(
          Constraints.pattern("\\w*".r,
            error = "形式が不正です。記号は _ が使用できます")),
      "name" -> nonEmptyText(maxLength = 20),
      "password" ->nonEmptyText(minLength = 6,maxLength = 20)
    )(CreateForm.apply)(CreateForm.unapply))



  val signinForm = Form(
    mapping(
      "id" -> nonEmptyText,
      "password" -> nonEmptyText
    )(SigninForm.apply)(SigninForm.unapply))

}