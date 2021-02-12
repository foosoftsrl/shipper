#!/bin/sh
set -e
APPUSER=shipper
LOGDIR=/var/log/shipper
DATADIR=/var/lib/shipper

function reloadSystemd {
	which systemctl &>/dev/null
	if [[ $? -eq 0 ]]; then
		echo "Reloading systemd"
		systemctl daemon-reload
	fi
}

if id "$APPUSER" >/dev/null 2>&1; then
        echo "User $APPUSER already exists with uid $(id -u $APPUSER) gid $(id -g $APPUSER)"
else
        echo "Creating a system user $APPUSER"
        useradd -r -s /sbin/nologin $APPUSER
        echo "Created user $APPUSER with uid $(id -u $APPUSER) gid $(id -g $APPUSER)"
fi

echo "Creating log directory $LOGDIR" 
mkdir -p $LOGDIR
chown $APPUSER:$APPUSER $LOGDIR
mkdir -p $DATADIR
chown $APPUSER:$APPUSER $DATADIR

reloadSystemd

