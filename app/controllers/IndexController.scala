package controllers

import javax.inject._
import play.api.mvc._

@Singleton
class IndexController @Inject()(cc: ControllerComponents) extends AbstractController(cc){

  def index = Action{
    Ok(views.html.index())
  }

  def signin = Action{
    Ok(views.html.signin())
  }

  def signup = Action{
    Ok(views.html.signin())
  }

  def signout = Action{
    Redirect(routes.IndexController.index())
  }

  def main =  Action{
    Ok(views.html.main())
  }
}
