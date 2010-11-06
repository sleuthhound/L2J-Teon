#!/bin/bash

./LoginServer_loop.sh &
sleep 1
tail -f log/stdout.log
