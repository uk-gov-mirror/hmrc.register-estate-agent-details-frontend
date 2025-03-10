/*
 * Copyright 2021 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package controllers.actions

import config.FrontendAppConfig
import javax.inject.Inject
import models.requests.IdentifierRequest
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

class FakeIdentifierAction @Inject()(config: FrontendAppConfig,
                                     override val parser: BodyParsers.Default,
                                     estatesAuthFunctions: EstatesAuthorisedFunctions,
                                     override implicit val executionContext: ExecutionContext) extends IdentifierAction(parser, estatesAuthFunctions, config) {

  override def invokeBlock[A](request: Request[A], block: IdentifierRequest[A] => Future[Result]): Future[Result] =
    block(IdentifierRequest(request, "id", "SARN1234567"))

  override def composeAction[A](action: Action[A]): Action[A] = new FakeAffinityGroupIdentifierAction(action, estatesAuthFunctions, config)

}

class FakeAffinityGroupIdentifierAction[A](
                                            action: Action[A],
                                            estatesAuthFunctions: EstatesAuthorisedFunctions,
                                            config: FrontendAppConfig)
  extends AffinityGroupIdentifierAction(action, estatesAuthFunctions, config)  {
  override def apply(request: Request[A]): Future[Result] = {
    action(request)
  }
}
