SUMMARY = "Basic image for a home automation hub and control HMI"

IMAGE_FEATURES += "package-management ssh-server-dropbear splash x11-base"

LICENSE = "MIT"

inherit core-image

# Window manager and session
IMAGE_INSTALL += "packagegroup-x11-browser matchbox-wm xf86-input-keyboard chromium-session-localhost"

IMAGE_INSTALL += "haveged"
IMAGE_INSTALL += "i2c-tools libpython3 mosquitto mosquitto-clients"
#IMAGE_INSTALL += "domoticz"
IMAGE_INSTALL += "openhab"

# 1000 MiB of additional storage for installable addons and data
IMAGE_ROOTFS_EXTRA_SPACE = "1024000"

# Recommended hardware: 
# - openHAB: >1GB of storage, 2GB of RAM minimum
