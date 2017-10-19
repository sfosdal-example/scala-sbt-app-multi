logLevel := Level.Warn

addSbtPlugin("com.eed3si9n"                      % "sbt-assembly"           % "0.14.5")
addSbtPlugin("com.eed3si9n"                      % "sbt-buildinfo"          % "0.7.0")
addSbtPlugin("com.github.gseitz"                 % "sbt-release"            % "1.0.5")
addSbtPlugin("com.thoughtworks.sbt-api-mappings" % "sbt-api-mappings"       % "1.1.0")
addSbtPlugin("com.timushev.sbt"                  % "sbt-updates"            % "0.3.2")
addSbtPlugin("com.typesafe.sbt"                  % "sbt-git"                % "0.9.3")
addSbtPlugin("net.virtual-void"                  % "sbt-dependency-graph"   % "0.8.2")
addSbtPlugin("org.scalastyle"                    %% "scalastyle-sbt-plugin" % "1.0.0")
addSbtPlugin("org.scoverage"                     % "sbt-scoverage"          % "1.5.1")

// see: https://github.com/sbt/sbt-git#known-issues
libraryDependencies += "org.slf4j" % "slf4j-nop" % "1.7.25"
