POSTGRES_DRIVER_VERSION=42.2.12
JBOSS_HOME=/opt/jboss/wildfly
JBOSS_CLI=$JBOSS_HOME/bin/jboss-cli.sh
JBOSS_MODE=${1:-"standalone"}
JBOSS_CONFIG=${2:-"$JBOSS_MODE-full.xml"}

cd /tmp
curl -O https://jdbc.postgresql.org/download/postgresql-$POSTGRES_DRIVER_VERSION.jar

function wait_for_server() {
  until `$JBOSS_CLI -c ":read-attribute(name=server-state)" 2> /dev/null | grep -q running`; do
    sleep 1
  done
}


echo "=> Starting WildFly server"
$JBOSS_HOME/bin/$JBOSS_MODE.sh -b 0.0.0.0 -c $JBOSS_CONFIG &

echo "=> Waiting for the server to boot"
wait_for_server

echo "Configurando JBOS Datasource para ${DATASOURCE_JNDI}"
echo "Datasource name ${DATASOURCE_NAME}"
echo "DB_HOST ${DB_HOST}"
echo "DB_PORT ${DB_PORT}"
echo "DB_NAME ${DB_NAME}"
echo "DB_USER ${DB_USER}"
echo "DB_PASS ${DB_PASS}"


$JBOSS_CLI -c << EOF
batch

module add --name=org.postgres --resources=/tmp/postgresql-$POSTGRES_DRIVER_VERSION.jar --dependencies=javax.api,javax.transaction.api

/subsystem=datasources/jdbc-driver=postgres:add(driver-name="postgres",driver-module-name="org.postgres",driver-class-name=org.postgresql.Driver)

data-source add \
  --jndi-name=$DATASOURCE_JNDI \
  --name=$DATASOURCE_NAME \
  --connection-url=jdbc:postgresql://$DB_HOST:$DB_PORT/$DB_NAME \
  --driver-name=postgres \
  --user-name=$DB_USER \
  --password=$DB_PASS \
  --check-valid-connection-sql="SELECT 1" \
  --background-validation=true \
  --background-validation-millis=60000 \
  --flush-strategy=IdleConnections \
  --min-pool-size=1 --max-pool-size=2  --pool-prefill=false

run-batch
EOF


rm -rf /opt/jboss/wildfly/standalone/configuration/standalone_xml_history

echo "=> Shutting down WildFly"
if [ "$JBOSS_MODE" = "standalone" ]; then
  $JBOSS_CLI -c ":shutdown"
else
  $JBOSS_CLI -c "/host=*:shutdown"
fi