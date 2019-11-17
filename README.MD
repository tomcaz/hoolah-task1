# The Transaction Analyser


### How To Build
```
mvn assembly:assembly -DdescriptorId=jar-with-dependencies
```
### How To Run
```
java -jar target/hoolah-1.0-SNAPSHOT-jar-with-dependencies.jar
```

### For MAC User, will need to add path for mvn
```
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0_221.jdk/Contents/Home
export PATH=<MAVEN_PATH>apache-maven-3.6.2/bin:$PATH

```