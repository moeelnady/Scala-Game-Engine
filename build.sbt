name := "EngineGame"

version := "0.1"

// Scala version in the current project
scalaVersion := "3.2.2"

// Add dependency on ScalaFX library
libraryDependencies += "org.scalafx" %% "scalafx" % "16.0.0-R25"

// Determine OS version of JavaFX binaries
lazy val osName = System.getProperty("os.name") match {
  case n if n.startsWith("Linux") => "linux"
  case n if n.startsWith("Mac") => "mac"
  case n if n.startsWith("Windows") => "win"
  case _ => throw new Exception("Unknown platform!")
}

// Add dependency on JavaFX libraries, OS dependent
lazy val javaFXModules = Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
libraryDependencies ++= javaFXModules.map(m =>
  "org.openjfx" % s"javafx-$m" % "16" classifier osName
)

// Add dependency on ScalaFX library
libraryDependencies += "org.scalafx" %% "scalafx" % "18.0.1-R27"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-encoding", "utf8", "-feature")
mainClass := Some("com.example.HelloWorld")

// Fork a new JVM for 'run' and 'test:run', to avoid JavaFX double initialization problems
fork := true
