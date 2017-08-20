# PiLapse

PiLapse is a project that uses a Raspberry Pi with a connected camera to capture images for use as timelapses.
It automatically detects when there is motion, and begins to record images. You can set the pace yourself, but the default is two images every minute.

> This project was originally developed to be used as a part of [Svendborg Tekniske Gymnasium](http://svend-es.dk/teknisk-gymnasium-htx/)'s 2017 [robotics contest](https://sites.google.com/svend-es.dk/sit/sit-comp-robotics?authuser=0), where it should make timelapses of the development and building of the courses as well as the competition itself, intended for branding purposes

Below here, you can find all the necessary information on how to get your own PiLapse system up and running.


## Installing Raspbian

> This assumes that you have internet connectivity on your Raspberry Pi

On a regular computer _(not the Pi)_:
 + Download the ZIP file [Raspbian Stretch Lite](https://www.raspberrypi.org/downloads/raspbian/)
 + Dowload [Etcher](https://etcher.io/)
 + Insert the SD card into your computer
 + Open Etcher, and select the downloaded ZIP and select the SD Card
 + Click `Flash!` and wait
 + Once finished, insert the SD Card into your Pi
 
On the Raspbarry Pi:
 + Plugin keyboard and display to the Pi, and power it up!
 + The default login credentials are: 
     + `raspberrypi login / username: pi`
     + `password: raspberry` Note that the password is completely hidden while typing
 
 
## Configuring Raspbian
### Enable Autologin
 + Type `sudo raspi-config` and navigate by the arrow keys to `3 Boot Options` and hit `Enter`
 + Select `B1 Desktop / CLI` and hit `Enter`
 + Select `B2 Console Autologin` and hit `Enter`
 
### Change Locale
While still in the `raspi-config` menu
 + Select `4 Localisation Options` and hit `Enter`
 + Select `I1 Change Locale` and hit `Enter`
 + Navigate down to `en_DK ISO-8859-1` and hit `Enter`
 + Select `en_GB.UTF-8` and hit `Enter`
 + Wait, this may take a while
 
### Change Timezone
While still in the `raspi-config` menu
 + Select `4 Localisation Options` and hit `Enter`
 + Select `I2 Change Timezone` and hit `Enter`
 + Select `Europe` and hit `Enter`
 + Select `Copenhagen` and hit `Enter`
 
### Enable Camera
While still in the `raspi-config` menu
 + Select `5 Interfacing Options` and hit `Enter`
 + Select `P1 Camera` and hit `Enter`
 + Select `Yes` and hit `Enter`
 
### Enable SSH
While still in the `raspi-config` menu
 + Select `5 Interfacing Options` and hit `Enter`
 + Select `P2 SSH` and hit `Enter`
 + Select `Yes` and hit `Enter`
 
### Expand Filesystem
While still in the `raspi-config` menu
 + Select `7 Advanced Options` and hit `Enter`
 + Select `A1 Expand Filesystem` and hit `Enter`
 + This may take some time...
 
### Reboot to commit changes
 + Click `Rightarrow key` twice to select `Finish` and hit `Enter`
 + Hit `Enter` to reboot now
 + Alternatively, you can reboot manually by typing `sudo reboot now`
 + The next boot will take longer than usual
 
> https://raspberrypi.stackexchange.com/questions/8734/execute-script-on-start-up


## Installing Required Components

### Installing Java
To install Oracle Java 8 JDK:
 + Type `sudo apt-get install oracle-java8.jdk` and hit `Enter`
 + When prompted, type `Y` and hit `Enter` to confirm
 + Wait...
 + Once it has finished, verify the installation by typing `java -version`, hit `Enter` and check the output
 
### Installing Apache webserver
To install Apache 2 webserver:
 + Type `sudo apt-get install apache2` and hit `Enter`
 + When prompted, type `Y` and hit `Enter` to confirm
 + Wait...
 + Once finished, you can verify the installation, if you want, by doing:
     + Determine the Pi's IP-address by typing `hostname -I` and hit `Enter` on the Pi
     + Type in the IP-address of the Pi, as an URL in your webbrowser on your regular computer (requires you to be on the same local network as the Pi)
     + If you see an ugly webpage named `Apache2 Debian Default Page`, that says `It works!`, then it, well, works

## SFTP
As SSH is already enabled on the Pi, you just need a client on your regular computer to access the filesystem of the Pi remotely. Several different clients exists, but here is instructions for using FileZilla:
 + Download and install [FileZilla Client](https://filezilla-project.org/download.php?type=client) on your regular computer
 + Once successfully installed, go to `File -> Site Manager...` and click `New Site`
 + Type in the IP-address of the Pi as `Host` and leave `Port` blank
     + You can determine the Pi's IP-address by typing `hostname -I` and hit `Enter` on the Pi
 + Set `Logon Type` to `Normal`
 + `User` is `pi`
 + `Password` is `raspberry`, unless you have actively changed it
 + Click `Connect` and wait
 + You should now see your own computer's filesystem to the bottom left and the Pi's filesystem to the rigth
     + Navigate into folders by double-clicking on them, and go back 'up' the filetree by doubleclicking on `..`
     + Copy a file from either computer by double-clicking on the file, and it will automatically be copied to the other machine. It will be copied into the currently shown folder on the other machine. This works both ways

