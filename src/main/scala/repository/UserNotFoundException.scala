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
package repository

/**
 * Exception thrown when Document couldn't be found in MongoDB.
 *
 * @param msg message with information about exception
 */
case class UserNotFoundException(msg: String) extends RuntimeException(msg) {}
