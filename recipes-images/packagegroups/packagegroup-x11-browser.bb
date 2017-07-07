SUMMARY = "Basic X11 packageset for minimal web browser support"
DESCRIPTION = "Packages required to set up a basic working X11 session that incorporates a web browser"
LICENSE = "MIT"

inherit packagegroup

RDEPENDS_${PN} = "\
    matchbox-keyboard   \
    matchbox-keyboard-im    \
    liberation-fonts \
    "
