# PiLapse

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
 
