SUMMARY = "Basic image for a headless home automation hub"

IMAGE_FEATURES += "package-management ssh-server-dropbear"

LICENSE = "MIT"

inherit core-image distro_features_check

IMAGE_INSTALL_append += "custom-appstart haveged"
#IMAGE_INSTALL_append += "domoticz"
IMAGE_INSTALL_append += "openhab"
IMAGE_INSTALL_append += "i2c-tools libpython3  mosquitto mosquitto-clients"

# 1000 MiB of additional storage for installable addons and data
IMAGE_ROOTFS_EXTRA_SPACE = "1024000"

# Recommended hardware: 
# - openHAB: >1GB of storage, 2GB of RAM minimum