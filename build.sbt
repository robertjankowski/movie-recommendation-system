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

libraryDependencies ++= Seq(
  "ch.qos.logback"               %  "logback-classic" % "1.2.3",
  "org.apache.spark"             %% "spark-core"      % "2.4.4",
  "com.typesafe.scala-logging"   %% "scala-logging"   % "3.9.2",
  "org.apache.spark"             %% "spark-sql"       % "2.4.4"
)

javaOptions in run ++= Seq(
  "-Dlog4j.debug=true",
  "-Dlog4j.configuration=log4j.properties"
)