# SSH remote control for android

## About
This android application is a simple remote control for your linux server.
There is only two commands :
* Power on
* Power off

## Requirement

### Server side
* The server mother board must have the wake on lan function.
* An ssh server
* A user with sudo rights

### Library
To build the application you will need :
* [JSch](http://www.jcraft.com/jsch/)
* [JZlib](http://www.jcraft.com/jzlib/)

##How it works?
I think you already guessed with the requirements.

To power up your server you send a wake on lan packet.

To power it down your application connect with ssh and run the poweroff command.

##How to use it?
In a setting menu you have to set the mac address of your server and the brodcast ip.

You can get it with a:
>ifconfig

You will also need to set the host, user and password for the ssh connection

## Special thank
Thanks to [jibble.org](http://www.jibble.org).
I used there piece of code for the wake on lan part.
