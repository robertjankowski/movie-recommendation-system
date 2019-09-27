name := "music-recommendation-system"

version := "0.1"

scalaVersion := "2.12.8"

scalacOptions ++= Seq(
  "-encoding", "utf8",
  "-Xfatal-warnings",
  "-deprecation",
  "-unchecked",
  "-language:implicitConversions",
  "-language:higherKinds",
  "-language:existentials",
  "-language:postfixOps"
)

val akkaVersion = "2.5.23"
val sparkVersion = "2.4.4"

libraryDependencies ++= Seq(
  "com.typesafe.akka"            %% "akka-actor"      % akkaVersion,
  "com.typesafe.akka"            %% "akka-stream"     % akkaVersion,
  "com.typesafe.akka"            %% "akka-http"       % "10.1.8",
  "ch.qos.logback"               %  "logback-classic" % "1.2.3",
  "com.typesafe.scala-logging"   %% "scala-logging"   % "3.9.2",
  "org.apache.spark"             %% "spark-core"      % sparkVersion,
  "org.apache.spark"             %% "spark-sql"       % sparkVersion
)

enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)

mainClass in Compile := Some("Boot")
dockerBaseImage      := "openjdk:8-jre"
