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
package api

import akka.util.Timeout

/**
 * Timeout for asynchronous operation within endpoint.
 */
trait EndpointTimeOut {

  import scala.concurrent.duration._

  implicit def resourceTimeout: Timeout = 1.seconds
}
