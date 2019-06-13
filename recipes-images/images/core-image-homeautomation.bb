SUMMARY = "Basic image for a headless home automation hub"

IMAGE_FEATURES += "package-management ssh-server-dropbear"

LICENSE = "MIT"

inherit core-image distro_features_check

IMAGE_INSTALL_append += "custom-appstart"
IMAGE_INSTALL_append += "haveged"
IMAGE_INSTALL_append += "i2c-tools libpython3 domoticz mosquitto mosquitto-clients"
