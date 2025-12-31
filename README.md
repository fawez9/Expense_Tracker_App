# Expense Tracker App

This is a simple Android application for tracking personal expenses. It allows users to register, log in, and manage a personal list of their expenditures.

## Features

-   **User Authentication**: Secure user registration and login system. New users can create an account, and existing users can log in to access their data.
-   **Session Management**: The app keeps users logged in across sessions using `SharedPreferences`. A logout option is also provided.
-   **Expense Management (CRUD)**:
    -   **Create**: Add new expenses with a name and amount.
    -   **Read**: View a list of all your expenses on the main screen.
    -   **Update**: Edit the name and amount of existing expenses.
    -   **Delete**: Remove expenses you no longer need to track.
-   **Total Expense Calculation**: Automatically calculates and displays the total sum of all recorded expenses.
-   **User-Specific Data**: Each user's expense data is stored separately and is only visible after they have logged in.

## Technology Stack

-   **Language**: Java
-   **Platform**: Android
-   **Database**: SQLite for local data persistence.
-   **Build Tool**: Gradle
-   **UI**: Android XML with Material Components.

## Getting Started

To get a local copy up and running, follow these simple steps.

### Prerequisites

-   Android Studio (latest stable version recommended)
-   An Android Virtual Device (AVD) or a physical Android device

### Installation

1.  **Clone the repository:**
    ```sh
    git clone https://github.com/fawez9/Expense_Tracker_App.git
    ```
2.  **Open in Android Studio:**
    -   Launch Android Studio.
    -   Select `File -> Open` and navigate to the cloned project directory.
3.  **Build the project:**
    -   Android Studio will automatically sync the project with the Gradle files. Wait for the process to complete.
4.  **Run the application:**
    -   Select a run configuration (either an emulator or a connected physical device).
    -   Click the 'Run' button (▶) or use the shortcut `Shift + F10`.

## How It Works

The application follows a straightforward flow for managing user-specific expenses:

1.  **Registration & Login**: On first launch, the user is presented with a login screen. New users can navigate to the registration screen to create an account with their name, a valid Gmail address, and a password. User credentials are stored in a local SQLite database.
2.  **Main Dashboard**: After a successful login, the user is taken to the `MainActivity`. This screen serves as the main dashboard, displaying a list of their expenses and the total amount.
3.  **Adding an Expense**: By clicking the "Add Expense" button, the user is taken to `AddExpenseActivity`, where they can input the expense name and amount. Upon saving, the new expense is added to the database and the main list is updated.
4.  **Editing and Deleting**: Users can tap on an expense in the list to bring up a dialog with options to "Edit" or "Delete" the selected item. All changes are reflected immediately in the UI and persisted in the database.
5.  **Data Persistence**: A `DatabaseHelper` class manages all interactions with the SQLite database, handling the creation of tables for users and expenses, as well as all CRUD (Create, Read, Update, Delete) operations. Expenses are linked to users via a `userId`.
6.  **Session Control**: `SharedPreferences` are used to store the logged-in user's session details, allowing them to remain logged in even after closing the app. The "Logout" button clears these preferences and returns the user to the login screen.
