package org.springframework.samples.petclinic.utility;

import com.github.mryf323.tractatus.*;
import com.github.mryf323.tractatus.experimental.extensions.ReportingExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(ReportingExtension.class)
@ClauseDefinition(clause = 'a', def = "t1arr[0] != t2arr[0]")
@ClauseDefinition(clause = 'b', def = "t1arr[1] != t2arr[1]")
@ClauseDefinition(clause = 'c', def = "t1arr[2] != t2arr[2]")
@ClauseDefinition(clause = 'd', def = "t1arr[0] < 0")
@ClauseDefinition(clause = 'e', def = "t1arr[0] + t1arr[1] < t1arr[2]")
class TriCongruenceTest {

	private static final Logger log = LoggerFactory.getLogger(TriCongruenceTest.class);

	// *******************************  CACC For Line 15  ***********************************88
	@CACC(
		predicate = "d || e",
		majorClause = 'd',
		predicateValue = true,
		valuations = {
			@Valuation(clause = 'd', valuation = true),
			@Valuation(clause = 'e', valuation = false)
		}
	)
	@Test
	public void test_CACC_line15_d_true() {
		Triangle t1 = new Triangle(-2, 5, 2);
		Triangle t2 = new Triangle(4, 5, 6);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertFalse(areCongruent);
	}

	@CACC(
		predicate = "d || e",
		majorClause = 'd',
		predicateValue = false,
		valuations = {
			@Valuation(clause = 'd', valuation = false),
			@Valuation(clause = 'e', valuation = false)
		}
	)
	@Test
	public void test_CACC_line15_d_false() {
		Triangle t1 = new Triangle(4, 5, 6);
		Triangle t2 = new Triangle(4, 5, 6);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertTrue(areCongruent);
	}

	@CACC(
		predicate = "d || e",
		majorClause = 'e',
		predicateValue = true,
		valuations = {
			@Valuation(clause = 'd', valuation = false),
			@Valuation(clause = 'e', valuation = true)
		}
	)
	@Test
	public void test_CACC_line15_e_true() {
		Triangle t1 = new Triangle(2, 3, 6);
		Triangle t2 = new Triangle(4, 5, 6);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertFalse(areCongruent);
	}

	@CACC(
		predicate = "d || e",
		majorClause = 'e',
		predicateValue = false,
		valuations = {
			@Valuation(clause = 'd', valuation = false),
			@Valuation(clause = 'e', valuation = false)
		}
	)
	@Test
	public void test_CACC_line15_e_false() {
		Triangle t1 = new Triangle(2, 5, 6);
		Triangle t2 = new Triangle(3, 4, 5);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertFalse(areCongruent);
	}
	// ***************************  CC For Line15 ****************************
	@ClauseCoverage(
		predicate = "d || e",

		valuations = {
			@Valuation(clause = 'd', valuation = true),
			@Valuation(clause = 'e', valuation = true),
		}
	)
	@Test
	public void test_CC_line15_true() {
		Triangle t1 = new Triangle(-1, 4, 5);
		Triangle t2 = new Triangle(1, 2, 3);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertFalse(areCongruent);
	}

	@ClauseCoverage(
		predicate = "d || e",

		valuations = {
			@Valuation(clause = 'd', valuation = false),
			@Valuation(clause = 'e', valuation = false),
		}
	)
	@Test
	public void test_CC_line15_false() {
		Triangle t1 = new Triangle(3, 4, 5);
		Triangle t2 = new Triangle(3, 4, 5);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertTrue(areCongruent);
	}

	// ************************************* CUTPNFP For Line 14 **************************************************

	@UniqueTruePoint(
		predicate = "a + b + c",
		dnf = "a + b + c",
		implicant = "a",
		valuations = {
			@Valuation(clause = 'a', valuation = true),
			@Valuation(clause = 'b', valuation = false),
			@Valuation(clause = 'c', valuation = false)
		}
	)
	@Test
	public void line14_CUTPNFP_test1() {
		Triangle t1 = new Triangle(3, 4, 5);
		Triangle t2 = new Triangle(2, 4, 5);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertFalse(areCongruent);
	}

