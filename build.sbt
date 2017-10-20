import Dependencies._

lazy val `scala-sbt-app-multi` = (project in file("."))
  .aggregate(domain, core, app)
  .settings(globalSettings: _*)
  .enablePlugins(GitBranchPrompt)

lazy val domain = project
  .settings(globalSettings: _*)
  .settings(projectSettings: _*)
  .settings(domainSettings: _*)
  .enablePlugins(GitBranchPrompt)

lazy val core = project
  .settings(globalSettings: _*)
  .settings(projectSettings: _*)
  .settings(coreSettings: _*)
  .enablePlugins(GitBranchPrompt)

lazy val app = project
  .settings(globalSettings: _*)
  .settings(projectSettings: _*)
  .settings(appSettings: _*)
  .settings(buildInfoSettings: _*)
  .dependsOn(core, domain)
  .enablePlugins(GitBranchPrompt, BuildInfoPlugin)

lazy val globalSettings = Seq(
  dependencyUpdatesFilter -= moduleFilter(organization = "org.scala-lang")
)

lazy val domainSettings = Seq()

lazy val coreSettings = Seq(
  libraryDependencies ++= Seq(
    MetricsCore,
    MetricsDatadog,
    MetricsHealthChecks,
    MetricsJvm,
    MetricsLog4j2
  )
)

lazy val appSettings = Seq(
  buildInfoPackage := Seq(organization.value, safe("scala-sbt-app-multi")).mkString("."),
  libraryDependencies ++= Seq(
    Log4jApi,
    Log4jCore,
    Log4jSlf4jImpl,
    Pureconfig,
    TypesafeConfig
  )
)

lazy val projectSettings = Seq(
  fork                       := true,
  organization               := "net.fosdal.example",
  scalaVersion               := "2.11.11",
  coverageMinimum            := 0,
  coverageFailOnMinimum      := true,
  scalastyleFailOnError      := true,
  (scalastyleConfig in Test) := baseDirectory.value / "scalastyle-test-config.xml",
  libraryDependencies ++= Seq(
    Oslo,
    ScalaCheck,
    ScalaLogging,
    ScalaMock,
    ScalaTest,
    Slf4jApi
  ),
  scalacOptions in (Compile, compile) ++= Seq(
    "-deprecation",
    "-encoding",
    "UTF-8",
    "-feature",
    "-unchecked",
    "-Xfatal-warnings",
    "-Xfuture",
    "-Xlint",
    "-Yno-adapted-args",
    "-Ywarn-dead-code",
    "-Ywarn-numeric-widen",
    "-Ywarn-unused",
    "-Ywarn-value-discard"
  )
)

lazy val buildInfoSettings = Seq(
  buildInfoKeys := Seq[BuildInfoKey](
    organization,
    name,
    version,
    scalaVersion,
    sbtVersion,
    description,
    isSnapshot,
    git.gitHeadCommit,
    git.gitCurrentBranch,
    git.gitCurrentTags,
    git.gitUncommittedChanges,
    git.formattedShaVersion,
    git.formattedDateVersion,
    resolvers,
    libraryDependencies,
    scalacOptions in (Compile, compile),
    BuildInfoKey.action("configBase")(buildInfoPackage.value)
  ),
  buildInfoOptions ++= Seq(BuildInfoOption.BuildTime, BuildInfoOption.ToJson, BuildInfoOption.ToMap)
)

def safe(s: String): String = s.replaceAll("[^a-zA-Z0-9]", "_").replaceAll("^[0-9]*", "")

//
// Plugin Settings: sbt-assembly
//
//mainClass in assembly := Some("Main")
//
//test in assembly := {}
//
//assemblyMergeStrategy in assembly := {
//  case "reference.conf" => MergeStrategy.concat
//  case PathList("META-INF", xs@_*) =>
//    xs.map(_.toLowerCase) match {
//      case ("manifest.mf" :: Nil) | ("index.list" :: Nil) | ("dependencies" :: Nil) =>
//        MergeStrategy.discard
//      case ps@(e :: es) if ps.last.endsWith(".sf") || ps.last.endsWith(".dsa") =>
//        MergeStrategy.discard
//      case _ => MergeStrategy.discard
//    }
//  case _ => MergeStrategy.first
//}
