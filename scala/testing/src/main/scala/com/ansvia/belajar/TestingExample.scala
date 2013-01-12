
package com.ansvia.belajar

case class Anak(nama:String, umur:Int)

object SekolahSD {

  object validator {
    def namaValid(nama:String):Boolean = {
      nama.startsWith("n") || nama.startsWith("b")
    }
    def umurValid(umur:Int):Boolean = {
      umur > 4 && umur < 8
    }
  }

  private var anakAnak = Array.empty[Anak]

  def daftar(nama:String, umur:Int) = {

    if (!validator.namaValid(nama))
      throw new Exception("nama %s gak valid coy!".format(nama))

    if (!validator.umurValid(umur))
      throw new Exception("umur %d gak valid, harus diatas 4 di bawah 8".format(umur))

    val anak = Anak(nama, umur)
    anakAnak :+= anak

    anak
  }

  def getDaftar:Array[Anak] = {
    anakAnak
  }

}


