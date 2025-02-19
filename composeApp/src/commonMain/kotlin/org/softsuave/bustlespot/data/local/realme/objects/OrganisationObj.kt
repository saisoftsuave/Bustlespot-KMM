package org.softsuave.bustlespot.data.local.realme.objects


import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class OrganisationObj : RealmObject {

    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var name: String = ""
    var organisationId: Int = 0
    var image: String = ""
    var roleId: Int = 0
    var enableScreenshot: Int = 0
    var description: String = ""
    var role: String = ""
    var otherRoleIds: RealmList<Int> = realmListOf()
}