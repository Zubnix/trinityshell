XVFB_COMMAND=Xvfb

function main(){
	#check if Xvfb is running
	checkXvfbRunning;
	#shut down Xvfb
	shutDownXvfb;
}

function checkXvfbRunning(){
	echo "Checking if Xvfb is running...";
	
	if ps ax | grep -v grep | grep $XVFB_COMMAND > /dev/null; then
    	echo "...Xvfb is running. Will try to shut it down now.";
	else
    	echo "...Xvfb is not running. Aborting.";
    	exit 1;
	fi
}

function shutDownXvfb(){
	echo "Trying to shut down Xvfb...";
	killall $XVFB_COMMAND;
	
	if ps ax | grep -v grep | grep $XVFB_COMMAND > /dev/null; then
    	echo "...Xvfb is still running. Aborting.";
    	#TODO do a kill -9 if Xvfb can not be killed gracefully.
    	exit 1;
	else
    	echo "...Xvfb has been shut down.";
	fi 
}

main $*