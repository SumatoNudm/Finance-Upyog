#!/bin/bash
set -e

# Prompt for Docker username and version tag
read -p "Enter your Docker Hub username: " DOCKER_USER
read -p "Enter the Docker image version (e.g. 4.0.9): " VERSION

# Clear existing builds
echo "Cleaning existing builds..."

#(cd egov/egov-ear && rm -rf target)

# Run Maven clean package
#echo "Running Maven clean package..."
## (mvn clean package)
#(cd egov && mvn clean package -s settings.xml -Ddb.user=postgres -Ddb.password=postgres -Ddb.driver=org.postgresql.Driver -Ddb.url=jdbc:postgresql://localhost:5432/finance_db_v12 -DskipTests)
#if [ $? -ne 0 ]
#then
#    echo "Maven build failed. Exiting."
#    exit 2
#fi

echo "Building Docker image..."

docker build -t ${DOCKER_USER}/egov-finance:${VERSION} -f Dockerfile .

if [ $? -ne 0 ]; then
    echo -e "Failed while building docker image."
    exit 3
fi

echo -e "Docker image ${DOCKER_USER}/egov-finance:${VERSION} built successfully."
