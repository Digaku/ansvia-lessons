package com.ansvia.belajar

import org.specs2.mutable.Specification

/**
 * Copyright (C) 2011-2013 Ansvia Inc.
 * Author: robin
 * Date: 1/12/13
 * 
 */
class SekolahSDValidatorSpec extends Specification {

  "Anak SD validator" should {
    "nama diawalai dengan n, b" in {
      SekolahSD.validator.namaValid("ntest") must beTrue
      SekolahSD.validator.namaValid("btest") must beTrue
    }
    "umur harus dibawah 8 tahun" in {
      SekolahSD.validator.umurValid(5) must beTrue
      SekolahSD.validator.umurValid(7) must beTrue
    }
    "umur harus diatas 4 tahun" in {
      SekolahSD.validator.umurValid(6) must beTrue
      SekolahSD.validator.umurValid(4) must beFalse
    }
    "nama andrie harus gak valid" in {
      SekolahSD.validator.namaValid("andrie") must beFalse
    }
    "nama surya harus gak valid" in {
      SekolahSD.validator.namaValid("surya") must beFalse
    }
    "nama bram harus valid" in {
      SekolahSD.validator.namaValid("bram") must beTrue
    }
    "nama nadhir harus valid" in {
      SekolahSD.validator.namaValid("nadhir") must beTrue
    }
  }
}
