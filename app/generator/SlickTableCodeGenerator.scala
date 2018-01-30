package generator

import slick.codegen._

object SlickTableCodeGenerator extends App {
  SourceCodeGenerator.run(
    profile = "slick.jdbc.MySQLProfile",
    jdbcDriver = "com.mysql.cj.jdbc.Driver",
    url = "jdbc:mysql://localhost:3306/omakartta?nullNamePatternMatchesAll=true",
    outputDir = "./app",
    pkg = "models",
    user = Some("root"),
    password = Some(""),
    ignoreInvalidDefaults = true
  )
}