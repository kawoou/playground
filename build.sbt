name := "Playground"
version := "0.1"
scalaVersion := "2.12.6"

/// Akka
libraryDependencies ++= {
  def akkaGroup = "com.typesafe.akka"
  def akkaVersion = "2.5.13"
  def akkaHttpVersion = "10.1.1"

  Seq(
    akkaGroup %% "akka-actor" % akkaVersion,
    akkaGroup %% "akka-stream" % akkaVersion,
    akkaGroup %% "akka-http" % akkaHttpVersion,
    akkaGroup %% "akka-http-spray-json" % akkaHttpVersion,
    akkaGroup %% "akka-slf4j" % akkaVersion
  )
}

/// Alpakka
libraryDependencies ++= {
  def lightbendGroup = "com.lightbend.akka"
  def alpakkaVersion = "0.19"

  Seq(
    lightbendGroup %% "akka-stream-alpakka-slick" % alpakkaVersion,
    lightbendGroup %% "akka-stream-alpakka-s3" % alpakkaVersion
  )
}

/// Database
libraryDependencies ++= {
  def slickGroup = "com.typesafe.slick"
  def slickVersion = "3.2.1"
  def postgreDriverVersion = "42.2.2"

  Seq(
    slickGroup %% "slick" % slickVersion,
    slickGroup %% "slick-hikaricp" % slickVersion,
    "org.postgresql" % "postgresql" % postgreDriverVersion
  )
}

libraryDependencies += "org.jsoup" % "jsoup" % "1.11.3"
libraryDependencies += "org.slf4j" % "slf4j-simple" % "1.7.25"
libraryDependencies += "com.ning" % "async-http-client" % "1.9.40"

resolvers += "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/"
resolvers += "Java.net Maven2 Repository" at "https://download.java.net/maven/2/"
resolvers += "Maven Central" at "https://repo.maven.apache.org/maven2"

mainClass in Compile := Some("com.playground.Boot")