	@UniqueTruePoint(
		predicate = "a + b + c",
		dnf = "a + b + c",
		implicant = "b",
		valuations = {
			@Valuation(clause = 'a', valuation = false),
			@Valuation(clause = 'b', valuation = true),
			@Valuation(clause = 'c', valuation = false)
		}
	)
	@Test
	public void line14_CUTPNFP_test2() {
		Triangle t1 = new Triangle(2, 4, 5);
		Triangle t2 = new Triangle(2, 3, 5);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertFalse(areCongruent);
	}

	@UniqueTruePoint(
		predicate = "a + b + c",
		dnf = "a + b + c",
		implicant = "c",
		valuations = {
			@Valuation(clause = 'a', valuation = false),
			@Valuation(clause = 'b', valuation = false),
			@Valuation(clause = 'c', valuation = true)
		}
	)
	@Test
	public void line14_CUTPNFP_test3() {
		Triangle t1 = new Triangle(2, 3, 6);
		Triangle t2 = new Triangle(2, 3, 5);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertFalse(areCongruent);
	}

	@NearFalsePoint(
		predicate = "a + b + c",
		dnf = "a + b + c",
		implicant = "a",
		clause = 'a',
		valuations = {
			@Valuation(clause = 'a', valuation = false),
			@Valuation(clause = 'b', valuation = false),
			@Valuation(clause = 'c', valuation = false)
		}
	)
	@Test
	public void line14_CUTPNFP_test4() {
		Triangle t1 = new Triangle(2, 3, 4);
		Triangle t2 = new Triangle(2, 3, 4);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertTrue(areCongruent);
	}

	@NearFalsePoint(
		predicate = "a + b + c",
		dnf = "a + b + c",
		implicant = "b",
		clause = 'b',
		valuations = {
			@Valuation(clause = 'a', valuation = false),
			@Valuation(clause = 'b', valuation = false),
			@Valuation(clause = 'c', valuation = false)
		}
	)
	@Test
	public void line14_CUTPNFP_test5() {
		Triangle t1 = new Triangle(-1, 3, 4);
		Triangle t2 = new Triangle(-1, 3, 4);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertFalse(areCongruent);
	}

	@NearFalsePoint(
		predicate = "a + b + c",
		dnf = "a + b + c",
		implicant = "c",
		clause = 'c',
		valuations = {
			@Valuation(clause = 'a', valuation = false),
			@Valuation(clause = 'b', valuation = false),
			@Valuation(clause = 'c', valuation = false)
		}
	)
	@Test
	public void line14_CUTPNFP_test6() {
		Triangle t1 = new Triangle(2, 3, 4);
		Triangle t2 = new Triangle(2, 3, 4);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertTrue(areCongruent);
	}






	/*
	  predicate: ab + cd
	  CUTPNFP:
	  Implicants: {ab, cd}
	  ab:
	  	UTP: {TTTF}
	  	NFP: a: {FTTF} b: {TFTF}
	  cd:
	  	UTP: {TFTT}
	  	NFP: c: {TFFT} d: {TFTF}

	  CUTPNFP: {TTTF, FTTF, TFTF, TFTT, TFFT}

	  UTP:
	  	 ~f = ~a~c + ~a~d + ~b~c + ~b~d
	  Implicants: {ab, cd, ~a~d, ~a~c, ~b~c, ~b~d}

	   because of having 6 implicants we need 6 test for UTPC but we have only 5 tests in CUTPNFP, so it can't subsume UTPC.
	 */
	private static boolean questionTwo(boolean a, boolean b, boolean c, boolean d, boolean e) {
		boolean predicate = false;
		predicate = (a && b) || (c && d);
		return predicate;
	}
}

