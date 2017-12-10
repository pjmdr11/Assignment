Add the following line to dependency section in build.gradle file
compile 'com.khalti:khalti-android:1.1.2'
It is recommended that you update your support libraries to the latest version. However, if you're unable to update the libraries add the following line instead.
compile ('com.khalti:khalti-android:1.1.2') {
        transitive = true
    }
Note : We recommend you to use the latest version of Build tools and Support libraries for maximum compatibility.
In order to build and run this project, please use Android Studio 3 and please note that the minimum Build tools and Support libraries version should be 26.
compileSdkVersion 26
buildToolsVersion '26.0.2'

compile 'com.android.support:appcompat-v7:26.1.0'
In order to add support library 26, add the Google's maven url in your project level build.gradle
repositories {
        jcenter()
        mavenCentral()
        maven { url "https://maven.google.com" }
    }
Usage
1.	Layout
You can add WLinkButton to your xml layout

<pre><code>

<payment.worldlink.com.wlinkpayment.widgets.WLinkButton
    android:id="@+id/wlink_button"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_margin="5dp"
    app:wlink_button_style="Rounded_Corners" />
</code></pre>


And, Locate your xml WLink Button in your Java
WLinkButton wlinkButton = (WLinkButton) findViewById(R.id. wlink_button);
Or, use it in Java
WLinkButton wlinkButton = new WLinkButton();
And, add this java WLinkButton into your layout container.
2.	Configure
You will need to provide a configuration file to WLinkButton.
When creating a configuration file, you will need to instantiate WlinkConfig Class by passing ‘Merchant Name’, ‘Merchant App Id’, and a new instance of OnDetailsFetchedListener.

WlinkConfig wlinkConfig=new WlinkConfig(“Merchant Name”, “Merchant App Id”, new Interface.OnDetailsFetchedListener() {
    @Override
    public void onDetailsFetched(WlinkDetails wlinkDetails) {
      
 	 //Returns all the details about the user when user proceeds for payment.
	
	// Create a payment transaction with the amount and username returned from wlinkDetails.

	//After Successful payment, pass the transaction id from the payment and the wlinkDetails to WLinkButton (Step 4)
	
	}

  	  @Override
    public void onError(String Error) {
         //returns error 
    }
});

Note:

WlinkDetails class consists of:


1.	Username (String): User name of the World Link Customer.
	
2.	Amount to be Paid (Long): Total Amount of the pending/advance payment of the 		Customer.

3.	Has Advance Payment (Boolean): Determines the World Link Customer can pay in advance.

4.	Has Due  (Boolean): Identifies the world link user have due left to be paid.
	
	5. VolumeRechargeId (Integer): Give the selected volume id of the user if user have volume based subscription.

 
3.	Set Configuration to WLinkButton.
Finally set your config in your WLinkButton.
	wLinkButton.setConfig(wlinkConfig);

4.	Generate Transaction.
After the WlinkDetails are generated and a payment is successfully made, pass the transaction id of the payment and WlinkDetails to WLinkButton to generate a transaction.
wLinkButton.GenerateTransaction(wlinkDetails, “transactionId”, new Interface.OnInvoiceGenerateListener() {
    @Override
    public void onInvoiceGenerated(String InvoiceNumber) {
        // This will send request to WorldLink’s transaction to activate customer’s account.
	// On Successful transaction, it will return the invoice number which needs to be saved on merchant’s database along with the transaction details for further validation.
    }

    @Override
    public void onFailure(String Error) {
        //Returns error on process failure. 

    }
});







