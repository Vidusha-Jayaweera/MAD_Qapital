//import android.app.DatePickerDialog
//import android.widget.Toast
//import com.example.qapital.activities.DebtInsertionActivity
//import org.junit.Assert
//import org.junit.Test
//import org.junit.runner.RunWith
//import org.mockito.Mock
//import org.mockito.Mockito
//import org.mockito.junit.MockitoJUnitRunner
//
//@RunWith(MockitoJUnitRunner::class)
//class DebtInsertionActivityTest {
//    @Mock
//    private lateinit var datePickerDialog: DatePickerDialog
//    @Test
//    fun testClickDatePicker() {
//        // Arrange
//        val activity = DebtInsertionActivity()
//        activity.datePickerDialog = datePickerDialog
//
//        // Act
//        activity.clickDatePicker(null)
//
//        // Assert
//        Mockito.verify(datePickerDialog).show()
//    }
//
//    @Test
//    fun testSaveDebtData_emptyDebtAmount() {
//        // Arrange
//        val activity = DebtInsertionActivity()
//        activity.etDebtAmount.setText("")
//
//        // Act
//        activity.saveDebtData()
//
//        // Assert
//        Assert.assertEquals("Please enter debt amount", activity.etDebtAmount.error)
//    }
//
//    @Test
//    fun testSaveDebtData_emptyDebtName() {
//        // Arrange
//        val activity = DebtInsertionActivity()
//        activity.etDebtName.setText("")
//
//        // Act
//        activity.saveDebtData()
//
//        // Assert
//        Assert.assertEquals("Please enter creditor name", activity.etDebtName.error)
//    }
//
//    @Test
//    fun testSaveDebtData_success() {
//        // Arrange
//        val activity = DebtInsertionActivity()
//        activity.etDebtAmount.setText("100")
//        activity.etDebtName.setText("John Doe")
//
//        // Act
//        activity.saveDebtData()
//
//        // Assert
//        Assert.assertEquals(
//            "Data inserted successfully",
//            Toast.makeText(activity, "", Toast.LENGTH_LONG).getText()
//        )
//        Assert.assertEquals("", activity.etDebtAmount.text)
//        Assert.assertEquals("", activity.etDebtName.text)
//        Assert.assertEquals("", activity.etDebtNote.text)
//        Assert.assertEquals("", activity.etDebtBorrowedDate.text)
//        Assert.assertEquals("Borrowed date", activity.etDebtBorrowedDate.hint)
//        Assert.assertEquals("", activity.etDebtReturnDate.text)
//        Assert.assertEquals("Return date", activity.etDebtReturnDate.hint)
//    }
//}