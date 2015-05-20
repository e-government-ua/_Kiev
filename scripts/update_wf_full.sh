#!/bin/sh
echo Set up or update workflow full
cd /project
echo Clean up the project
mvn clean
echo Proceeding with fast update
 /project/scripts/update_wf_fast.sh



