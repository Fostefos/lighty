#!/bin/bash

#start controller with java 8
java -ms128m -mx128m -XX:MaxMetaspaceSize=128m -jar lighty-community-restconf-netconf-app-11.2.3-SNAPSHOT.jar #sampleConfigSingleNode.json


#start controller with java 11 or later
#java --add-modules java.xml.bind -jar lighty-community-restconf-netconf-app-11.2.3-SNAPSHOT.jar sampleConfigSingleNode.json
