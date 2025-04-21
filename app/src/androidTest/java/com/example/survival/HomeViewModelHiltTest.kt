package com.example.survival


import com.example.survival.ui.home.HomeRepo
import com.example.survival.ui.home.HomeViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class HomeViewModelHiltTest {


    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var homeRepo: HomeRepo

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        hiltRule.inject()
        viewModel = HomeViewModel(homeRepo)
    }


    @Test
    fun addData(){
        homeRepo.addData("test")
        assertEquals(homeRepo.getData().size, 1)
        assertEquals(homeRepo.getData()[0], "test")
    }

    @Test
    fun editData(){
        homeRepo.addData("test")
        homeRepo.editData("test2",0)
        assertEquals(homeRepo.getData()[0],"test2")

    }

    @Test
    fun removeData(){
        homeRepo.addData("test")
        homeRepo.removeData(0)
        assertEquals(homeRepo.getData().size, 0)
    }

    @Test
    fun clearData(){
        homeRepo.addData("test")
        homeRepo.clearData()
        assertEquals(homeRepo.getData().size, 0)
    }

    /**
     * 嘗試從空的資料列表中刪除資料，應該不會丟出錯誤，資料列表應該保持為空
     */
    @Test
    fun removeDataIndexOutOfBounds() {
        homeRepo.removeData(0)
        assertEquals(0, homeRepo.getData().size)
    }

    /**
     *  嘗試從空的資料列表中編輯資料，應該不會丟出錯誤，資料列表應該保持為空
     */
    @Test
    fun editDataIndexOutOfBounds() {
        homeRepo.editData("test", 0)
        assertEquals(0, homeRepo.getData().size)
    }


    @Test
    fun editDataError() {

        homeRepo.addData("test1")
        homeRepo.editData("test2", 5)

        assertEquals(1, homeRepo.getData().size)
        assertEquals("test1", homeRepo.getData()[0])

    }

    @Test
    fun removeDataError() {

        homeRepo.addData("test1")
        homeRepo.removeData(5)

        assertEquals(1, homeRepo.getData().size,)
        assertEquals("test1", homeRepo.getData()[0])
    }

}