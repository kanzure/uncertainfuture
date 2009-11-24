package org.singinst.uf.model;

import java.util.ArrayList;
import java.util.Collection;

import org.singinst.uf.math.SimplePoint;

public class MooresLawData {

	private static MooresLawData instance;

	public static MooresLawData getInstance() {
		if (instance == null) {
			instance = new MooresLawData();
		}
		return instance;
	}

	public MooresLawData() {
		points = new ArrayList<SimplePoint>();
		addPoint(1950, 0.00060, "SEAC");
		addPoint(1951, 0.00077, "UNIVAC_I");
		addPoint(1952, 0.00001, "Zuse-5");
		addPoint(1952, 0.00225, "IBM_CPC");
		addPoint(1953, 0.00062, "IBM_650");
		addPoint(1954, 0.00044, "EDVAC");
		addPoint(1955, 0.04476, "Whirlwind");
		addPoint(1955, 0.00346, "IBM_704");
		addPoint(1956, 0.00184, "Librascope_LGP-30");
		addPoint(1959, 0.01521, "IBM_7090");
		addPoint(1960, 0.00074, "IBM_1620");
		addPoint(1960, 0.11821, "DEC_PDP-1");
		addPoint(1961, 0.04032, "Atlas");
		addPoint(1962, 0.01444, "Burroughs_5000");
		addPoint(1963, 0.01665, "IBM_7040");
		addPoint(1963, 0.01586, "Honeywell_1800");
		addPoint(1964, 0.0845, "DEC_PDP-6");
		addPoint(1964, 0.2628, "CDC_6600");
		addPoint(1965, 0.456, "IBM_1130");
		addPoint(1966, 0.07925, "IBM_360/75");
		addPoint(1967, 0.06655, "IBM_360/65");
		addPoint(1968, 0.22008, "DEC_PDP-10");
		addPoint(1969, 0.45489, "CDC_7600");
		addPoint(1969, 2.73651, "DG_Nova");
		addPoint(1970, 0.06068, "GE-635");
		addPoint(1971, 0.20475, "SDS_920");
		addPoint(1972, 0.43683, "IBM_360/195");
		addPoint(1972, 1.2625, "Honeywell_700");
		addPoint(1973, 9.06353, "Prime_Computer_100");
		addPoint(1974, 1.05672, "IBM-370/168");
		addPoint(1974, 4.76, "MITS_Altair");
		addPoint(1975, 2.4346, "DG_Eclipse");
		addPoint(1975, 1.1914, "DEC-KL-10");
		addPoint(1976, 0.73067, "DEC_PDP-11/70");
		addPoint(1976, 4.11, "Cray-1");
		addPoint(1977, 4.49231, "Apple_II");
		addPoint(1977, 1.46, "DEC_VAX_11/780");
		addPoint(1977, 5.84, "TRS-80");
		addPoint(1977, 11.68, "Commodore_PET");
		addPoint(1978, 4.71, "CDC_IPL");
		addPoint(1979, 2.45, "Nanodata_VMX200");
		addPoint(1980, 13.23333, "TRS-80_M3");
		addPoint(1980, 6.40493, "Sun-1");
		addPoint(1981, 3.5624, "CDC_Cyber-205");
		addPoint(1981, 62.79570, "Vic_20");
		addPoint(1982, 31.62, "IBM_PC");
		addPoint(1982, 17.22825, "Sun-2");
		addPoint(1982, 186, "Commodore_64");
		addPoint(1983, 96, "TRS-80_M4");
		addPoint(1983, 7.6704, "Vax_11/750");
		addPoint(1984, 104.208, "Macintosh-128K");
		addPoint(1984, 5.6613, "Vax_11/785");
		addPoint(1985, 42.7656, "Cray-2");
		addPoint(1985, 67.63910, "L.Edge_XT-7.16");
		addPoint(1985, 100.74706, "Atari_800XL");
		addPoint(1986, 108.445, "Sun-3");
		addPoint(1986, 32.62872, "DEC_VAX_8650");
		addPoint(1986, 564.972, "MIT_XT-8");
		addPoint(1987, 456.66667, "Mac_II");
		addPoint(1987, 102.476, "Sun-4");
		addPoint(1988, 239.58042, "Mac-IIx");
		addPoint(1988, 763.15036, "CompuAdd_386-16");
		addPoint(1988, 1002.16327, "PC_Brand_386-25");
		addPoint(1989, 120.77255, "Wang_VS_10000");
		addPoint(1989, 359.07621, "Macintosh_SE30");
		addPoint(1989, 304.98, "Solbourne_5/500");
		addPoint(1990, 193.24719, "Stardent_3000");
		addPoint(1990, 2715.51724, "Dell_320LX");
		addPoint(1990, 638.36255, "Mac_IIfx");
		addPoint(1990, 2386.36364, "Amiga_3000");
		addPoint(1991, 5205.46154, "Gateway-486DX2/66");
		addPoint(1991, 4212.52941, "ACT_468/33");
		addPoint(1991, 4381.32768, "Mac-Quadra-900");
		addPoint(1992, 6238.07143, "AST_Bravo");
		addPoint(1992, 3588.1, "IBM_PS/2_55-041");
		addPoint(1992, 3074.70833, "NEC_Powermate");
		addPoint(1993, 5053.25, "IBM_Valuepoint");
		addPoint(1993, 8861.85714, "Acer_Power");
		addPoint(1993, 3989.72414, "DECpc_LPv");
		addPoint(1994, 10367.5, "IBM_433/DX/Si");
		addPoint(1994, 11583, "Gateway_2000_486");
		addPoint(1994, 24663.67713, "PowerMac_7100/66");
		addPoint(1995, 33083.27082, "PowerMac_8500/120");
		addPoint(1995, 27741.08322, "PowerMac_9500/132");
		addPoint(1995, 25725, "Intel_Xpress/60");
		addPoint(1996, 34822, "Gateway_P5-75");
		addPoint(1996, 68922.61002, "Power_Tower_180e");
		addPoint(1996, 40386.79560, "PowerMac_7600/132");
		addPoint(1997, 91861.64802, "Gateway_G6-200");
		addPoint(1997, 116100, "Power_Center_210");
		addPoint(1997, 193500, "Mac_G3/266");
		addPoint(1998, 272076.92308, "iMac_G3/233");
		addPoint(1998, 165933.33333, "AcerPower8000/450B");
		addPoint(1998, 222130.43478, "Mac_G3/333");
		addPoint(1999, 301500, "Pentium_II/455");
		addPoint(1999, 263712, "Pentium_III/500");
		addPoint(1999, 275289.6, "Mac_G4/450");
		addPoint(2000, 75545.45455, "IBM_ASCI_White");
		addPoint(2000, 16620000.0, "Sony_Playstation_II");
		addPoint(2000, 356142.85714, "Mac_G4/500_dual");
		addPoint(2001, 392840, "CerfCube");
		addPoint(2001, 444080, "Mac_G4/867");
		addPoint(2001, 945093.33333, "Dell_Workst_340/2G");
		addPoint(2002, 596750, "iMac_G4/700");
		addPoint(2002, 1562400, "Dell_Workst_340/2.5");
		addPoint(2002, 1198997.33333, "Athlon_XP_2.6GHz");
		addPoint(2003, 1108750, "Mac_G4/Dual-1.25GHz");
		addPoint(2003, 1574957.2, "Dell_D_8300_P4/3.0");
		addPoint(2003, 1878666, "Mac_G5/Dual-2.0");
		addPoint(2004, 3871750, "Dell_P4-530_3.0");
		addPoint(2004, 2602857.14286, "Mac_G5/Dual-2.5");
		addPoint(2004, 3036666.66667, "VAtech_SysX_2.3G");
		addPoint(2004, 3097400, "Athlon64_FX55-2.6");
		addPoint(2005, 3485400, "Mac_Mini_G4-1.25");
		addPoint(2005, 4396000, "Opteron_252-2.6");
		addPoint(2005, 5382857.14286, "Mac_G5/Quad-2.5");
		addPoint(2006, 8576470.58824, "iMac-Core_Duo_2.0x2");
		addPoint(2006, 11664000, "Mac_Pro_Xeon_2.66x4");
		addPoint(2006, 10935000, "Mac_Mini_Duo_1.83x2");
		addPoint(2007, 12500000, "Mac_Pro/2_3GHzx8");
		addPoint(2007, 10000000, "Sun_Blackbox");
		addPoint(2007, 10769230.76923, "IBM_Blue_Gene/P");
	}

	private void addPoint(int year, double flopsPerDollar, String name) {
		points.add(new SimplePoint(year, Math.log10(flopsPerDollar)));
	}

	public final Collection<SimplePoint> points;

}
