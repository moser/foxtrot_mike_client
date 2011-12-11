name := "Foxtrot Mike Client"

version := "1.0"

organization := "de.moserei"

scalaVersion := "2.9.1"

resolvers += "Scala-Tools Maven2 Repository" at "http://scala-tools.org/repo-releases"

resolvers += "eclipse link" at "http://download.eclipse.org/rt/eclipselink/maven.repo"

resolvers += "javax" at "http://download.java.net/maven/2/"

resolvers += "codehaus" at "http://repository.codehaus.org/"

libraryDependencies += "net.databinder" %% "dispatch-json" % "0.8.6"

libraryDependencies += "net.databinder" %% "dispatch-http" % "0.8.6"

libraryDependencies += "net.databinder" %% "dispatch-http-json" % "0.8.6"

libraryDependencies += "org.scala-lang" % "scala-library" % "2.9.1"

libraryDependencies += "org.scala-tools.time" %% "time" % "0.5"

libraryDependencies += "org.scala-lang" % "scala-swing" % "2.9.1"

libraryDependencies += "org.scalaj" %% "scalaj-collection" % "1.2"

libraryDependencies += "com.miglayout" % "miglayout" % "3.7.+" classifier "swing"

libraryDependencies += "org.eclipse.persistence" % "org.eclipse.persistence.jpa" % "2.3.1"

libraryDependencies += "org.eclipse.persistence" % "javax.persistence" % "2.0.0"

libraryDependencies += "com.h2database" % "h2" % "1.2.+"

libraryDependencies += "org.scalatest" %% "scalatest" % "1.6.1" % "test"
