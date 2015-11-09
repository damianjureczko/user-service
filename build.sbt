name := "user-service"

version := "0.0.1"

scalaVersion := "2.11.7"

libraryDependencies ++= {
  val akkaVersion = "2.3.14"
  val sprayVersion = "1.3.3"
  val macwireVersion = "2.1.0"

  Seq(
    // akka
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",

    // spray
    "io.spray" %% "spray-can" % sprayVersion,
    "io.spray" %% "spray-routing" % sprayVersion,
    "io.spray" %% "spray-json" % "1.3.2",
    "io.spray" %% "spray-client" % sprayVersion,
    "io.spray" %% "spray-testkit" % sprayVersion % "test",

    // macwire
    "com.softwaremill.macwire" %% "macros" % macwireVersion,
    "com.softwaremill.macwire" %% "util" % macwireVersion,

    // logging
    "org.slf4j" % "slf4j-api" % "1.7.12",
    "org.slf4j" % "jcl-over-slf4j" % "1.7.12",
    "ch.qos.logback" % "logback-classic" % "1.1.3",

    // tests
    "org.scalatest" %% "scalatest" % "2.2.5" % "test",
    "org.scalamock" %% "scalamock-scalatest-support" % "3.2.2" % "test"
  )

}

Revolver.settings