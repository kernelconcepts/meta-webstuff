DESCRIPTION = "Lightweight FLTK browser, with support for SSL, tabs and much more..."
HOMEPAGE = "http://www.dillo.org"
SECTION = "x11/network"
PRIORITY = "optional"
LICENSE = "GPLv3"

LIC_FILES_CHKSUM = "file://${S}/COPYING;md5=d32239bcb673463ab874e80d47fae504"

DEPENDS = "fltk libpng openssl"

SRC_URI="http://www.dillo.org/download/dillo-${PV}.tar.bz2 \
         file://dillo.desktop \
         file://dillo.png"

SRC_URI[md5sum] = "554aad93b6107bba696f4da022c41561"
SRC_URI[sha256sum] = "db1be16c1c5842ebe07b419aa7c6ef11a45603a75df2877f99635f4f8345148b"

inherit autotools pkgconfig

FILES_${PN} += " ${libdir}/dillo/ ${bindir}/dpi* "
FILES_${PN}-dbg += " ${libdir}/dillo/dpi/*/.debug/"


EXTRA_OECONF = "--enable-ipv6 --enable-ssl"

do_configure() {
	sed -i 's:-I/usr/local/include::g' ${S}/configure*
	sed -i 's:-L/usr/local/lib::g' ${S}/configure*
	PNG_CONFIG=pkg-config oe_runconf
}

do_compile() {
	PNG_CONFIG=pkg-config oe_runmake
}

do_install_append() {
        install -d ${D}${datadir}/applications
        install -d ${D}${datadir}/pixmaps
        install -m 0644 ${WORKDIR}/dillo.desktop ${D}${datadir}/applications/dillo.desktop
        install -m 0644 ${WORKDIR}/dillo.png ${D}${datadir}/pixmaps/dillo.png
}
