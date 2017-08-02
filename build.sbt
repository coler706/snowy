lazy val root =
  (project in file("."))
    .aggregate(server, client, measures, measuresListener, load, sharedJvm, sharedJs)
    .settings(commonSettings: _*)


lazy val commonSettings = Seq(
  version := "0.1.0",
  scalaVersion := V.scala,
  scalacOptions ++= Seq(
    "-unchecked",
    "-deprecation",
    "-feature",
    "-Xfatal-warnings",
    "-language:postfixOps"
  ),
  libraryDependencies ++= Seq(
    "org.scalacheck" %%% "scalacheck" % V.scalacheck % "test",
    "org.scalactic"  %%% "scalactic"  % V.scalactic  % "test",
    "org.scalatest"  %%% "scalatest"  % V.scalatest  % "test"
  ),
  test in assembly := {}
)

lazy val itSettings = Defaults.itSettings ++ Seq(
  libraryDependencies ++= Seq(
    "org.scalacheck" %% "scalacheck" % V.scalacheck % "it",
    "org.scalactic"  %% "scalactic"  % V.scalactic  % "it",
    "org.scalatest"  %% "scalatest"  % V.scalatest  % "it"
  )
)

lazy val V = new Object {
  val scala      = "2.12.3"
  val akka       = "2.5.3"
  val akkaHttp   = "10.0.9"
  val jackson    = "2.9.0"
  val log4j      = "2.8.2"
  val scalacheck = "1.13.5"
  val scalactic  = "3.0.3"
  val scalatest  = "3.0.3"
}

lazy val scalaLogging = Seq(
  "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2"
)

lazy val akkaStreams = Seq(
  "com.typesafe.akka" %% "akka-actor"  % V.akka,
  "com.typesafe.akka" %% "akka-stream" % V.akka
)

lazy val loggingProvider = Seq(
  "org.apache.logging.log4j"         % "log4j-core"              % V.log4j,
  "org.apache.logging.log4j"         % "log4j-slf4j-impl"        % V.log4j,
  "com.fasterxml.jackson.dataformat" % "jackson-dataformat-yaml" % V.jackson,
  "com.fasterxml.jackson.core"       % "jackson-databind"        % V.jackson
)

lazy val scopt = Seq(
  "com.github.scopt" %% "scopt" % "3.6.0"
)

lazy val server = (project in file("server"))
  .enablePlugins(JavaAppPackaging)
  .settings(commonSettings: _*)
  .settings(
    assemblyJarName in assembly := "full.jar",
    herokuAppName in Compile := "snowy-3d",
    herokuFatJar in Compile := Some((assemblyOutputPath in assembly).value),
    name := "server",
    javaOptions := Seq(
      "-Xmx500m",
      "-Xms500m",
      "-XX:+UseG1GC",
      "-XX:+UseCompressedOops",
      "-XX:+AggressiveOpts",
      "-XX:MaxGCPauseMillis=10"
    ),
    javaOptions in reStart := javaOptions.value,
    libraryDependencies ++= Seq(
      "org.typelevel"            %% "squants"    % "1.3.0",
      "org.apache.logging.log4j" % "log4j-jul"   % V.log4j,
      "com.typesafe.akka"        %% "akka-http"  % V.akkaHttp,
      "com.typesafe.akka"        %% "akka-slf4j" % V.akka,
      "org.typelevel"            %% "cats"       % "0.9.0"
    ) ++ scopt ++ scalaLogging ++ loggingProvider ++ akkaStreams,
    (resourceGenerators in Compile) += Def.task {
      val f1          = (fastOptJS in Compile in client).value.data
      val f1SourceMap = f1.getParentFile / (f1.getName + ".map")
      val f2          = (packageJSDependencies in Compile in client).value
      Seq(f1, f1SourceMap, f2)
    }.taskValue,
    watchSources ++= (watchSources in client).value
  )
  .dependsOn(sharedJvm, measures)

lazy val client = (project in file("client"))
  .enablePlugins(ScalaJSPlugin)
  .settings(commonSettings: _*)
  .settings(
    name := "Sock Client",
    scalaJSUseMainModuleInitializer := true,
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "0.9.3"
    ),
    jsDependencies ++= Seq(
      "org.webjars.bower" % "three.js"                   % "0.86.0" / "0.86.0/three.min.js",
      "org.webjars.bower" % "github-com-mrdoob-stats-js" % "r17" / "r17/build/stats.min.js"
    )
  )
  .dependsOn(shared.js)

lazy val measures = (project in file("measures"))
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= scalaLogging ++ akkaStreams
  )

lazy val measuresListener = (project in file("measures-listener"))
  .settings(commonSettings: _*)
  .settings(
    javaOptions in reStart ++= Seq(
      "-XX:MaxDirectMemorySize=512G",
      "-Xmx3G",
      "-Xms3G"
    ),
    libraryDependencies ++= Seq(
      "com.orientechnologies" % "orientdb-graphdb" % "2.2.23"
    ) ++ scopt ++ scalaLogging ++ loggingProvider ++ akkaStreams
  )
  .dependsOn(measures)

lazy val load = (project in file("load"))
  .enablePlugins(JavaAppPackaging)
  .configs(IntegrationTest)
  .settings(commonSettings: _*)
  .settings(itSettings: _*)
  .settings(
    libraryDependencies ++= Seq(
      "com.typesafe.akka"   %% "akka-testkit"        % V.akka,
      "com.typesafe.akka"   %% "akka-stream-testkit" % V.akka,
      "org.asynchttpclient" % "async-http-client"    % "2.1.0-alpha21"
    )
  )
  .dependsOn(server, sharedJvm, measures)

lazy val shared = (crossProject.crossType(CrossType.Pure) in file("shared"))
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= Seq(
      "io.suzaku" %%% "boopickle" % "1.2.6"
    ),
    addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
  )

lazy val sharedJvm = shared.jvm
lazy val sharedJs  = shared.js

// loads the server project at sbt startup
onLoad in Global := (Command
  .process("project server", _: State)) compose (onLoad in Global).value
