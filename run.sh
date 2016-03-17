#!/bin/sh

if [ -z "$IRUTILS_HOME" ]
then
   IRUTILS_HOME=.
fi

java -Xmx4g -Djava.library.path=/usr/local/lib/ -cp "target/Lib2Data-1.0-SNAPSHOT.jar:target/lib/*" $@
