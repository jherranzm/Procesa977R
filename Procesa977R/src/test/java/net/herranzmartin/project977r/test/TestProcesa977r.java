package net.herranzmartin.project977r.test;

import net.herranzmartin.project977r.Procesa977R;

import org.junit.Test;

public class TestProcesa977r {

	@Test
	public void test() {
		String textFile = "/Users/jherranzm/dev/java_workspace/PMF01119.ENE";
	

		Procesa977R proc = new Procesa977R(textFile);
		proc.execute();
	}

}
