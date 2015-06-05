#!/bin/sh

SIMPLETOOL_HOME=/usr/local/simpletool
PIDPATH=$SIMPLETOOL_HOME/pid
PIDFILE=$PIDPATH/simpletool.pid
STARTSHELL=$SIMPLETOOL_HOME/java -jar simpletool-0.1.0.jar > simpletool.log &
PID=0

if [ ! -d "$PIDPATH" ]; then
  mkdir $PIDPATH
fi

if [ -e $PIDFILE ]; then
    PID=`cat $PIDFILE`
    if [ "q$PID" != "q" ] && kill -0 $PID 2> /dev/null ; then
	STATUS="SimpleTool (pid $PID) running"
    else
	STATUS="SimpleTool (pid $PID?) not running"
    fi 
    if [ "x" == "x$PID" ]; then
        PID=0
    fi
else
  STATUS="SimpleTool (no pid file) not running"
fi

case "$1" in
    start)
        if [ 0 -ne $PID ]; then
            running=`ps --pid $PID | grep $PID |wc -l`
            if [ $running ]; then
                echo "Server is already running"
                exit 1
            fi

            echo "Server does not appear to be running, but old PID file exists. Removing..."
            rm $PIDFILE
            PID=0
        fi

        echo "Starting SimpleTool Server ... ... ..."
        $STARTSHELL
        sleep 5 
        PID=`cat $PIDFILE`
        echo "SimpleTool Service pid $PID running...."
        echo "done"
        ;;
    stop)
        if [ 0 -eq $PID ]; then
            echo "Server was not running"
            exit 1
        fi

        echo "Attempting to kill process $PID (SimpleTool)"
        kill $PID
        while [ 1 ];
        do
                if ps --pid $PID > /dev/null; then
                        echo -n ".";
                        sleep 1;
                else
                        break;
                fi
        done
        echo "done"
        ;;
    restart)
        $0 stop
        $0 start
        ;;
    status)
        echo $STATUS;
	;;
    *)
        echo "Usage: "`basename $0`" (start|stop|restart|status)"
        exit 1
        ;;
esac

