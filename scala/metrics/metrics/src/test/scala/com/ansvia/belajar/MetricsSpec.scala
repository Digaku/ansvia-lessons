package com.ansvia.belajar

import org.specs2.mutable._

class MetricsSpec extends Specification {
	
	"Hello" should {
		"match 5 characters" in {
			"hello".length must equalTo(5)
		}
	}
	
}
