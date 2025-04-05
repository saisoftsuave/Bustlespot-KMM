This is a Kotlin Multiplatform project targeting Android, iOS, Desktop.

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - `commonMain` is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    `iosMain` would be the right folder for such calls.

* `/iosApp` contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform, 
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.


Learn more about [Kotlin Multi-platform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…

# Employee Login API Documentation

## Endpoint Details

- **Base URL:** `https://bustlespot-api.gamzinn.com`
- **Route:** `/api/auth/signin`
- **Method:** `POST`
- **Content-Type:** `application/json`

## Request Parameters

The API expects a JSON payload with the following structure:

- **email** (string, required):  
  The registered email address of the employee attempting to log in. This field must contain a valid email format.

- **password** (string, required):  
  The password corresponding to the provided email address. It should meet the security requirements defined by the system (e.g., minimum length, complexity).

**Example Request Body:**
```json
{
  "email": "john.doe@example.com",
  "password": "securePassword123"
}
```

## Response Parameters
If the credentials are valid, the API will return a 200 OK response with a JSON object containing the employee's basic information and an authentication token.

- **userId** (Integer):
  The unique identifier of the employee in the system.
- **firstName**	(String):
  The employee’s first name.
- **lastName**	(String):
  The employee’s last name.
- **email**	 (String):
  The employee’s registered email address.
- **token**	 (String):
  A JWT token used for authenticating further API requests.
## Sample Response

```json
{
  "userId": 101,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "token": "eyJhbGciOiJIUzI1NiIsInR..."
}
```

# Error Handling

- **401** (Unauthorized) :
  If the email or password is incorrect.
- **400** (Bad Request) :
  If required fields are missing or invalid.
- **500** (Internal Server Error) :
  If something goes wrong on the server side


# User Organization API Documentation

## Endpoint Details
- **Base URL:** `https://bustlespot-api.gamzinn.com`
- **Route:** `/api/organisation/getUserOrganization`
- **Method:** `GET`
- **Content-Type:** `application/json`

## Request Parameters

This endpoint does not require any request body or parameters.

## Response Parameters

If the request is successful, the API will return a `200 OK` response with a JSON object containing the user's organization details.

- **status** (String):  
  Indicates the status of the request. Expected value: `"success"`.

- **data** (Object):  
  Contains the list of organizations the user is associated with.

  - **organisationList** (Array of Objects):  
    A list of organizations where the user is a member. Each object includes:

    - **name** (String):  
      The name of the organization.

    - **organisationId** (Integer):  
      A unique identifier for the organization.

    - **image** (String):  
      URL of the organization's logo or image.

    - **roleId** (Integer):  
      The role ID assigned to the user within the organization.

    - **enableScreenshot** (Integer):  
      A flag (1 or 0) indicating whether screenshot functionality is enabled for this organization.

    - **description** (String):  
      A brief description of the organization.

    - **role** (String):  
      The role of the user in the organization (e.g., `"Super Admin"`).

    - **otherRoleIds** (Array of Integers):  
      Additional role IDs assigned to the user in the organization.

## Sample Response


```json
{
    "status": "success",
    "data": {
        "organisationList": [
            {
                "name": "ExampleOrg",
                "organisationId": 123,
                "image": "https://example.com/images/org123.png",
                "roleId": 2,
                "enableScreenshot": 0,
                "description": "Example organization description",
                "role": "Admin",
                "otherRoleIds": [3, 4]
            },
            {
                "name": "TestOrg",
                "organisationId": 456,
                "image": "https://example.com/images/org456.png",
                "roleId": 3,
                "enableScreenshot": 1,
                "description": "Test organization for demo purposes",
                "role": "User",
                "otherRoleIds": []
            }
        ]
    },
    "message": "User organisation list"
}
```
# Error Handling
- **401** (Unauthorized) :
  If the user is not authenticated or does not have permission to access the organization details.
- **400** (Bad Request) :
  If required fields are missing or invalid.
- **500** (Internal Server Error) :
  If an unexpected server-side issue occurs.


# Employee Organization Projects API Documentation

### **Endpoint Details**
- **Base URL:** `https://bustlespot-api.gamzinn.com`
- **Route:** `/api/organisation/getUserOrganization`
- **Method:** `POST`
- **Content-Type:** `application/json`
- **Authorization:** Bearer Token

### **Request Parameters**
The API expects a JSON payload with the following structure:

- **organisationId** (number, required):  
  The unique identifier of the organization for which the project list is to be retrieved.

#### **Example Request Body:**
```json
{
    "organisationId": 123
}
```

---

### **Response Parameters**
If the request is successful, the API returns a 200 OK response with the project details for the specified organization.

#### **Response Structure:**
```json
{
  "status": "success",
  "data": {
    "projectLists": [
      {
        "projectId": 101,
        "name": "Project Alpha",
        "status": 0,
        "startDate": "2023-01-01T10:00:00.000Z",
        "users": "[{\"userId\": 201, \"fullName\": \"John Doe\", \"profileImage\": null}, {\"userId\": 202, \"fullName\": \"Jane Smith\", \"profileImage\": null}]"
      },
      {
        "projectId": 102,
        "name": "Project Beta",
        "status": 0,
        "startDate": "2024-02-15T09:30:00.000Z",
        "users": "[{\"userId\": 301, \"fullName\": \"Alice Brown\", \"profileImage\": \"https://example.com/profiles/301.jpg\"}, {\"userId\": 302, \"fullName\": \"Bob White\", \"profileImage\": \"https://example.com/profiles/302.jpg\"}]"
      }
    ]
  },
  "message": "User organization project list retrieved successfully."
}
```

### **Notes:**
- The **status** field indicates whether the request was successful.
- The **data** field contains a list of projects associated with the organization.
- Each project includes its `projectId`, `name`, `status`, `startDate`, and a `users` array containing details of assigned users.
- The **message** field provides additional information about the request outcome.

---
**Example Usage:**  
Use a tool like Postman or cURL to send a request:
```sh
curl -X POST "https://bustlespot-api.gamzinn.com/api/organisation/getUserOrganization" \
-H "Authorization: Bearer <token>" \
-H "Content-Type: application/json" \
-d '{ "organisationId": 123 }'
```

### **Error Handling**
| Status Code | Meaning | Description |
|------------|---------|-------------|
| **401** | Unauthorized | Invalid or missing authentication token. |
| **400** | Bad Request | organisationId is missing or invalid. |
| **500** | Internal Server Error | An unexpected server error occurred. |

## Project Tasks API Documentation

### **Endpoint Details**
- **Base URL:** `https://bustlespot-api.gamzinn.com`
- **Route:** `/api/task/getTaskByProjectId`
- **Method:** `POST`
- **Content-Type:** `application/json`
- **Authorization:** Bearer Token

### **Request Parameters**
The API expects a JSON payload with the following structure:

- **projectId** (number, required):  
  The ID of the project for which the tasks are to be retrieved.
- **organisationId** (number, required):  
  The ID of the organization to which the project belongs.

#### **Example Request Body:**
```json
{
    "projectId": 815,
    "organisationId": 1312
}
```

---

### **Response Schema**
If successful, the response will adhere to the following schema:

```json
{
  "taskId": 1001,
  "taskName": "Design Login UI",
  "taskDescription": "Create a responsive login screen",
  "assignedTo": "John Doe",
  "dueDate": "2024-12-31T23:59:59Z"
}
```

### **Response Fields**
- **taskId** (number): Unique identifier for the task.
- **taskName** (string): Name/title of the task.
- **taskDescription** (string): Detailed description of the task.
- **assignedTo** (string): Full name of the user to whom the task is assigned.
- **dueDate** (date-time): ISO formatted due date for the task.

### **Error Handling**
| Status Code | Meaning | Description |
|------------|---------|-------------|
| **401** | Unauthorized | Authentication token missing or invalid. |
| **400** | Bad Request | One or more required fields are missing or invalid. |
| **500** | Internal Server Error | Server encountered an unexpected condition. |

---
**Example Usage:**
```sh
curl -X POST "https://bustlespot-api.gamzinn.com/api/task/getTaskByProjectId" \
-H "Authorization: Bearer <token>" \
-H "Content-Type: application/json" \
-d '{ "projectId": 815, "organisationId": 1312 }'
```
 
# Endpoint: Add Activity List

- **URL:** `https://bustlespot-api.gamzinn.com/api/activity/addActivityList`
- **Method:** `POST`
- **Authorization:** Bearer Token
- **Content-Type:** `application/json`

### Request Body

**activityData** (array): An array of activity objects, each containing:

| Field             | Type     | Description                                      |
|------------------|----------|--------------------------------------------------|
| taskId           | number   | The ID of the task.                             |
| projectId        | number   | The ID of the project.                          |
| startTime        | string   | Start time of the activity (ISO 8601 format).   |
| endTime          | string   | End time of the activity (ISO 8601 format).     |
| mouseActivity    | number   | Percentage of mouse activity.                   |
| keyboardActivity | number   | Percentage of keyboard activity.                |
| totalActivity    | number   | Total activity percentage.                      |
| notes            | string   | Additional notes for the activity.              |
| organisationId   | number   | The ID of the organization.                     |
| uri              | string   | URI for additional details or screenshots.      |
| unTrackedTime    | number   | Untracked time during the activity.             |

#### Example Request Body
```json
{
  "activityData": [
    {
      "taskId": 1705,
      "projectId": 815,
      "startTime": "2025-03-03T11:34:00Z",
      "endTime": "2025-03-03T11:54:00Z",
      "mouseActivity": 50,
      "keyboardActivity": 50,
      "totalActivity": 100,
      "notes": "Worked on API integration",
      "organisationId": 1312,
      "uri": "https://example.com/screenshot1.png",
      "unTrackedTime": 0
    }
  ]
}
```

### Response

```json
{
  "status": "success",
  "data": {
    "success": true,
    "activities": {
      "fieldCount": 0,
      "affectedRows": 1,
      "insertId": 2283680,
      "serverStatus": 2,
      "warningCount": 0,
      "message": "",
      "protocol41": true,
      "changedRows": 0
    }
  },
  "message": "Success"
}
```

### Response Description

- **status:** Indicates the status of the request (e.g., "success").
- **data.success:** Boolean indicating if the activity was added.
- **data.activities:** Contains database insertion metadata (e.g., `affectedRows`, `insertId`).
- **message:** Descriptive success or error message.

---





