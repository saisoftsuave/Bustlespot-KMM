package org.softsuave.bustlespot.data.network


object APIEndpoints {
    const val SIGNIN = "/api/auth/signin"
    const val SIGNOUT = "/api/auth/signout"

    const val GETALLORGANISATIONS ="/api/organisation/getUserOrganization"
    const val GetOrganisation ="/organisation/get_organisation"

    const val GETALLPROJECTS = "/api/project/getProjectList"
    const val GETALLTASKS = "/api/task/getTaskByProjectId"

    const val POSTACTIVITY = "/api/activity/addActivityList"
    const val GETALLACTIVITIES = "/api/activity/get-all-activity"

    const val UPDATEACTIVITY = "/api/activity/updateActivity"
}