package hu.bme.aut.adminclient.model

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.IntegerRes

class Costumer{
    var customerId : Int = 1
    var emailAddress : String = ""
    var enabled : Boolean = false
    var phone : String = ""
    var drivingLicenceFront : Int = 0
    var drivingLicenceBack : Int = 0
    var profileImage : Int = 0
    var licenceCardNumber : String = ""
    var firstName : String = ""
    var expirationDate : String = ""
    var lastName : String = ""

}