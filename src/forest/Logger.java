package forest;

public class Logger {	
	public int monthlyWood      = 0;
	public int monthlyMawings   = 0;
	public int monthlySaplings  = 0;
	public int monthlyAdults    = 0;
	public int monthlyElders    = 0;
	
	public int yearlyWood       = 0;
	public int yearlyMawings    = 0;
	public int yearlySaplings   = 0;
	public int yearlyAdults     = 0;
	public int yearlyElders     = 0;
	
	public int lumberJacksHired = 0;
	public int bearsCaught      = 0;
	
	public int saplings         = 0;
	public int adults           = 0;
	public int elders           = 0;
	public int bears            = 0;
	public int lumberjacks      = 0;
	
	void updateMonthlyWood(int x) {
		monthlyWood += x;
		yearlyWood += x;
	}
	
	void updateMonthlyMawings(int x) {
		monthlyMawings += x;
		yearlyMawings += x;
		lumberjacks -= x;
	}
	
	void updateMonthlySaplings(int x) {
		monthlySaplings += x;
		yearlySaplings += x;
		saplings += x;
	}
	
	void updateMonthlyAdults(int x) {
		monthlyAdults += x;
		yearlyAdults += x;
		adults += x;
	}
	
	void updateMonthlyElders(int x) {
		monthlyElders += x;
		yearlyElders += x;
		elders += x;
	}
	
	void updateLumberJacksHired(int x) {
		lumberJacksHired += x;
		lumberjacks += x;
	}
	
	void updateBearsCaught(int x) {
		bearsCaught += x;
		bears += x;
	}
	
	void updateLumberJacks(int x) {
		lumberjacks += x;
	}
	
	void updateSaplingTrees(int x) {
		saplings += x;
	}
	
	void updateAdultTrees(int x) {
		adults += x;
	}
	
	void updateElderTrees(int x) {
		elders += x;
	}
	
	void updateBears(int x) {
		bears += x;
	}
	
	public void monthlyLog (int month) {
		String m = String.valueOf(month);
		while (m.length() < 4) {
			m = "0" + m;
		}
		m = "Month [" + m + "] ";
		if (monthlyWood != 0) {
			if (monthlyWood > 1) {
				System.out.println(m + monthlyWood + " pieces of Lumber harvested by Lumberjacks.");
			} else {
				System.out.println(m + "1 piece of Lumber harvested by Lumberjacks.");
			}
			
		}
		if (monthlyMawings != 0) {
			if (monthlyMawings > 1) {
				System.out.println(m + monthlyMawings + " Lumberjacks were eaten by bears.");
			} else {
				System.out.println(m + "1 Lumberjack was eaten by bears.");
			}
		}
		if (monthlySaplings != 0) {
			if (monthlySaplings > 1) {
				System.out.println(m + monthlySaplings + " new Saplings were created.");	
			} else {
				System.out.println(m + "1 new Sapling was created.");
			}
		}
		if (monthlyAdults != 0) {
			if (monthlyAdults > 1) {
				System.out.println(m + "[" + monthlyAdults + "] Saplings matured into adult trees.");	
			} else {
				System.out.println(m + "1 Sapling matured into an adult tree.");
			}
			
		}
		if (monthlyElders != 0) {
			if (monthlyElders > 1) {
				System.out.println(m + monthlyElders + " adult trees have matured into elder trees.");	
			} else {
				System.out.println(m + "[1] adult tree has matured into an elder tree.");
			}
			
		}
		System.out.println("");
		monthlyReset();
	}
	
	private void monthlyReset() {
		monthlyAdults   = 0;
		monthlyElders   = 0;
		monthlyMawings  = 0;
		monthlySaplings = 0;
		monthlyWood     = 0;
	}
	
	public void yearlyLog(int year) {
		String y = String.valueOf(year);
		while (y.length() < 3) {
			y = "0" + y;
		}
		y = "Year [" + y + "] ";
		System.out.println(y + "The forest has " + saplings + " Saplings, " + adults + " Adult Trees and " + elders + " Elder Trees.");
		System.out.println(y + "There are " + lumberjacks + " Lumberjacks and " + bears + " Bears.");
		if (yearlyWood > 0) {
			if (yearlyWood > 1) {
				System.out.println(y + yearlyWood + " pieces of wood were harvested.");	
			} else {
				System.out.println(y + yearlyWood + " piece of wood was harvested.");
			}
		}
		if (yearlyMawings > 0) {
			if (yearlyMawings > 1) {
				System.out.println(y + yearlyMawings + " Lumberjacks were eaten by Bears.");
			} else {
				System.out.println(y + yearlyMawings + " Lumberjack was eaten by Bears.");
			}
		}
		if (bearsCaught > 0) {
			System.out.println(y + bearsCaught + " Bear has been caught.");
		} else if (bearsCaught < 0) { 
			System.out.println(y + -bearsCaught + " Bear was added to the forest.");
		}
		if (lumberJacksHired > 0) {
			if (lumberJacksHired > 1) {
				System.out.println(y + lumberJacksHired + "Lumberjacks were hired.");
			} else {
				System.out.println(y + lumberJacksHired + "Lumberjack was hired.");
			}
		} else if (lumberJacksHired < 0) {
			System.out.println(y + "1 Lumberjack was let go.");
		}
		System.out.println();
		yearlyReset();
	}
	
	private void yearlyReset() {
		yearlyWood       = 0;
		yearlyMawings    = 0;
		yearlySaplings   = 0;
		yearlyAdults     = 0;
		yearlyElders     = 0;
		lumberJacksHired = 0;
		bearsCaught      = 0;
	}
	
}
