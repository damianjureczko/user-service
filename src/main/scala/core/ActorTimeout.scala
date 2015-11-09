/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */
package core

import akka.util.Timeout

/**
 * Trait that defines default timeout for asynchronous operations.
 */
trait ActorTimeout {

  import scala.concurrent.duration._

  implicit def timeout: Timeout = 1.second
}
