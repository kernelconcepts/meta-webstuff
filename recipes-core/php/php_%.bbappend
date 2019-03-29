FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI_append = " file://www.conf"

DEPENDS += "gd curl libxpm"
RDEPENDS_${PN} += "openssl"
PACKAGECONFIG = "soap sqlite3 opcache openssl \
                 ${@bb.utils.filter('DISTRO_FEATURES', 'ipv6 pam', d)}"
EXTRA_OECONF += "--with-gd=${STAGING_LIBDIR}/.. --with-xpm-dir=no --with-curl=${STAGING_LIBDIR}/.."

do_configure_append () {
	sed -i 's/#define HAVE_GD_XPM 1/#undef HAVE_GD_XPM/g' ${B}/main/php_config.h
}

do_install_append () {
	install -d ${D}/${sysconfdir}/php-fpm.d/
	install -m644 ${WORKDIR}/www.conf ${D}/${sysconfdir}/php-fpm.d/www.conf
}

FILES_${PN}-fpm += "${sysconfdir}/php-fpm.d/www.conf"
