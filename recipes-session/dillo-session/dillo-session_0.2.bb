DESCRIPTION = "Epiphany X session files for Yocto/Poky"
HOMEPAGE = "http://www.yoctoproject.org"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

RDEPENDS_${PN} += "sudo dillo"

SECTION = "x11"

SRC_URI = "file://dillo-session \
           file://change_browser_url.sh \
          "

inherit update-alternatives

BROWSER="dillo"
USER="root"

ALTERNATIVE_${PN} = "x-session-manager"
ALTERNATIVE_TARGET[x-session-manager] = "${bindir}/dillo-session"
ALTERNATIVE_PRIORITY = "90"

do_install() {
        install -d ${D}/${bindir}
        install -m 0755 ${WORKDIR}/dillo-session ${D}/${bindir}
        install -m 0755 ${WORKDIR}/change_browser_url.sh ${D}/${bindir}
}
