FROM amazoncorretto:17
RUN    yum update -y && yum install -y unzip
RUN     curl -f -o /tmp/gradle-8.1.1.zip -fSL "https://services.gradle.org/distributions/gradle-8.1.1-bin.zip" \
            && unzip /tmp/gradle-8.1.1.zip -d /opt \
            && rm /tmp/gradle-8.1.1.zip \
            && ln -s /opt/gradle-8.1.1/bin/gradle /usr/bin/gradle

WORKDIR /srv
COPY    build.gradle settings.gradle /srv/

RUN     gradle clean

COPY    src /srv/src
RUN     gradle assemble \
          && mv build/libs/*.jar app.jar

CMD     ["java", "-jar", "app.jar"]