package com.example.survival.ui.home



interface HomeRepoInterfaces {
    fun addData(value: String)
    fun removeData(value: Int)
    fun editData(value: String, index: Int)
    fun clearData()
    fun getData(): List<String>
}



class HomeRepo : HomeRepoInterfaces {


    private val data : MutableList<String> = mutableListOf()

    /**
     * 新增資料
     */
    override fun addData(value: String) {
        data.add(value)
    }

    /**
     * 移除資料
     */
    override fun removeData(value: Int) {
        data.removeAt(value)
    }

    /**
     * @param value 要修改的值
     * @param index 尋找index
     */
    override fun editData(value: String, index: Int) {
        if (index in data.indices){ //防止crash判斷的
            data[index] = value
        }

    }

    /**
     * 清除所有資料
     */
    override fun clearData() {
        data.clear()
    }

    /**
     * 取得所有資料
     */
    override fun getData(): List<String> = data.toList()




}