#!/bin/bash
CURDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

EARFILE="${CURDIR}/egov-ear/target/egov-ear-*.ear"
dated=$(date '+%d-%m-%y')
WILDFLY_HOME="/Users/MriDx/projs/nudm/devops/wildfly"

if [ ! -d "$WILDFLY_HOME" ]; 
then
    echo "${WILDFLY_HOME} doesn't exist."
    exit 1
fi

DEPLOY_FOLDER="${WILDFLY_HOME}/standalone/deployments"
if [ "${1}X" == "cleanX" ]
then
    echo "Remove EAR files ${DEPLOY_FOLDER}"
    rm -rf ${DEPLOY_FOLDER}/*
    exit 0
fi

WILDFLY_PID=$(ps -ef | grep -v grep | grep -i java | grep ${WILDFLY_HOME} | awk '{print $2}')
if [ "${WILDFLY_PID}X" != "X" ]
then
    kill -9 ${WILDFLY_PID}
    echo "Stopped WildFly...!!"
    sleep 2
fi

[ -d "${WILDFLY_HOME}/Archive" ] || mkdir -p "${WILDFLY_HOME}/Archive"
echo "Archiving OLD EAR to ${WILDFLY_HOME}/Archive"
mv ${DEPLOY_FOLDER}/egov-ear*.ear ${WILDFLY_HOME}/Archive/egov-ear.ear.${dated}

# Run Maven clean package
echo "Running Maven clean package..."
# (mvn clean package)
(mvn clean package -s settings.xml -Ddb.user=postgres -Ddb.password=postgres -Ddb.driver=org.postgresql.Driver -Ddb.url=jdbc:postgresql://localhost:5432/finance_db_v6 -DskipTests)
if [ $? -ne 0 ]
then
    echo "Maven build failed. Exiting."
    exit 2
fi

echo "Copying local EAR to ${DEPLOY_FOLDER}"
rm -rf ${DEPLOY_FOLDER}/*
cp -rp ${EARFILE} ${DEPLOY_FOLDER}/egov-ear.ear
if [ $? -eq 0 ]
then 
    touch ${DEPLOY_FOLDER}/egov-ear.ear.dodeploy
    echo "Starting WildFly ...."
    ${WILDFLY_HOME}/bin/standalone.sh -b 0.0.0.0 &
    sleep 2
    echo "WildFly Started, and you can see the logs @ ${WILDFLY_HOME}/standalone/log/server.log"
	tail -f ${WILDFLY_HOME}/standalone/log/server.log
else
    echo "Unable to copy the EAR, please check the permission or disk space error."
    exit 2
fi
