package com.example.qapital//import android.content.Intent
//import com.example.qapital.activities.DebtDetailsActivity
//import org.junit.Assert
//import org.junit.Test
//import org.junit.runner.RunWith
//import org.mockito.ArgumentMatchers
//import org.mockito.Mock
//import org.mockito.Mockito
//import org.mockito.junit.MockitoJUnitRunner
//
//@RunWith(MockitoJUnitRunner::class)
//class DebtDetailsActivityTest {
//    @Mock
//    private lateinit var activity: DebtDetailsActivity? = null
//    @Test
//    fun testOnCreate() {
//        // Arrange
//        val intent: `val` = Intent()
//        intent.putExtra("debtName", "John Doe")
//        intent.putExtra("debtAmount", 100.0)
//        intent.putExtra("debtBorrowedDate", 1234567890)
//        intent.putExtra("debtReturnDate", 9876543210)
//        intent.putExtra("debtNote", "This is a debt note.")
//
//        // Act
//        activity!!.onCreate(null)
//
//        // Assert
//        Assert.assertEquals("John Doe", activity.tvNameDetails.text.toString())
//        Assert.assertEquals("100.0", activity.tvAmountDetails.text.toString())
//        Assert.assertEquals("12/31/1999", activity.tvBorrowedDateDetails.text.toString())
//        Assert.assertEquals("12/31/1998", activity.tvReturnDateDetails.text.toString())
//        Assert.assertEquals("This is a debt note.", activity.tvNoteDetails.text.toString())
//    }
//
//    @Test
//    fun testOnBackPressed() {
//        // Arrange
//        Mockito.doNothing().`when`(activity).finish()
//
//        // Act
//        activity!!.onBackPressed()
//
//        // Assert
//        Mockito.verify(activity).finish()
//    }
//
//    @Test
//    fun testOnUpdateDebt() {
//        // Arrange
//        val intent: `val` = Intent()
//        intent.putExtra("debtName", "John Doe")
//        intent.putExtra("debtAmount", 100.0)
//        intent.putExtra("debtBorrowedDate", 1234567890)
//        intent.putExtra("debtReturnDate", 9876543210)
//        intent.putExtra("debtNote", "This is a debt note.")
//        Mockito.doNothing().`when`(activity).updateDebtData(
//            ArgumentMatchers.anyString(),
//            ArgumentMatchers.anyDouble(),
//            ArgumentMatchers.anyString(),
//            ArgumentMatchers.anyLong(),
//            ArgumentMatchers.anyLong(),
//            ArgumentMatchers.anyString()
//        )
//
//        // Act
//        activity.onUpdateDebt(intent)
//
//        // Assert
//        Mockito.verify(activity).updateDebtData(
//            ArgumentMatchers.anyString(),
//            ArgumentMatchers.anyDouble(),
//            ArgumentMatchers.anyString(),
//            ArgumentMatchers.anyLong(),
//            ArgumentMatchers.anyLong(),
//            ArgumentMatchers.anyString()
//        )
//    }
//}