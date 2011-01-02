#!/bin/sh
PRG="$0"
while [ -h "$PRG" ] ; do
ls=`ls -ld "$PRG"`
link=`expr "$ls" : '.*-> \(.*\)$'`
if expr "$link" : '/.*' > /dev/null; then
PRG="$link"
else
PRG=`dirname "$PRG"`/"$link"
fi
done

PRGDIR=`dirname "$PRG"`
DBUTIL_HOME=`cd "$PRGDIR/.." ; pwd`

EXECUTABLE=startDbutil.sh
# Check that target executable exists
if [ ! -x "$DBUTIL_HOME"/bin/"$EXECUTABLE" ]; then
echo "Cannot find $DBUTIL_HOME/bin/$EXECUTABLE"
echo "This file is needed to run this program"
exit 1
fi
#change the path to the install path.
DBUTIL_CLASSPATH=
for i in `ls ${DBUTIL_HOME}/lib/*.jar`
do
  DBUTIL_CLASSPATH=${DBUTIL_CLASSPATH}:${i}
done

echo $DBUTIL_CLASSPATH

java -DDBUTIL_HOME=${DBUTIL_HOME} -Xmx512M -Xms150M -cp ${DBUTIL_CLASSPATH} org.wsm.database.tools.StartMe
