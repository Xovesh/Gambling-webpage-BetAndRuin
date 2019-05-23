package testsV2;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

import businessLogicV3.privateAPI.BLFacadePrivate;
import businessLogicV3.publicAPI.BLFacadePublic;

public class DatabaseStatsPopulation {

	public static void main(String[] args) {
		BLFacadePublic dataManager = new BLFacadePublic();
		String s = dataManager.getUsers().authentication("tractor", "Patata");
		BLFacadePrivate p = dataManager.getPrivateAPI("tractor", s);
		long x = 1234567L;
		long y = 23456789L;
		long z = 12345L;
		long h = 234567L;
		Random r = new Random();
		for (int i = 0; i<=28; i++) {
			Calendar date = new GregorianCalendar(2019, 1, i);
			p.getOthers().fakeStats(date, x + ((long) (r.nextDouble() * (y - x))),
					h + ((long) (r.nextDouble() * (z - h))));
		}
		for (int i = 0; i<=31; i++) {
			Calendar date = new GregorianCalendar(2019, 2, i);
			p.getOthers().fakeStats(date, x + ((long) (r.nextDouble() * (y - x))),
					h + ((long) (r.nextDouble() * (z - h))));
		}
		for (int i = 0; i<=30; i++) {
			Calendar date = new GregorianCalendar(2019, 3, i);
			p.getOthers().fakeStats(date, x + ((long) (r.nextDouble() * (y - x))),
					h + ((long) (r.nextDouble() * (z - h))));
		}
		for (int i = 0; i<=15; i++) {
			Calendar date = new GregorianCalendar(2019, 4, i);
			p.getOthers().fakeStats(date, x + ((long) (r.nextDouble() * (y - x))),
					h + ((long) (r.nextDouble() * (z - h))));
		}

	}
}
