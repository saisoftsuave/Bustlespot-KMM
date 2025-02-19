package org.softsuave.bustlespot.data.local.realme.objects


import io.realm.kotlin.types.EmbeddedRealmObject
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class OrganisationObj : RealmObject {

    @PrimaryKey
    var _id : ObjectId = ObjectId()
    var organisationId: String = ""
    var organisationName: String = ""
    var organisationDescription: String = ""
    var isDeleted: Boolean = false
}