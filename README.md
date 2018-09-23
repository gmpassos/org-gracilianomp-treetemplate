Tree Template - By Graciliano M. P.
========

An easy way to create templates for your projects.

## Template Example

A template is just a tree of files and properties that are stored inside a ZIP file:

File: `template1.zip`

    __PROJECT_NAME__/                              ## Root directory of the project, using the property %PROJECT_NAME%.
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
    
File `treetemplate.properties`, declaring properties and their descriptions:

    PROJECT_NAME=Project name. Will be used for root directory
    MAIN_CLASS=Java Main class name
    ROOT_PACKAGE=Java root package

* `treetemplate.properties` Will be loaded and removed from generated tree

## Template Properties

The properties of a template should be declared in the file `treetemplate.properties`.

The format of a property/variable name should be uppercase, using only `_`, letters and numbers: `[A-Z0-9_]+`

A variable can be used inside files with `%VAR_NAME%`, or in file/directory names with `__VAR_NAME__`


## Template Generation Example

Using the template above, here is an example of the generated project: 

Defined properties:

    PROJECT_NAME=simple-project
    MAIN_CLASS=MainFoo
    ROOT_PACKAGE=org.foo

Generated tree:

    simple-project
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

First clone this project:

```
$> git clone https://github.com/gmpassos/org-gracilianomp-treetemplate.git
$> cd org-gracilianomp-treetemplate
```

Now you can build and run the project:

```

$> ./gradlew run --console=plain

## ... Will prompt for:

Template Zip file> /tmp/some-template.zip
Destination of generated Zip file> /tmp/some-project-generated.zip 

## ... Will ask for properties values:

---------------------------------
TEMPLATE PROPERTIES:
-- PROJECT_NAME (Project name. Will be used for root directory)=simple-project
-- MAIN_CLASS (Java Main class name): MainFoo
-- ROOT_PACKAGE (Java root package): org.foo

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

