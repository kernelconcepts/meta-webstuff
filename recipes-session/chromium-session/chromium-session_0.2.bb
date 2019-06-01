DESCRIPTION = "Chromium X session files for Yocto/Poky"
HOMEPAGE = "http://www.yoctoproject.org"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

RDEPENDS_${PN} += "sudo chromium-x11"

SECTION = "x11"

SRC_URI = "file://chromium-session \
          "

inherit update-alternatives

BROWSER="chromium"
USER="root"

ALTERNATIVE_${PN} = "x-session-manager"
ALTERNATIVE_TARGET[x-session-manager] = "${bindir}/chromium-session"
ALTERNATIVE_PRIORITY = "90"

do_install() {
        install -d ${D}/${bindir}
        install -m 0755 ${WORKDIR}/chromium-session ${D}/${bindir}
}
