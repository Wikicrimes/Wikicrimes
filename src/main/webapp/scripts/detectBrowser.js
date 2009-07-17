<!--
/* OS / Browser Detect. Written by Joe McCormack. www.virtualsecrets.com */
var net_browser = 0;
function wbrowsertype() {
	/*
	__ net_browser flag values based on OS/Browser __
	0 = Undetermined OS / Browser
	17 = Undetermined OS / Browser
	MAC OS:
	2 = Unknown Browser
	3 = Internet Explorer
	4 = Safari
	5 = Firefox
	6 = Netscape
	7 = Opera
	8 = Camino
	9 = Firebird
	26 = Google Chrome
	WINDOWS OS:
	10 = Unknown Browser
	11 = Internet Explorer
	12 = Firefox
	13 = Netscape
	14 = Opera
	15 = Camino
	16 = Firebird
	25 = Google Chrome
	UNKNOWN OS:
	17 = Unknown Browser
	18 = Safari
	19 = Internet Explorer
	20 = Firefox
	21 = Netscape
	22 = Opera
	23 = Camino
	24 = Firebird
	27 = Google Chrome
	*/
	var btfound = 0; browser_detect = navigator.userAgent.toLowerCase();
	if ((browser_detect.indexOf("konqueror") + 1)) { btfound = 1; net_browser = 1; }
	if ((browser_detect.indexOf("mac_powerpc") + 1)) { btfound = 1; net_browser = 3; }
	if (btfound == 0) {
	// MAC OS
	if ((browser_detect.indexOf("macintosh") + 1)) {
	if ((browser_detect.indexOf("safari") + 1)) { btfound = 1; net_browser = 4; }
	else if ((browser_detect.indexOf("firefox") + 1)) { btfound = 1; net_browser = 5; }
	else if ((browser_detect.indexOf("netscape") + 1)) { btfound = 1; net_browser = 6; }
	else if ((browser_detect.indexOf("opera") + 1)) { btfound = 1; net_browser = 7; }
	else if ((browser_detect.indexOf("camino") + 1)) { btfound = 1; net_browser = 8; }
	else if ((browser_detect.indexOf("firebird") + 1)) { btfound = 1; net_browser = 9; }
	else if ((browser_detect.indexOf("chrome") + 1)) { btfound = 1; net_browser = 26; }
	else { btfound = 1; net_browser = 2; }
	}
	// Windows OS
	if ((browser_detect.indexOf("windows") + 1) && btfound == 0) {
	if ((browser_detect.indexOf("msie") + 1)) { btfound = 1; net_browser = 11; }
	else if ((browser_detect.indexOf("firefox") + 1)) { btfound = 1; net_browser = 12; }
	else if ((browser_detect.indexOf("netscape") + 1)) { btfound = 1; net_browser = 13; }
	else if ((browser_detect.indexOf("opera") + 1)) { btfound = 1; net_browser = 14; }
	else if ((browser_detect.indexOf("camino") + 1)) { btfound = 1; net_browser = 15; }
	else if ((browser_detect.indexOf("firebird") + 1)) { btfound = 1; net_browser = 16; }
	else if ((browser_detect.indexOf("chrome") + 1)) { btfound = 1; net_browser = 25; }
	else { btfound = 1; net_browser = 10; }
	}
	// Unknown OS
	if (btfound == 0) {
	if ((browser_detect.indexOf("safari") + 1)) { net_browser = 18; }
	else if ((browser_detect.indexOf("msie") + 1)) { net_browser = 19; }
	else if ((browser_detect.indexOf("firefox") + 1)) { net_browser = 20; }
	else if ((browser_detect.indexOf("netscape") + 1)) { net_browser = 21; }
	else if ((browser_detect.indexOf("opera") + 1)) { net_browser = 22; }
	else if ((browser_detect.indexOf("camino") + 1)) { net_browser = 23; }
	else if ((browser_detect.indexOf("firebird") + 1)) { net_browser = 24; }
	else if ((browser_detect.indexOf("chrome") + 1)) { net_browser = 27; }
	else { net_browser = 17; }
	}
	}
	/* In most cases, Google Chrome will behave the same as Firefox. If not you can remove these value overwrites. */
	if (net_browser == 25) { net_browser = 12; }
	else if (net_browser == 26) { net_browser = 5; }
	else if (net_browser == 27) { net_browser = 20; }
}
wbrowsertype();
//-->
