# kOS-IDEs
This plugin enables support for kOS KerboScript in [IntelliJ IDEA](https://www.jetbrains.com/idea/) IDE.

Current kOS version supported: 0.18.2

###INSTALL###

0. Install Intellij IDEA
1. Install "KerboScript(kOS)" plugin. [Plugins installation instruction.](
https://www.jetbrains.com/idea/help/installing-updating-and-uninstalling-repository-plugins.html)

###BUILDING###

Project uses [Gradle](https://gradle.org/) build system and [gradle-intellij-plugin](https://github.com/jetbrains/gradle-intellij-plugin)

Ensure you have Java 8 installed on your machine

To build project just run `./gradlew build` (Linux/OSX) or `gradlew.bat build` (Windows)

Built plugin distribution would be in `build/distributions/` directory.

###EDITING###

It's recommended to use IntelliJ IDEA to edit sources, but you may use your favorite IDE (better with Gradle support)
0. Follow [prerequisites steps](
http://www.jetbrains.org/intellij/sdk/docs/tutorials/custom_language_support/prerequisites.html)
with exception that you don't need to clone IntelliJ sources
1. Import gradle project in IDEA folder in your IntelliJ IDEA
2. Develop
3. If you need to regenerate parser, psi model or lexer from .bnf, .flex files,
 run 'generateSources' gradle task. Also that task would be runner on 'build'.
4. Build plugin with 'build' gradle task
5. Run separate IDEA with plugin with 'runIdea' gradle task
