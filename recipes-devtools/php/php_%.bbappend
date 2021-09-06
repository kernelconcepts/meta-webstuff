FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI_append = " file://www.conf"

DEPENDS += "gd curl"
RDEPENDS_${PN} += "openssl"
PACKAGECONFIG = "soap sqlite3 opcache openssl  zip \
                 ${@bb.utils.filter('DISTRO_FEATURES', 'ipv6 pam', d)}"
EXTRA_OECONF += "--enable-gd --with-curl=${STAGING_LIBDIR}/.."

do_install_append () {
	install -d ${D}/${sysconfdir}/php-fpm.d/
	install -m644 ${WORKDIR}/www.conf ${D}/${sysconfdir}/php-fpm.d/www.conf
}

FILES_${PN}-fpm += "${sysconfdir}/php-fpm.d/www.conf"
