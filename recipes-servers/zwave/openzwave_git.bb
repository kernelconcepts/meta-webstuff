SUMMARY = "A C++ library to control Z-Wave Networks via a USB Z-Wave Controller."
HOMEPAGE = "http://www.openzwave.com/"

LICENSE = "LGPL-3.0-or-later"
LIC_FILES_CHKSUM = "file://licenses/lgpl.txt;md5=7be289db0a5cd2c8acf72a8cbd0c15df"

inherit pkgconfig

DEPENDS = "udev coreutils-native libxml2-native"

SRCREV = "3fff11d246a0d558d26110e1db6bd634a1b347c0"
# counts git commits since 1.5 tag
PV = "1.6.1"

SRC_URI = "git://github.com/OpenZWave/open-zwave.git;protocol=https;nobranch=1 \
           file://5cdc891c140a35190ead9464be6965b9c7389aab.patch \
           file://0001-disable-examples.patch \
           "

S = "${WORKDIR}/git"

EXTRA_OEMAKE = "PREFIX=${prefix} \
                BITBAKE_ENV=1 \
                MACHINE=${TARGET_ARCH} \
                DESTDIR=${D} \
                SYSCONFDIR=${sysconfdir}/openzwave \
                sysconfdir=${sysconfdir}/openzwave \
                instlibdir=${libdir} \
                pkgconfigdir=${libdir}/pkgconfig "

do_compile() {
	sed -i -e 's:$(PREFIX)/etc:${sysconfdir}:g' ${S}/cpp/build/Makefile
	sed -i -e 's:Werror:Wno-error:g' ${S}/cpp/build/Makefile
	sed -i -e 's:Werror:Wno-error:g' ${S}/cpp/build/Makefile
	sed -i -e 's:Werror:Wno-error:g' ${S}/cpp/hidapi/configure.ac
	sed -i -e 's,pkgconfigdir ?,pkgconfigdir :,g' ${S}/cpp/build/support.mk
	oe_runmake
}

do_install() {
	oe_runmake install
}

FILES:${PN}-dev += "${bindir}/ozw_config"
