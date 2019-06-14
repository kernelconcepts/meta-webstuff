SUMMARY = "Basic image for a home automation hub and control HMI"

IMAGE_FEATURES += "package-management ssh-server-dropbear splash x11-base"

LICENSE = "MIT"

inherit core-image distro_features_check

# Window manager and session
IMAGE_INSTALL_append += "packagegroup-x11-browser matchbox-wm xf86-input-keyboard chromium-session-localhost"

IMAGE_INSTALL_append += "haveged"
IMAGE_INSTALL_append += "i2c-tools libpython3 mosquitto mosquitto-clients"
#IMAGE_INSTALL_append += "domoticz"
IMAGE_INSTALL_append += "openhab"
