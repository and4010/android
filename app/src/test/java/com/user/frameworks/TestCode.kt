package com.user.frameworks


import com.biotech.framework.extension.Preference
import com.biotech.framework.util.CheckDigitUtil
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class TestCode {
    @Test
    fun test(){
        val checkDigitUtilTest = CheckDigitUtil()
        assertEquals(checkDigitUtilTest.check("111111"),true)


    }

    @Test
    fun test2(){
        var preference by Preference(RuntimeEnvironment.application,"n","")
        assertEquals(preference,"")

        preference = "ff"
        assertEquals(preference,"ff")
    }

}