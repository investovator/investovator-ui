##Important
First you need to install the JABM jar in to your local maven repo. Download it from [here](http://jabm.sourceforge.net/) and install it to the local repo using the following command

**mvn install:install-file -Dfile=jabm-0.8-with-libs.jar -DgroupId=net.sourceforge.jabm -DartifactId=jabm -Dversion=0.8 -Dpackaging=jar**

##How to run...?

Follow these steps.
* mvn package
* mvn jetty:run

Then the app becomes available at http://localhost:8080/
