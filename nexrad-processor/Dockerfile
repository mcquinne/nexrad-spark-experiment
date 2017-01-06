FROM gettyimages/spark:2.0.2-hadoop-2.7

MAINTAINER "Evan McQuinn <mcquinne@gmail.com>"

RUN apt-get update && apt-get -y install openssh-server
RUN mkdir /var/run/sshd

COPY build/libs/nexrad-processor-0.1-all.jar /opt/
COPY submit.sh /bin/

CMD ["/usr/sbin/sshd", "-D"]
