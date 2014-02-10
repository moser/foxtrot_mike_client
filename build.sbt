import AssemblyKeys._ // put this at the top of the file

name := "Foxtrot Mike Client"

version := "1.0"

organization := "de.moserei"

scalaVersion := "2.10.3"

resolvers += "Scala-Tools Maven2 Repository" at "http://scala-tools.org/repo-releases"

resolvers += "eclipse link" at "http://download.eclipse.org/rt/eclipselink/maven.repo"

resolvers += "javax" at "http://download.java.net/maven/2/"

resolvers += "codehaus" at "http://repository.codehaus.org/"

libraryDependencies += "net.databinder" %% "dispatch-json" % "0.8.10"

libraryDependencies += "net.databinder" %% "dispatch-http" % "0.8.10"

libraryDependencies += "net.databinder" %% "dispatch-http-json" % "0.8.10"

libraryDependencies += "org.scalaj" %% "scalaj-http" % "0.3.14"

libraryDependencies += "org.scala-lang" % "scala-library" % "2.10.3"

libraryDependencies += "org.scala-tools" % "time" % "2.7.4-0.1"

libraryDependencies += "org.scala-lang" % "scala-swing" % "2.10.3"

libraryDependencies += "org.scalaj" %% "scalaj-collection" % "1.5"

libraryDependencies += "com.miglayout" % "miglayout" % "3.7.+" classifier "swing"

libraryDependencies += "org.eclipse.persistence" % "org.eclipse.persistence.jpa" % "2.3.1"

libraryDependencies += "org.eclipse.persistence" % "javax.persistence" % "2.0.0"

libraryDependencies += "com.h2database" % "h2" % "1.2.+"

libraryDependencies += "org.scalatest" %% "scalatest" % "1.9.2" % "test"

parallelExecution in Test := false

seq(assemblySettings: _*)

mergeStrategy in assembly := { 
  case n if n.startsWith("META-INF/ECLIPSEF.RSA") => MergeStrategy.discard
  case n if n.startsWith("META-INF/ECLIPSEF.SF") => MergeStrategy.discard
  case n if n.startsWith("META-INF/MANIFEST.MF") => MergeStrategy.discard
  case n if n.startsWith("META-INF/NOTICE.txt") => MergeStrategy.discard
  case n if n.startsWith("META-INF/NOTICE") => MergeStrategy.discard
  case n if n.startsWith("META-INF/LICENSE.txt") => MergeStrategy.discard
  case n if n.startsWith("META-INF/LICENSE") => MergeStrategy.discard
  case n if n.startsWith("readme.html") => MergeStrategy.discard
  case n if n.startsWith("readme.txt") => MergeStrategy.discard
  case n if n.startsWith("library.properties") => MergeStrategy.discard
  case n if n.startsWith("license.html") => MergeStrategy.discard
  case n if n.startsWith("about.html") => MergeStrategy.discard
  case _ => MergeStrategy.deduplicate
}

packageOptions in assembly += Package.ManifestAttributes("SplashScreen-Image" -> "splash.png")
