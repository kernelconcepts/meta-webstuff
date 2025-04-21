SUMMARY = "Domoticz is a Home Automation system design to control various devices and receive input from various sensors. "

LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://License.txt;md5=d32239bcb673463ab874e80d47fae504"

DEPENDS = "python3 lua sqlite3 boost curl openssl libusb zlib openzwave mosquitto jsoncpp"

inherit cmake pkgconfig useradd systemd manpages python3targetconfig

PV = "2025.1.16627+git${SRCPV}"

SRCREV = "1eb2e944f05b745c1815062681be3ea5d2352e60"
SRC_URI = "gitsm://github.com/domoticz/domoticz.git;protocol=https;branch=development \
           file://domoticz.service \
          "

S = "${WORKDIR}/git"

EXTRA_OECMAKE = " -DWITH_LIBUSB=YES \
                  -DBOOST_INCLUDEDIR=${STAGING_INCDIR} \
                  -DUSE_STATIC_BOOST=NO \
                  -DOPENSSL_INCLUDE_DIR=${STAGING_INCDIR} \
                  -DOPENSSL_LIBRARIES=${STAGING_LIBDIR} \
                  -DUSE_OPENSSL_STATIC=NO \
                  -DCURL_LIBRARIES=${STAGING_LIBDIR} \
                  -DCURL_INCLUDE_DIR=${STAGING_INCDIR} \
                  -DOPENZWAVE_LIBRARY_DIRS=${STAGING_LIBDIR} \
                  -DOPENZWAVE_INCLUDE_DIRS=${STAGING_INCDIR}/openzwave \
                  -DUSE_STATIC_OPENZWAVE=NO \
                  -DUSE_STATIC_LIBSTDCXX=NO \
                  -DUSE_BUILTIN_MINIZIP=YES \
                  -DUSE_BUILTIN_SQLITE=NO \
                  -DUSE_BUILTIN_MQTT=NO \
                  -DUSE_BUILTIN_JSONCPP=NO \
                  -DUSE_PRECOMPILED_HEADER=NO \
                  -DGIT_SUBMODULE=NO \
"

INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME:${PN} = "domoticz"
INITSCRIPT_PARAMS:${PN} = "defaults 80"

do_compile:append() {
    sed -i 's:USERNAME=.*:USERNAME=domoticz:g' ${S}/domoticz.sh
    sed -i 's:DAEMON=.*:DAEMON=${localstatedir}/lib/domoticz/\$NAME:g' ${S}/domoticz.sh
    sed -i 's:. /lib/init/vars.sh::g' ${S}/domoticz.sh
}

CXXFLAGS:append = " -std=c++17 -flto=jobserver"

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

    # Webserver files, 'wwwroot'
    install -d ${D}${datadir}/${BPN}
    mv ${D}${localstatedir}/lib/domoticz/www ${D}${datadir}/${BPN}

    # Data files and scripts, 'approot'
    # keep them in /var/lib/domoticz

    # Executables
    install -d ${D}${bindir} 
    mv ${D}${localstatedir}/lib/domoticz/domoticz ${D}${bindir}
    # internal update script, disable
    rm -f ${D}${localstatedir}/lib/domoticz/updatedomo

    chown -R domoticz ${D}${localstatedir}/lib

    install -d ${D}${systemd_unitdir}/system
    sed -e s:LIBDIR:${localstatedir}/lib:g \
        -e s:BINDIR:${bindir}:g \
	-e s:/var/lib/domoticz/www:${datadir}/${BPN}/www:g \
        -e s:/var:${localstatedir}:g \
            ${WORKDIR}/domoticz.service > ${D}${systemd_unitdir}/system/domoticz.service
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
