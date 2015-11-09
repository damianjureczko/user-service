package core

import com.typesafe.config.ConfigFactory

trait MainConfig {

  lazy val config = ConfigFactory.load()
}
