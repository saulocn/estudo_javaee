JBOSS_HOME=/opt/jboss/wildfly
JBOSS_CLI=$JBOSS_HOME/bin/jboss-cli.sh
JBOSS_MODE=${1:-"standalone"}
JBOSS_CONFIG=${2:-"$JBOSS_MODE-full.xml"}
QUEUE_NAME=PaymentQueue
CONNECTION_FACTORY=PaymentQueueCF
JMS_USER=user123
JMS_PASSWORD=Password123
JMS_HOST=queue-jee
JMS_PORT=5445
OUTBOUND_BINDING=remote-artemis

cd /tmp

function wait_for_server() {
  until `$JBOSS_CLI -c ":read-attribute(name=server-state)" 2> /dev/null | grep -q running`; do
    sleep 1
  done
}


echo "=> Starting WildFly server"
$JBOSS_HOME/bin/$JBOSS_MODE.sh -b 0.0.0.0 -c $JBOSS_CONFIG &

echo "=> Waiting for the server to boot"
wait_for_server



$JBOSS_CLI -c << EOF
batch


/socket-binding-group=standard-sockets/remote-destination-outbound-socket-binding=$OUTBOUND_BINDING:add(host=$JMS_HOST, port=$JMS_PORT)

/subsystem=messaging-activemq/server=default/remote-connector=$OUTBOUND_BINDING:add(socket-binding=$OUTBOUND_BINDING) 

/subsystem=messaging-activemq/server=default/pooled-connection-factory=$OUTBOUND_BINDING:add(connectors=[$OUTBOUND_BINDING], entries=[java:/jms/$CONNECTION_FACTORY],user="$JMS_USER", password="$JMS_PASSWORD")

jms-queue add --queue-address=$QUEUE_NAME --entries=[queue/$QUEUE_NAME,java:jboss/exported/jms/queue/$QUEUE_NAME]

run-batch
EOF


rm -rf /opt/jboss/wildfly/standalone/configuration/standalone_xml_history

echo "=> Shutting down WildFly"
if [ "$JBOSS_MODE" = "standalone" ]; then
  $JBOSS_CLI -c ":shutdown"
else
  $JBOSS_CLI -c "/host=*:shutdown"
fi