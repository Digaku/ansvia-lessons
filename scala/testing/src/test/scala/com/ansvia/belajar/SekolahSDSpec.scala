package com.ansvia.belajar

import org.specs2.Specification

/**
 * Copyright (C) 2011-2013 Ansvia Inc.
 * Author: robin
 * Date: 1/12/13
 * 
 */
class SekolahSDSpec extends Specification {
  def is = {
    "Sekolah SD harus" ^
    p ^
      "bisa mendaftarkan anak dengan nama bram dan umur 7" ! sekolahSd.daftarBram ^
      "tidak bisa mendaftarkan anak dengan nama andrie" ! sekolahSd.daftarAndrie ^
      "tidak bisa mendaftarkan anak dengan umur diatas 8 tahun" ! sekolahSd.daftarAnakUmurDiatas9Tahun ^
      "bisa mendapakan daftar anak-anak yang sudah masuk" ! sekolahSd.daftarAnak ^
    end
  }

  object sekolahSd {
    
    val bram = SekolahSD.daftar("bram", 7)

    def daftarBram = {
      bram must beAnInstanceOf[Anak]
    }

    def daftarAndrie = {
      SekolahSD.daftar("andrie", 10) must throwAn[Exception]
    }

    def daftarAnakUmurDiatas9Tahun =
      SekolahSD.daftar("bram", 10) must throwAn[Exception]

    def daftarAnak = {
      SekolahSD.getDaftar.contains(bram) must beTrue
    }

  }


}
