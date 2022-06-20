package com.biotech.framework.util

/**************************************************************
 * 字串工具類別
 */
class StringUtils private constructor() {
    companion object {
        /**************************************************************
         * 判斷是否為空
         * @param argStr     字串資料
         * @return
         */
        fun isBlank(argStr: CharSequence): Boolean {
            return isEmpty(argStr) || argStr.toString().trim { it <= ' ' }.isEmpty()
        }

        /**************************************************************
         * 判斷長度是否為 0
         * @param argStr     字串資料
         * @return
         */
        fun isEmpty(argStr: CharSequence?): Boolean {
            return argStr == null || argStr.isEmpty()
        }

        /**************************************************************
         * 取得長度
         * @param argStr     字串資料
         * @return
         */
        fun length(argStr: CharSequence?): Int {
            return argStr?.length ?: 0
        }

        /**************************************************************
         * 判斷是否部為空
         * @param argStr     字串資料
         * @return
         */
        fun isNotBlank(argStr: Any?): Boolean {
            if (argStr == null) {
                return false
            } else {
                if (argStr.toString().trim { it <= ' ' }.isEmpty()) {
                    return false
                }
            }
            return true
        }

        /**************************************************************
         * 合併字元組
         * @param argStr1     第一組
         * @param argStr2    第二組
         * @return
         */
        fun equals(argStr1: String?, argStr2: String?): Boolean {
            return if (argStr1 != null) argStr1 == argStr2 else argStr2 == null
        }

        /**************************************************************
         * 合併字元組
         * @param argFirstArray     第一組
         * @param argSecondArray    第二組
         * @return
         */
        fun getByteContact(argFirstArray: ByteArray?, argSecondArray: ByteArray?): ByteArray? {
            if (argFirstArray == null || argSecondArray == null) return null
            val bytes = ByteArray(argFirstArray.size + argSecondArray.size)
            System.arraycopy(argFirstArray, 0, bytes, 0, argFirstArray.size)
            System.arraycopy(argSecondArray, 0, bytes, argFirstArray.size, argSecondArray.size)
            return bytes
        }

        /**************************************************************
         * 右字串補數(零)
         * @param argData     資料
         * @param argPaddingCount    補充長度
         * @return
         */
        fun getRightPadZero(argData: String, argPaddingCount: Int): String {
            return getRightPad(argData, argPaddingCount, '0')
        }

        /**************************************************************
         * 右字串補數(空白)
         * @param argData     資料
         * @param argPaddingCount    補充長度
         * @return
         */
        fun getRightPadSpace(argData: String, argPaddingCount: Int): String {
            return getRightPad(argData, argPaddingCount, ' ')
        }

        /**************************************************************
         * 右字串補數
         * @param argData           資料
         * @param argPaddingCount   補充長度
         * @param argPaddingChar    補充字元
         * @return
         */
        fun getRightPad(argData: String, argPaddingCount: Int, argPaddingChar: Char): String {
            var res = argData
            for (i in 0 until argPaddingCount) {
                res = res + argPaddingChar
            }
            return res
        }

        /**************************************************************
         * 左字串補數(零)
         * @param argData     資料
         * @param argPaddingCount    補充長度
         * @return
         */
        fun getLefttPadZero(argData: String, argPaddingCount: Int): String {
            return getLeftPad(argData, argPaddingCount, '0')
        }

        /**************************************************************
         * 左字串補數(空白)
         * @param argData     資料
         * @param argPaddingCount    補充長度
         * @return
         */
        fun getLeftPadSpace(argData: String, argPaddingCount: Int): String {
            return getLeftPad(argData, argPaddingCount, ' ')
        }

        /**************************************************************
         * 左字串補數
         * @param argData           資料
         * @param argPaddingCount   補充長度
         * @param argPaddingChar    補充字元
         * @return
         */
        fun getLeftPad(argData: String, argPaddingCount: Int, argPaddingChar: Char): String {
            var res = argData
            for (i in 0 until argPaddingCount) {
                res = argPaddingChar.toString() + res
            }
            return res
        }

        /**************************************************************
         * 判斷是否為英文
         * @param argData           資料
         * @return
         */
        fun isAlpha(argData: String?): Boolean {
            val regex = "^[a-zA-Z]*$"
            return checkByRegex(argData, regex)
        }

        /**************************************************************
         * 判斷是否為數字
         * @param argData           資料
         * @return
         */
        fun isRealNumeric(argData: String?): Boolean {
            val regex = "^[0-9\\.]*$"
            return checkByRegex(argData, regex)
        }

        /**************************************************************
         * 判斷是否為數字
         * @param argData           資料
         * @return
         */
        fun isNumeric(argData: String?): Boolean {
            val regex = "^[0-9]*$"
            return checkByRegex(argData, regex)
        }

        /**************************************************************
         * 判斷是否為英文 + 數字
         * @param argData           資料
         * @return
         */
        fun isAlphaNumeric(argData: String?): Boolean {
            val regex = "^[a-zA-Z0-9]*$"
            return checkByRegex(argData, regex)
        }

        /**************************************************************
         * 判斷是否為網路位址 IPv4
         * @param argData           資料
         * @return
         */
        fun isIpAddress(argData: String?): Boolean {
            val regex = ("^((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4]"
                    + "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]"
                    + "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}"
                    + "|[1-9][0-9]|[0-9]))$")
            return checkByRegex(argData, regex)
        }

        /**************************************************************
         * 正規表達式判斷
         * @param argData           資料
         * @param argRegex          正規表達式
         * @return
         */
        fun checkByRegex(argData: String?, argRegex: String): Boolean {
            return argData != null && argData.trim { it <= ' ' }.isNotEmpty() && argData.trim { it <= ' ' }.matches(Regex(argRegex))
        }
    }

    init {
        throw AssertionError()
    }
}