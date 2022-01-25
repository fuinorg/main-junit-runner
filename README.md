# main-junit-runner
Runs JUnit tests in a simple main() method and writes the results as JSON to disk.

[![Java Maven Build](https://github.com/fuinorg/main-junit-wrapper/actions/workflows/maven.yml/badge.svg)](https://github.com/fuinorg/main-junit-wrapper/actions/workflows/maven.yml)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.fuin/main-junits-runner/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.fuin/main-junits-runner/)
[![LGPLv3 License](http://img.shields.io/badge/license-LGPLv3-blue.svg)](https://www.gnu.org/licenses/lgpl.html)
[![Java Development Kit 11](https://img.shields.io/badge/JDK-11-green.svg)](https://openjdk.java.net/projects/jdk/11/)

## Versions
- 0.3.x (or later) = **Java 11** (Junit4)
- 0.2.0 = **Java 8** (Junit4)

## Usage
Assuming that you have a Java 8 runtime below the current directory in a folder named "jre" and all dependencies (jar files) in a folder named "lib", you can start the app using the following command:
  
```
jre\bin\java -classpath *;lib/* java.org.fuin.mjunitrun.JUnitApp \
        -name MyTest \
        -class org.fuin.mjunitrun.TestDummy \
        -dir /var/tmp
```

The following command line arguments can be used: 

| Argument | Type | Description | Example |
|:-------- |:-----|:------------|:--------|
| **-name** | mandatory | Unique test name | *MyTest* |
| **-class** | mandatory | Fully qualified name of the junit test class | *org.fuin.mjunitrun.TestDummy*
| **-dir** | mandatory | Directory to write the result to | */var/tmp* |
| **-logbackXmlSrc** | optional | Logback XML configuration file to copy, if it does not exist in the target directory (default is 'logback.xml' in current directory)) | */var/tmp/logback.xml* |
 
As a result, the following JSON file will be written to "/var/tmp/MyTest.json" in case of successful execution:

```json
{ 
  "message" : "Executed: 2, Passed: 2, Failed: 0" ,
  "status":"success"
}
```

In case of a failure the JSON file will look like this:

```json
{
	"message": "Executed: 2, Passed: 1, Failed: 1",
	"status": "failure",
	"failures": [
		{
			"description": "testTrue(org.fuin.mjunitrun.TestDummy)",
			"message": "expected:<[fals]e> but was:<[tru]e>",
			"trace": "org.junit.ComparisonFailure: expected:<[fals]e> but was:<[tru]e>\n
			... STACK-TRACE..."
		}
	]
}
```

