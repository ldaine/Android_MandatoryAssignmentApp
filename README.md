# Android_MandatoryAssignmentApp

Android Shopping List App which was created during Android Course in BAAA. 
The functionality of the app: 

## 1	Product List
The product list is populated from FireBase using FireBase UI List Adapter. 

## 2	Adding New Items
Populating input field in an onClick Listener and pushing the new instance of Product class to Firebase

## 3	Clearing the Product List
Clearing the List can be done from ‘Clear List’ option in overflow or the trash bin icon in the action bar. In the landscape mode the text is combined with the icon.
Clear list calls a function callCofirmClearList().
The function is responsible of creating a dialog to confirm or reject the action. It is achieved by creating an instance of ConfirmDialogFragment. When calling the Fragment, the positiveClick() and negativeClick() actions are overwritten.
In case of positive click, the list is cleared by FireBase method. listRef.Child(“products”).remove(). It could be done also by setting the value of the products to null. 
After the product list is cleared the list items cannot be returned as a copy of the list is not saved.

## 4	Deleting an Item
Deleting an item is done by using Event listener on Remove button. The function which is set in action makes a copy of the product and deletes it from Firebase. 
If Snackbar ‘UNDO’ button is used, the previously saved product is pushed back to FireBase. 
The order of the products doesn’t stay the same though. To achieve that each product should have an Priority. 
##5	Launcher Icon
The place where it is possible to define global App settings is file called AndroidManifest.xml. 
Here you can define also which image file should be used as icon for the app. 

## 6	Orientation Change
When orientation is changed, the app is re-launched. Although the state of the app is saved automatically, variables which are needed should be saved and retrieved manually. 
 
I use the function onSaveInstanceState() and onRestoreInstanceState() to save and retrieve the values in oriantation change.   
 
The only value I save in orientation change is the the key for the list in ShoppingListActivity. Other than that there are no unique variables that should be saved. All the values are retrieved from FireBase every time an activity is launched. 
To save the dialog state on orientation change the setRetainInstance(true) needed to be set to true in the DialogFragment class. If not set, the app would crash if the orientation changes while dialog is open. 

## 7	Sharing a list
The product List can be shared from the List Detail View – ShoppingListActivity View - 
by clicking the share button in the ActionBar. 
 
When you click the icon, two functions are called: 
•	convertListToString(), which converts the list entries to one string.
•	setShareIntent(), which creates the intent and calls the activity. 
 
convertListToString(): The function loops through List adapter and adds each products string value to a single string. To get the list item from Firebase Adapter, I use build in getItem() method.
 
setShareIntent(): the method creates an intent with the action SEND, which indicates the android system that another application is needed. I add the string passed to this function by putting extra data to the intent. 
 
## 8	Settings
The Settings can be changed by clicking the Settings icon in the ActionBar in the MainActivity View or ‘Settings’ in the overflow menu of the ShoppingListActivity View
There are several settings which can be changed. The settings which I use to adjust the app are Name, Email and Measurement. 
### 8.1	Name
The name is used to personalize the List view ActionBar title. After setting the name my action 
Bar title changes to “<name>’s Lists”. 
### 8.2	Email
Also email can be changed. The email is changed not just in local preferences but also in FireBase itself. It is done in SettingsActivity.java 

### 8.3	Measurement
Measurement sets the default measurement to use in the spinner when the ShoppingListActivity View is launched. 

## 8.4	Other
Following Preferences are for test purposes and not used in the App itself.
•	Gender – radio button list
•	Sound Enabled – checkbox

## 9	FireBase
All the data are stored in FireBase. Each user has its own node in the tree. The user node is created when the user signs up. 
 
I use Firebase UI to populate the data. 
 
The conversion from java Class to FireBase node is handled by FireBase itself. To achieve this class needed to meet conditions which are specified in the documentation, for Example, having an empty constructor and having the set and get methods for every property is mandatory. 

## 10	Authentication using FireBase
Authentication gives the opportunity to separate the lists and products into unique account nodes. For this application I used simple email & password authentication. 
The files responsible for this feature are SignupActivity and LoginActivity. To check if the user is signed in, I use condition statement in onCreate() methods. 


If checkups go wrong, I call function loadLoginView(), which starts the LoginActivity. 
 
The SignupActivity uses FireBase method createUser() to create an user. 
To log the user in, authWithPassword() is used to verify that such user exists. If the method returns success, the user is forwarded to MainActivity and the email and password preferences are saved in the Shared Preferences. 

## 11	Other Features
### 11.1	Open a list to view and edit list items. 
To open a list I need to pass the key of the list to the ShoppingListActivity. It is achieved by adding onClickListener on the ListView. 
 
When the ShoppingListACtivity start, I retrieve the key value from the intent. 
 
After the key is known the data can be fetched from database. 
### 11.2	Delete List
The deletion of the list is similar to deletion of the product. The difference is that the Activity must be destroyed after the list is deleted as it is done from the list Detail View itself. 
