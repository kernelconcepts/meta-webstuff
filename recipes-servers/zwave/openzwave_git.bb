SUMMARY = "A C++ library to control Z-Wave Networks via a USB Z-Wave Controller."
HOMEPAGE = "http://www.openzwave.com/"

LICENSE = "LGPLv3"
LIC_FILES_CHKSUM = "file://license/lgpl.txt;md5=7be289db0a5cd2c8acf72a8cbd0c15df"

inherit pkgconfig

DEPENDS = "udev coreutils-native libxml2-native"

SRCREV = "b0b09127ed736d7e09104f499a0bade38afb65d7"
# counts git commits since 1.5 tag
PV = "1.4.2886"

SRC_URI = "git://github.com/OpenZWave/open-zwave.git;protocol=https;branch=master"

S = "${WORKDIR}/git"

SECURITY_STRINGFORMAT = ""

EXTRA_OEMAKE = "PREFIX=${prefix} \
                BITBAKE_ENV=1 \
                MACHINE=${TARGET_ARCH} \
                DESTDIR=${D} \
                SYSCONFDIR=${sysconfdir}/openzwave \
                sysconfdir=${sysconfdir}/openzwave \
                pkgconfigdir=${libdir}/pkgconfig "

do_compile() {
	sed -i -e 's:$(PREFIX)/etc:${sysconfdir}:g' ${S}/cpp/build/Makefile
	sed -i -e 's:Werror:Wno-error:g' ${S}/cpp/build/Makefile
	sed -i -e 's,pkgconfigdir ?,pkgconfigdir :,g' ${S}/cpp/build/support.mk
	oe_runmake
}

do_install() {
	oe_runmake install
}

FILES_${PN}-dev += "${bindir}/ozw_config"
