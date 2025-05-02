# !/bin/bash

find /home/aly0na/OOP/OOP/Task_2_4_1/app/./repos/ -name "gradlew" -exec chmod +x {} \;
gradle run --args="/home/aly0na/OOP/OOP/Task_2_4_1/app/build/resources/main/courseConfig.groovy report.html pull"
