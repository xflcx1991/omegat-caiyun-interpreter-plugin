# OmegaT plugin development skeleton

## How to get skeleton into your project

It is recommended to checkout a squashed history of skeleton.

```
$ mkdir myproject; cd myproject
$ git init
$ git remote add skeleton https://github.com/omegat-org/plugin-skeleton.git
$ git fetch skeleton
$ git checkout -b master skeleton/squashed
$ git remote remove skeleton
$ git remote add origin https://where.your/project/repository
$ git push -u origin master
```


## Where you should change?

- Source code: `src/main/java/*`
- Test code: `src/test/java/*` and `src/test/resources/*`
- Project name in `settings.gradle`
- Plugin Main class name in``gradle.properties`.
- Integration test code: `src/integration-test/java/*` and `src/integration-test/resources/*`
- Coding rules: `config/checkstyle/checkstyle.xml
- Source file Header rule: `config/checkstyle/header.txt`

## Build system

This skeleton use a Gradle build system as same as OmegaT version 4.0.0 and later.

## Dependency

OmegaT and dependencies are located on remote maven repositories.
It is nessesary to connect the internet at least first time to compile.

## Extensions from Gradle defaults

- Integration test support.
- `provided` configuration for OmegaT classes and libraries that is bundled with OmegaT.
- Static code verification with Checkstyle and FindBugs.
- FatJar generation.

## FatJar

OmegaT considered a plugin is a single jar file. If it is depend on some libraries, 
you should ship your plugin with these libraries.
It is why generating a FatJar, a single jar file with all runtime dependencies
which is not provided with OmegaT.

## Where is a built artifact?

You can find distribution files in `build/distributions/*.zip` and `*.tgz.`
Also you can find jar files at `build/libs/`

## Test report

You will find a test results report at `build/reports/` and can show it with your favorite web browser.

## Installation

You can get a plugin jar file from zip/tgz distribution file.
OmegaT plugin should be placed in `$HOME/.omegat/plugin` or `C:\Program Files\OmegaT\plugin`
depending on your operating system.

## License

This project is distributed under the GNU general public license version 3 or later.

