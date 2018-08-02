#!/bin/sh
echo "output"
echo 700 > /sys/class/gpio/export
echo "\n DONE"
echo 1 > /sys/class/gpio/gpio700/value
sleep 2
echo 0 > /sys/class/gpio/gpio700/value
sleep 2
echo 1 > /sys/class/gpio/gpio701/value
sleep 2
echo 0 > /sys/class/gpio/gpio701/value

setprop script.finish 1



