FROM eclipse-temurin:8-jdk-ubi9-minimal

ENV ADS_VERSION=2.0.0.AM25

ADD https://archive.apache.org/dist/directory/apacheds/dist/${ADS_VERSION}/apacheds-${ADS_VERSION}.tar.gz /tmp/apacheds.tgz

RUN tar -C /opt -xf /tmp/apacheds.tgz && rm /tmp/apacheds.tgz

ENV ADS_INSTANCES=/ads \
    ADS_HOME=/opt/apacheds-${ADS_VERSION}

# The default ports as described at http://directory.apache.org/apacheds/advanced-ug/1.2-network.html
EXPOSE 10389 10636 60088 60464 8080 8443

WORKDIR $ADS_HOME
RUN mkdir $ADS_INSTANCES && cp -r instances/default $ADS_INSTANCES

VOLUME ["/ads"]

ENTRYPOINT ["bin/apacheds.sh", "default" , "run"]
