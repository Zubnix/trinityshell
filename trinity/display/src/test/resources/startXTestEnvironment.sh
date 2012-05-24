XVFB_COMMAND=Xvfb

function main(){
	#check if not running as root
	checkRoot;
	#check if Xvfb is installed
	checkXvfbCommand;
	#check if DISPLAY variable is set.
	checkDisplay;
	#launch Xvfb
	echo "Starting Xvfb."
	$XVFB_COMMAND -ac $DISPLAY;
}

function checkDisplay(){
	echo "Checking if DISPLAY variable is set...";
	if [[ -n ${DISPLAY} ]]; then
		echo "...DISPLAY variable is set. Good.";
	else
		echo "...DISPLAY variable is not set. Aborting";
		exit 1;
	fi
}

function checkRoot(){
	echo "Checking if user is not root...";
	if [[ $EUID -ne 0 ]]; then
   		echo "...user is not root. Good";
	else
		echo "...this script must not be run as root. Aborting.";
		exit 1;
	fi
}

function checkXvfbCommand() {
	echo "Checking if Xvfb is installed...";
	if command -v $XVFB_COMMAND &>/dev/null; then
		echo "...Xvfb is installed. Good.";
	else
		echo "...Xvfb required but it's not installed.  Aborting.";
		exit 1;
	fi
}

main $*