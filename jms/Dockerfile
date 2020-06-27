FROM jboss/wildfly

COPY --chown=1001:0  install_queue.sh  /tmp/


user root
RUN chmod +x /tmp/install_queue.sh
RUN echo 'hosts: files mdns4_minimal [NOTFOUND=return] dns mdns4' >> /etc/nsswitch.conf

user jboss
RUN /opt/jboss/wildfly/bin/add-user.sh admin Admin#007 --silent
RUN chown -R jboss:jboss /opt/jboss/wildfly/
RUN /tmp/install_queue.sh

CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-c", "standalone-full.xml", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]

