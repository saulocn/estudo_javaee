JBOSS_HOME=/opt/jboss/wildfly
JBOSS_CLI=$JBOSS_HOME/bin/jboss-cli.sh
USER_CLI=$JBOSS_HOME/bin/add-user.sh
JBOSS_MODE=${1:-"standalone"}
JBOSS_CONFIG=${2:-"$JBOSS_MODE-full.xml"}
QUEUE_NAME=PaymentQueue

function wait_for_server() {
  until `$JBOSS_CLI -c ":read-attribute(name=server-state)" 2> /dev/null | grep -q running`; do
    sleep 1
  done
}


echo "=> Starting WildFly server"
$JBOSS_HOME/bin/$JBOSS_MODE.sh -b 0.0.0.0  -c $JBOSS_CONFIG &

echo "=> Waiting for the server to boot"
wait_for_server

$USER_CLI -a -u user123 -p Password123 -g guest

$JBOSS_CLI -c << EOF
batch

/subsystem=messaging-activemq/server=default/remote-acceptor=netty:add(socket-binding=messaging)
/socket-binding-group=standard-sockets/socket-binding=messaging:add(port=5445)

run-batch
EOF


$JBOSS_CLI -c << EOF
batch


jms-queue add --queue-address=$QUEUE_NAME --entries=queue/$QUEUE_NAME java:jboss/exported/jms/queue/$QUEUE_NAME
:reload

run-batch
EOF



rm -rf /opt/jboss/wildfly/standalone/configuration/standalone_xml_history

echo "=> Shutting down WildFly"
if [ "$JBOSS_MODE" = "standalone" ]; then
  $JBOSS_CLI -c ":shutdown"
else
  $JBOSS_CLI -c "/host=*:shutdown"
fi

