package utils

import javax.inject.{ Inject, Singleton }
import play.api.i18n.MessagesApi
import play.api.mvc._
import scala.concurrent.{ ExecutionContext, Future }
import play.api.mvc.Results._

@Singleton
class AuthenticatedAction @Inject()(playBodyParsers: PlayBodyParsers, messagesApi: MessagesApi)(implicit val executionContext: ExecutionContext)
  extends ActionBuilder[MessagesRequest, AnyContent] {

  override def parser: BodyParser[AnyContent] = playBodyParsers.anyContent

  override def invokeBlock[A](request: Request[A], block: MessagesRequest[A] => Future[Result]): Future[Result] = {
    request.session.get("user_id") match {
      case Some(username) => block(new MessagesRequest(request, messagesApi))
      case None => Future(Redirect("/signin"))
    }
  }
}