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

- **Base URL:** `https:/bustlespot-api.gamzinn.com`
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

- **userId** (
