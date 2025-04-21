SUMMARY = "Domoticz is a Home Automation system design to control various devices and receive input from various sensors. "

LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://License.txt;md5=d32239bcb673463ab874e80d47fae504"

DEPENDS = "lua sqlite3 boost curl openssl libusb zlib openzwave mosquitto lsb"

inherit cmake pkgconfig useradd systemd update-rc.d

PV = "4.10717+git${SRCPV}"

SRCREV = "be79a17d8baebed5fad1c81d6c11ad71f2dc19c3"
SRC_URI = "git://github.com/domoticz/domoticz.git;protocol=https;branch=master \
           file://domoticz.service \
          "

S = "${WORKDIR}/git"

EXTRA_OECMAKE = " -DBOOST_INCLUDEDIR=${STAGING_INCDIR} \
                  -DOPENSSL_INCLUDE_DIR=${STAGING_INCDIR} \
                  -DOPENSSL_LIBRARIES=${STAGING_LIBDIR} \
                  -DCURL_LIBRARIES=${STAGING_LIBDIR} \
                  -DCURL_INCLUDE_DIR=${STAGING_INCDIR} \
                  -DOPENZWAVE_LIBRARY_DIRS=${STAGING_LIBDIR} \
                  -DUSE_STATIC_OPENZWAVE=NO \
                  -DUSE_BUILTIN_MQTT=YES \
                "

INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME:${PN} = "domoticz"
INITSCRIPT_PARAMS:${PN} = "defaults 80"

do_compile:append() {
    sed -i 's:USERNAME=.*:USERNAME=domoticz:g' ${S}/domoticz.sh
    sed -i 's:DAEMON=.*:DAEMON=${localstatedir}/lib/domoticz/\$NAME:g' ${S}/domoticz.sh
    sed -i 's:. /lib/init/vars.sh::g' ${S}/domoticz.sh
}

do_install:append() {
    install -d ${D}/${sysconfdir}/init.d
    install -m755 ${S}/domoticz.sh ${D}/${sysconfdir}/init.d/domoticz

    # The domoticz manual says "run from git checkout", but we don't tolerate such nonsense
    # and since 'make install' doesn't work properly, we do some massaging.
    install -d ${D}/foo
    mv ${D}${prefix}/* ${D}/foo
    install -d ${D}${localstatedir}/lib/domoticz
    mv ${D}/foo/* ${D}${localstatedir}/lib/domoticz
    rmdir ${D}/foo

    chown -R domoticz ${D}${localstatedir}/lib

    install -d ${D}${systemd_unitdir}/system
    sed s:LIBDIR:${localstatedir}/lib:g ${WORKDIR}/domoticz.service > ${D}${systemd_unitdir}/system/domoticz.service

    rmdir ${D}${prefix}
}

FILES:${PN}-dbg += "${localstatedir}/lib/domoticz/.debug/"

SYSTEMD_SERVICE:${PN} = "domoticz.service"

USERADD_PACKAGES = "${PN}"
USERADD_PARAM:${PN} = " \
    --system --no-create-home \
    --home ${localstatedir}/lib/domoticz \
    --groups dialout \
    --user-group domoticz"

# Domoticz is mostly used in combination with a smart meter (ftdi dongles) or an rftrxx (acm based).
RRECOMMENDS:${PN} += "python3 \
                      kernel-module-cdc-acm \
                      kernel-module-usbserial \
                     "
RDEPENDS:${PN} += "lsb"