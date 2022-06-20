package com.biotech.framework.util

import androidx.annotation.Nullable
import android.util.Log
//import org.apache.log4j.Logger
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
//import org.slf4j.LoggerFactory


class MD5 {

    private val TAG = this.javaClass.simpleName
//    private val logger = LoggerFactory.getLogger(this.javaClass.simpleName)

    @Nullable
    fun calculateMD5(updateFile:File): String? {
        val digest : MessageDigest
        try {
            digest = MessageDigest.getInstance("MD5")
        }catch (NoSuchAlgorithm:NoSuchAlgorithmException){
            Log.e(TAG,"Exception while getting diges",NoSuchAlgorithm)
//            logger.error("calculateMD5：$TAG Exception while getting digest")
            return null
        }

        val FileInput:FileInputStream
        try {
            FileInput = FileInputStream(updateFile)
        }catch (FileNotFound : FileNotFoundException){
            Log.e(TAG, "Exception while getting FileInputStream", FileNotFound)
//            logger.error("calculateMD5：$TAG Exception while getting FileInputStream")
            return null
        }

        val buffer = ByteArray(8192)

        val var8: String
        var read :Int
        try {
            do {
                read = FileInput.read(buffer)
                if ( read > 0){
                    digest.update(buffer,0,read)
                }

            }while (read>0)
            val md5sum = digest.digest()
            val bigInt = BigInteger(1,md5sum)
            var output = bigInt.toString(16)
            output = String.format("%32s", output).replace(' ', '0')
            var8 = output
        }catch (IoException:IOException){
            throw RuntimeException("Unable to process file for MD5", IoException)
        }finally {
            try {
                FileInput.close()
            }catch (IoExceptionTwo :IOException){
                Log.e(TAG, "Exception on closing MD5 input stream", IoExceptionTwo)
//                logger.error("calculateMD5：$TAG Exception on closing MD5 input stream")
            }
        }

        return var8


    }

    fun CheckMD5(md5:String, updateFile:File):Boolean {
        if (md5 != "" && updateFile != null) {
            val calculatedDigest = calculateMD5(updateFile)?.toUpperCase()
            if (calculatedDigest == null) {
                Log.e(TAG, "calculatedDigest null")
//                logger.error("$TAG calculatedDigest null")
                return false
            } else {
                Log.v(TAG, "Calculated digest: $calculatedDigest")
                Log.v(TAG, "Provided digest: $md5")

//                logger.debug("checkMD5: $TAG Calculated digest: $calculatedDigest")
//                logger.debug("checkMD5: $TAG Provided digest: $md5")
                return calculatedDigest.equals(md5, ignoreCase = true)
            }
        } else {
            Log.e(TAG, "MD5 string empty or updateFile null")
//            logger.error("checkMD5: $TAG MD5 string empty or updateFile null")
            return false
        }
    }


}