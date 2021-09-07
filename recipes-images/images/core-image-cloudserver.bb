SUMMARY = "Basic image for a cloud server"

IMAGE_FEATURES += "package-management ssh-server-dropbear"

LICENSE = "MIT"

inherit core-image features_check

IMAGE_INSTALL_append += "nextcloud custom-appstart \
                         joe openssh-sftp-server \
                         strace i2c-tools"

# 200 MiB of additional storage for installable addons and data
IMAGE_ROOTFS_EXTRA_SPACE = "200000"

# TODO: reduce memory and cpu footprint
# test nginx for web services
