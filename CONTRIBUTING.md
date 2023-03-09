## Contributing

Hey there!!! We are happy that you would like to contribute to make this plugin great!

On the section bellow you find all the necessary steps to start contributing.

All contributions to this project are released under the project [GNU AGPL License, Version 3.0](https://www.gnu.org/licenses/agpl-3.0.en.html) license.

Happy coding :-).

### Submitting a pull request

These are the general steps to open a pull request with your contribution to the plugin:

1. [Fork](https://github.com/FRI-DAY/sonar-gosu-plugin/fork) and clone the repository;
2. Build the project. Make sure all the tests were successful;
3. Create a new branch: `git checkout -b your-branch-name`;
4. Add your contribution and appropriate tests. Make sure all tests are green;
5. Push to your fork and [submit a pull request](https://github.com/FRI-DAY/sonar-gosu-plugin/compare);
6. Enjoying a cup of coffee while our team review your pull request.

Here are a few things to keep in mind while preparing your pull request:

- Write tests for your changes; 
- Keep your changes focused. If there are independent changes, consider if they could be submitted as separate pull requests;
- Write good [commit messages](https://github.blog/2022-06-30-write-better-commits-build-better-projects/).

With that in mind, the chances of your pull request be accepted will be quite high.  

## Prerequisites
Follows a list of tools that you will need:

* `JDK 8`;
* IDE of your choice;
* Docker & Docker Compose (_optional_).

## Getting started

To facilitate the execution of  some common tasks, we include a `.env.example` file in the repository 
with some alias commands that can be useful during development. 

Once you had clone the repository due the following steps to use the alias commands:

1. Create a copy of the `.env.example` on your local repository.
   * E.g: `$ cp .env.example .env.local`

2. Source the `.env.local` so the alias commands are available on your console.
   * E.g: `$ source .env.local`
3. All done. The alias commands will be available for usage on your local console. 
Check the `.env.local` to see all the aliases available.

### License

To make sure all the source code files contain the license header we use the [License Gradle Plugin](https://github.com/hierynomus/license-gradle-plugin).
The plugin will fail the build process in case a new file is added, and it does not include the license header. 
To automatically apply the license on the files missing it you can execute the following command `./gradlew licenseFormat` or the `apply-license` command alias.

### Working with ANTLR4

**The Gosu Grammar should not be modified**. However, it can happen that there are bugs in the grammar or a new release of the Gosu language contains new features.

In such cases, there is a very useful tool called [ANTLR v4](https://github.com/antlr/intellij-plugin-v4/blob/master/README.md) plugin for IntelliJ IDEA.
With it, you can verify the parse tree built by ANTLR. It can be a helpful tool when writing new rules for the plugin ;-).

#### How does it work?

The Gosu ANTLR v4 Grammar is split into two files: `GosuLexer.g4` and `GosuParser.g4`.
The Lexer is responsible for file tokenization. It will process an input sequence of characters into an output sequence of tokens.
The Parser is responsible for grouping tokens into contexts and then building the parse tree.
From those two files, ANTLR v4 will generate the `GosuParserBaseListener` that we are using in the rules.
The `GosuParserBaseListener` ([Observer Pattern](https://en.wikipedia.org/wiki/Observer_pattern#:~:text=In%20software%20design%20and%20engineering,calling%20one%20of%20their%20methods)) have a set of hooks that 
we can use to "listen" each parsing context. Each of the contexts, have `enter` and `exit` hooks.
E.g: 
```java
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterIfStatement(GosuParser.IfStatementContext ctx) { }

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitIfStatement(GosuParser.IfStatementContext ctx) { }
```
In this case, the hooks will be triggered on every Gosu `if` statement the parser finds for a given Gosu file.

### Adding new Gosu rules

#### BaseGosuRule

All the rules should inherit from `BaseGosuRule` class. We use this base class to be able to automatically
inject all the Gosu rules on the GosuFileParser using [Google Guice](https://github.com/google/guice). 
Check the `AnalysisModule` for more details. 

The `BaseGosuRule` have hooks for all parsing contexts, since it is a subclass of the `GosuParserBaseListener`, and some common behaviour for Rules.

The `BaseGosuRule` also provides a method to add Sonarqube issues for the file being analysed. You can buld the `GosuIssue`
using its builder and add to issues using the `BaseGosuRule.addIssue()`.
E.g:
```java
addIssue(
        new GosuIssue.GosuIssueBuilder(this)
        .onToken(token).withMessage("Complete the task associated to this TODO comment.")
        .build()
);
```

The message will be shown on Sonarqube interface on the Gosu file line that contains the token specified on the `.onToken()` method.

#### Writing a new Gosu rule

These are the steps to write a new Gosu rule:
1. Create a new Rule implementation on the appropriate package:
E.g.: 
```java
package de.friday.sonarqube.gosu.plugin.rules.bugs;

public class MyAwesomeNewRule extends BaseGosuRule {
    
}
```

2. Add a unique key for the new Rule:
E.g.:
```java
package de.friday.sonarqube.gosu.plugin.rules.bugs;

@Rule(key = MyAwesomeNewRule.KEY)
public class MyAwesomeNewRule extends BaseGosuRule {

    static final String KEY = "MyAwesomeNewRule";
    
}
```

3. Add the metadata and rule description files on the `resources/sonar` folder:

:warning: `Both files must have the same names as the Rule key. This is required for the plugin to automatically load the 
rule metadata and description of each rule.`

E.g.:
`resources/sonar/MyAwesomeNewRule.json`:
```json
{
   "title": "My awesome new rule title",
   "type": "BUG",
   "status": "ready",
   "tags": [
      "pitfall",
      "cert",
      "unused"
   ],
   "remediation": {
      "func": "Constant\/Issue",
      "constantCost": "10min"
   },
   "defaultSeverity": "Major",
   "scope": "Main"
}
```

This will be description shown on Sonarqube interface. As a good practice add examples of compliant and non-compliant code snippets.
`resources/sonar/MyAwesomeNewRule.html`:
```html
<p>My awesome new rule description. </p>
<h2>Non-compliant Code Example</h2>
<pre>
   Non-compliant code snippet
</pre>
<h2>Compliant Solution</h2>
<pre>
   Compliant version of previous non-complaint code snippet
</pre>
```

4. Now we need to write the logic for the rule using ANTLR. Worth to mention that on `enter` hooks **we do
not have parsed context data that we want yet**. This is because hooks are triggered during the parsing process and the context
will be available with the data on the `exit` hooks.

```java
package de.friday.sonarqube.gosu.plugin.rules.bugs;

@Rule(key = MyAwesomeNewRule.KEY)
public class MyAwesomeNewRule extends BaseGosuRule {

    static final String KEY = "MyAwesomeNewRule";

   @Override
   public void exitIfStatement(GosuParser.IfStatementContext ctx) {
      //My rule logic
   }
}
```

Some rules require the file that is being analysed and/or the token stream generate by the Lexer for that file.
To get this information you must simply inject the GosuFileProperties on your rule class:
```java
package de.friday.sonarqube.gosu.plugin.rules.bugs;

@Rule(key = MyAwesomeNewRule.KEY)
public class MyAwesomeNewRule extends BaseGosuRule {

   static final String KEY = "MyAwesomeNewRule";

   private final GosuFileProperties gosuFileProperties;

   @Inject
   public LinesOfCodeMetric(GosuFileProperties gosuFileProperties) {
       this.gosuFileProperties = gosuFileProperties;
   }
   
   /* Omitted */
}
```

5. Once you have the logic of your new rule, you can add a Sonarqube issue matching the rule criteria:
```java
package de.friday.sonarqube.gosu.plugin.rules.bugs;

@Rule(key = MyAwesomeNewRule.KEY)
public class MyAwesomeNewRule extends BaseGosuRule {
    static final String KEY = "MyAwesomeNewRule";
    
    private int ifCounter = 0;
    
    @Override
    public void enterIfStatement(GosuParser.IfStatementContext context) {
        ifCounter++;
    }

    @Override
    public void exitIfStatement(GosuParser.IfStatementContext context) {
        if(ifCounter >= 5) {
          addIssue(
                  new GosuIssue.GosuIssueBuilder(this)
                          .onContext(context.expression())
                          .withMessage("Class has too many ifs statements.")
                          .build()
          );   
       }
    }
}
```

6. Now you just have to re-generate the rules repository documentation with the `./gradlew htmlToMarkdown` or `generate-rule-docs` and
to write some tests for your rule. 

#### Testing

To enable easier and straightforward way to test the plugin rules we create a test DSL that can be used as follows to write the tests:

1. Add files containing complaint and non-complaint code under `test/resources/rules/<Rule Key>/` folder.
E.g:
`test/resources/rules/MyAwesomeNewRule/ok.gs`
```gosu
package rules.MyAwesomeNewRule

final class ok {

  function doSomething() {
    if(invalid()) {
      throw new RuntimeException("Kaboom!")
    }
    
    process()
  }
  
  private function invalid(): boolean { return true; } 
  
  private function process() { /* Do something */ }

}
```

E.g:
`test/resources/rules/MyAwesomeNewRule/nok.gs`
```gosu
package rules.MyAwesomeNewRule

final class nok {

  function doSomething() {
    if(invalid()) { throw new RuntimeException("Kaboom!") }
    if(enable()) { /* do something */ }
    if(otherCondition()) { /* do something else */ }
    if(anotherCondition()) { /* another thing going on here */ }
    if(yetAnotherCondition()) { /* ok, that's too many conditions */ }
  }

}
```

2. Write the test, using the DSL, covering complaint and non-complaint cases.
a. For complaint cases you want to ensure the rule did not find any issue on the Gosu file:
```java
package de.friday.sonarqube.gosu.plugin.rules.bugs;

import org.junit.jupiter.api.Test;
import static de.friday.test.support.rules.dsl.gosu.GosuRuleTestDsl.given;

public class MyAwesomeNewRuleTest {
    
   @Test
   void findsNoIssuesWhenIfConditionsAreLessThanThreshold() {
      given("MyAwesomeNewRule/ok.gs")
              .whenCheckedAgainst(MyAwesomeNewRule.class)
              .then()
              .noIssuesFound();
   }
   
   
}
```

b. For non-complaint cases you can assert that the issue was found on the file and, optionally, if the issue was found on a
expected location:
```java
package de.friday.sonarqube.gosu.plugin.rules.bugs;

import org.junit.jupiter.api.Test;
import static de.friday.test.support.rules.dsl.gosu.GosuRuleTestDsl.given;

public class MyAwesomeNewRuleTest {
    
   @Test
   void findsIssuesWhenIfConditionsAreGreaterThanThreshold() {
      given("MyAwesomeNewRule/nok.gs")
              .whenCheckedAgainst(MyAwesomeNewRule.class)
              .then()
              .issuesFound()
              .hasSizeEqualTo(1)
              .and()
              .areLocatedOn(
                      GosuIssueLocations.of(Arrays.asList(3, 1, 3, 28))
              );
   }
}
```

### Testing the plugin on a local Sonarqube server

This section covers the general steps to start a local Sonarqube server with the plugin and run scans against it. 

These are the general steps:
1. Execute the `docker/start-sonar-server.sh` script. Eg.:
```shell
$ cd docker
$ ./start-sonar-server.sh
```
The script will build the plugin JAR, containerized it and start a Docker compose service with it. 
Once it finishes, Sonar will be available at `http://localhost:9000`.

2. With server up and running, create an authentication token as described [here](https://docs.sonarqube.org/latest/user-guide/user-token/).

3. Now that you have the authentication token you can scan your Gosu projects as follows:
```shell
docker run \
  --network=host \
  --rm \
  -e SONAR_HOST_URL="http://localhost:9000" \
  -e SONAR_LOGIN="<MY AUTHENTICATION TOKEN>" \
  -v "$(pwd):/usr/src" \
  sonarsource/sonar-scanner-cli \
  -Dsonar.projectKey=<MY PROJECT KEY> \
  -Dsonar.sources=<MY PROJECT SOURCE FOLDER> \
  -Dsonar.coverage.jacoco.xmlReportPaths=<MY PROJECT JACOCO XML REPORT PATHS> \
  -Dsonar.jacoco.reportPath=MY PROJECT JACOCO BINARY> -X
```

#### Installing the plugin on a running Sonarqube Docker container

In case you already have the Docker container with the Sonarqube server running, you can install the plugin as follows:

1. Build the plugin JAR file with your changes;
2. Copy the JAR file to the running container;
E.g.:
```shell
$ docker cp build/libs/sonar-gosu-plugin-1.0.0-SNAPSHOT.jar sonarqube:/opt/sonarqube/extensions/plugins
```
3. Restart Sonarqube Docker container;
E.g.:
```shell
$ docker container restart <Sonarqube Container ID>
```

#### Elasticsearch issue

Sonarqube has an embedded Elasticsearch instance, once you start Sonarqube with docker-compose, if you get the following error:
```log
$ ERROR: [1] bootstrap checks failed. You must address the points described in the following [1] lines before starting Elasticsearch.
$ bootstrap check failure [1] of [1]: max virtual memory areas vm.max_map_count [65530] is too low, increase to at least [262144]
$ ERROR: Elasticsearch did not exit normally - check the logs at /opt/sonarqube/logs/sonarqube.log
```

Follow the instructions described [here](https://www.elastic.co/guide/en/elasticsearch/reference/current/vm-max-map-count.html) and increase
the `vm.max_map_count`. 
E.g.: `$ sysctl -w vm.max_map_count=262144`

The service should start normally after it has been restarted with the new configuration.


-------
## Resources
- [How to contribute to Open Source?](https://opensource.guide/how-to-contribute/);
- [ANTLR4 Documentation](https://github.com/antlr/antlr4/blob/master/doc/index.md);
- [Sonar Plugin basics](https://docs.sonarqube.org/9.6/extension-guide/developing-a-plugin/plugin-basics/);
- [Write better commits, build better projects](https://github.blog/2022-06-30-write-better-commits-build-better-projects/);
- [About Pull Requests](https://docs.github.com/en/pull-requests/collaborating-with-pull-requests/proposing-changes-to-your-work-with-pull-requests/about-pull-requests);
