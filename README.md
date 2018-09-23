Tree Template - By Graciliano M. P.
========

Create templates for your projects.

## Template Example

A template is just a tree of files and properties that are stored inside a ZIP file:

    simple-java-template
    |-- src/
    |   `-- main/
    |       `-- java/
    |           `-- __ROOT_PACKAGE__/              ## A directory that will be created based in the property %ROOT_PACKAGE%.
    |                 `-- sys/                     ## Sub package of %ROOT_PACKAGE%.
    |                     |-- Utils.java           ## Utils class of the package. 
    |                     `-- __MAIN_CLASS__.java  ## A class with name defined by property %MAIN_CLASS%.
    |-- README.md                                  ## README file.
    `-- treetemplate.properties                    ## File explicity declaring the template properties and decriptions.


File `__MAIN_CLASS__.java`:

    package %ROOT_PACKAGE%.sys ;
    
    public class %MAIN_CLASS% {
        
    }
    
File `treetemplate.properties`:

    MAIN_CLASS=Java Main class name
    ROOT_PACKAGE=Java root package

* `treetemplate.properties` Will be loaded and removed from generated tree

## Template Generation Example

Using the template above, here is an example of the generated project: 

Defined properties:

    MAIN_CLASS=MainFoo
    ROOT_PACKAGE=org.foo

Generated tree:

    simple-java-template
    |-- src/
    |   `-- main/
    |       `-- java/
    |           `-- org/
    |               `-- foo/
    |                   `-- sys/
    |                       |-- Utils.java 
    |                       `-- MainFoo.java
    `-- README.md

Generated `MainFoo.java`:

    package org.foo.sys ;
    
    public class MainFoo {
        
    }

## Running

After clone the project with git:

```

$> ./gradlew run --console=plain

## ... Will prompt for:

Template Zip file> /tmp/some-template.zip
Destination of generated Zip file> /tmp/some-project-generated.zip 

## ... Will ask for properties values:

---------------------------------
TEMPLATE PROPERTIES:
-- MAIN_CLASS: MainFoo
-- ROOT_PACKAGE: org.foo

## ...

Saved: /tmp/some-project-generated.zip

```


## Requirements

- JDK 1.8+

## Installation

- We use [Gradle](http://www.gradle.org), a cross-platform build automation tool that help with our full development flow. If you prefer [install Gradle](http://www.gradle.org/installation) or use the [Gradle wrapper](http://www.gradle.org/docs/current/userguide/gradle_wrapper.html) inside this project.

## Build project

```
./gradlew clean build
```

## Run tests

```
./gradlew clean test
```

## Generate Javadoc

```
./gradlew javaDoc
```

*Happy Coding!*

