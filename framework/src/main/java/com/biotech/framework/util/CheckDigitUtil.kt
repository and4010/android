package com.biotech.framework.util

class CheckDigitUtil {

    fun check(barcode: String) :Boolean
        = when {
            barcode.length == 8 && barcode.substring(0,1) == "0" -> {
                checkcode(upceToUpca(barcode))
            }
            else -> {
                checkcode(barcode)
            }
        }


    fun checkcode(barocde : String) :Boolean {
        var Eanbarcode = ""
        if (barocde.length != 18){
            Eanbarcode = barocde.padStart(18,'0')
        }
        val checkcode1 = Eanbarcode.substring(17,18)
        val NoCheckCodeBarcode = Eanbarcode.substring(0,17)
        var odd = 0
        var even = 0
        var idx = 0
        for (i in NoCheckCodeBarcode){
            val num = i.toInt() - '0'.toInt()
            idx = idx.inc()
            if (idx % 2 != 0){
                odd += num
            }else{
                even += num
            }
        }
        val total = (odd*3)+even
        val check = total.toString().substring(total.toString().length-1).toInt()
        val totalcheckcode = if (check == 0){
            0
        }else{
            10 - check
        }
        return checkcode1 == totalcheckcode.toString()
    }

    fun upceToUpca(upce : String) :String
        = when(upce.substring(6,7)) {
        "0" -> {
            "${upce.substring(0, 3)}00000${upce.substring(3, 6)}${upce.substring(7, 8)}"
        }
        "1" -> {
            "${upce.substring(0, 3)}10000${upce.substring(3, 6)}${upce.substring(7, 8)}"
        }
        "2" -> {
            "${upce.substring(0, 3)}20000${upce.substring(3, 6)}${upce.substring(7, 8)}"
        }
        "3" -> {
            "${upce.substring(0, 4)}00000${upce.substring(4, 6)}${upce.substring(7, 8)}"
        }
        "4" -> {
            "${upce.substring(0, 5)}00000${upce.substring(5, 6)}${upce.substring(7, 8)}"
        }
//        "5", "6", "7", "8", "9" -> {
        else -> {
            "${upce.substring(0, 6)}0000${upce.substring(6, 8)}"
        }
    }


}