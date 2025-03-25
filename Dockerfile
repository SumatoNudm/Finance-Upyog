# Stage 1: Build the .ear file using Maven
FROM egovio/alpine-maven-builder-jdk-8:gcp AS build
WORKDIR /app

# Set working directory inside the container
#WORKDIR /opt/jboss/wildfly/standalone/deployments/

# Copy the entire project (finance-upyog)
#COPY . .

# Navigate to the 'egov' directory and build the project
#RUN cd egov && mvn clean package -DskipTests



# Stage 2: Deploy in WildFly
FROM egovio/wildfly:1-helm-fin-e6312078

# Copy the built .ear file to the WildFly deployments directory
#COPY --from=build /app/egov/egov-ear/target/*.ear /opt/jboss/wildfly/standalone/deployments/

# Copy the locally built .ear file from host to container
COPY egov/egov-ear/target/*.ear /opt/jboss/wildfly/standalone/deployments/


# Run WildFly with necessary arguments
USER jboss
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0", "-Ddb.migration.enabled=true", "-Ddev.mode=true", "-Ddb.flyway.validateon.migrate=true"]
