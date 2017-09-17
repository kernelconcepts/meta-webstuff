SUMMARY = "Radicale *DAV Server"
DESCRIPTION = "A Free and Open-Source CalDAV and CardDAV Server"
HOMEPAGE="http://www.radicale.org/"
LICENSE = "GPLv3"

DEPENDS = "python3 openssl"

inherit pypi setuptools3 useradd update-rc.d

LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"
PYPI_PACKAGE = "Radicale"
SRC_URI += "file://radicale-init"

SRC_URI[md5sum] = "4d952dde77e96fe24902b1c4049698d6"
SRC_URI[sha256sum] = "63a57ffcc78fc8121b35829496d79c43ff531f847c551dc930a07cf57068a796"

USERADD_PACKAGES = "${PN}"
GROUPADD_PARAM_${PN} = "-f -r radicale"
USERADD_PARAM_${PN} =  "--system -r -g radicale -d ${datadir}/radicale/ -s ${sbindir}/nologin -c \"Radicale\" radicale"

INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME_${PN} = "radicale"
INITSCRIPT_PARAMS_${PN} = "defaults 80"

RDEPENDS_${PN} += "${PYTHON_PN}-misc ${PYTHON_PN}-pprint ${PYTHON_PN}-selectors\
                   ${PYTHON_PN}-enum ${PYTHON_PN}-threading ${PYTHON_PN}-shell\
                   ${PYTHON_PN}-xml ${PYTHON_PN}-subprocess ${PYTHON_PN}-vobject\
                   ${PYTHON_PN}-json  ${PYTHON_PN}-setuptools"

FILES_${PN} += "${datadir}"

do_install_append () {
	install -d ${D}${datadir}/radicale/
	install -m755 radicale.wsgi ${D}${datadir}/radicale/
	install -m755 radicale.fcgi ${D}${datadir}/radicale/
	install -d ${D}${sysconfdir}/init.d
	install -m 0755 ${WORKDIR}/radicale-init ${D}${sysconfdir}/init.d/radicale
}

do_pkg_postinst_append () {
        mkdir -p /var/lib/radicale/collections && chown -R radicale:radicale /var/lib/radicale/collections 
        chmod -R o-r /var/lib/radicale/collections
}
