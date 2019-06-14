DESCRIPTION = "openHAB2 home automation recipe. An Internet connection is 
 recommended in order to install addons."
HOMEPAGE = "http://www.openhab.org"
LICENSE = "EPL-1.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/EPL-1.0;md5=57f8d5e2b3e98ac6e088986c12bf94e6"
PR = "r0"

DEPENDS += "screen"

RDEPENDS_${PN} += "java2-runtime screen zip"

SRC_URI = "https://dl.bintray.com/openhab/mvn/org/openhab/distro/${PN}/${PV}/${P}.tar.gz \
           file://init"

SRC_URI[md5sum] = "69b08685bc1dd04c1155c5eca0e4bcf3"
SRC_URI[sha256sum] = "22f68dedf148742079cca9a86430dc99cce70f17919a0e26231db38db6009cb2"

S = "${WORKDIR}"

# Add autostart ability
inherit autotools update-rc.d
INITSCRIPT_NAME = "openhab"
INITSCRIPT_PARAMS = "defaults"

do_compile () {
}

do_install() {
	# Simply copy folders to datadir
	install -d ${D}/${datadir}/openhab-runtime
	cd ${S}
        cp -a runtime *sh userdata addons conf ${D}/${datadir}/openhab-runtime

	# Add init script to allow autostart
	install -d ${D}/${sysconfdir}/init.d
	install -m 0755 ${S}/init ${D}/${sysconfdir}/init.d/openhab
}

FILES_${PN} = "${datadir} ${sysconfdir}/init.d/openhab"
