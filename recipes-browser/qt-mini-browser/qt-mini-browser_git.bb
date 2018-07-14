SUMMARY = "Qt5 Simple Qt/QML based webkit browser"
LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://${WORKDIR}/git/LICENSE;md5=5b4473596678d62d9d83096273422c8c"

PV = "1.0.0+gitr${SRCPV}"

DEPENDS = "qtbase qtwebkit qtgraphicaleffects qtquickcontrols"
RDEPENDS_${PN} += "qtwebkit-qmlplugins qtquickcontrols qtquickcontrols-qmlplugins qtgraphicaleffects-qmlplugins"
RDEPENDS_${PN} += "${@bb.utils.contains('PACKAGECONFIG_OPENSSL', 'openssl', 'ca-certificates', '', d)}"


SRC_URI = "git://github.com/kernelconcepts/${PN}.git"
SRCREV = "510f9574866b0dcd78d46f68af7e8de32e776d34"
S = "${WORKDIR}/git"

inherit qmake5

