SUMMARY = "Nexcloud Server"
HOMEPAGE = "http://www.nextcloud.com"
LICENSE = "AGPLv3"

NEXTCLOUD_DATA_DIR ?= "${localstatedir}/nextcloud/data"

RDEPENDS_${PN} += "sqlite3 php php-cgi php-cli wget"

PACKAGECONFIG ?= "nginx"
PACKAGECONFIG[apache] = ",,,apache2 php-modphp"
PACKAGECONFIG[nginx] = ",,,nginx php-fpm"

LIC_FILES_CHKSUM = "file://COPYING;md5=73f1eb20517c55bf9493b7dd6e480788"

SRC_URI = "https://download.nextcloud.com/server/releases/${P}.tar.bz2 \
           file://config.php.in \
           file://apache.conf \
           file://nginx.conf \
	"
SRC_URI[md5sum] = "58334a961ed87f4267b1e11ce353aea7"
SRC_URI[sha256sum] = "a80ce586e9e930b2fba69707311e575346cd4dc4402e84678c730f9930d78aee"

S = "${WORKDIR}/${PN}"

inherit allarch

FILES_${PN} += "${localstatedir}/*"

do_install() {
    install -d ${D}${localstatedir}/www/${BPN}
    cp -R --no-dereference --preserve=mode,links -v * ${D}${localstatedir}/www/${BPN}/
    # remove patches
    rm -rf ${D}${localstatedir}/www/${BPN}/patches
    # remove some bloat
    rm -rf ${D}${localstatedir}/www/${BPN}/core/skeleton/*.mp4
    rm -rf ${D}${localstatedir}/www/${BPN}/core/skeleton/Photos/*

    # webserver configs
    install -d ${D}${sysconfdir}/apache2/conf.d
    install -m 0644 ${WORKDIR}/apache.conf ${D}${sysconfdir}/apache2/conf.d/nextcloud.conf

    install -d ${D}${sysconfdir}/nginx/sites-enabled
    install -m 0644 ${WORKDIR}/nginx.conf ${D}${sysconfdir}/nginx/sites-enabled/nextcloud.conf

    # generate config
    install -m 0640 ${WORKDIR}/config.php.in ${D}${localstatedir}/www/${BPN}/config/config.php
    sed -i 's:$MACHINE:${MACHINE}:g' ${D}${localstatedir}/www/${BPN}/config/config.php
    sed -i 's:$DATADIR:${NEXTCLOUD_DATA_DIR}:g' ${D}${localstatedir}/www/${BPN}/config/config.php
    sed -i 's:$MACHINE:${MACHINE}:g' ${D}${sysconfdir}/apache2/conf.d/nextcloud.conf
    sed -i 's:$MACHINE:${MACHINE}:g' ${D}${sysconfdir}/nginx/sites-enabled/nextcloud.conf
}

pkg_postinst_ontarget_${PN} () {
    # fix permissions
    chown -R daemon:daemon $D${localstatedir}/www/${BPN}

    # create
    mkdir -p ${NEXTCLOUD_DATA_DIR}
    chown daemon:daemon ${NEXTCLOUD_DATA_DIR}
    chmod 750 ${NEXTCLOUD_DATA_DIR}
}
