<p>Add the following line to dependency section in build.gradle file</p>
<p>compile 'com.khalti:khalti-android:1.1.2'</p>
<p>It is recommended that you update your support libraries to the latest version. However, if you're unable to update the libraries add the following line instead.</p>
<p>compile ('com.khalti:khalti-android:1.1.2') {</p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; transitive = true</p>
<p>&nbsp;&nbsp;&nbsp; }</p>
<p>Note : We recommend you to use the latest version of Build tools and Support libraries for maximum compatibility.</p>
<p>In order to build and run this project, please use Android Studio 3 and please note that the minimum Build tools and Support libraries version should be 26.</p>
<p>compileSdkVersion 26</p>
<p>buildToolsVersion '26.0.2'</p>
<p>&nbsp;</p>
<p>compile 'com.android.support:appcompat-v7:26.1.0'</p>
<p>In order to add support library 26, add the Google's maven url in your project level build.gradle</p>
<p>repositories {</p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; jcenter()</p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; mavenCentral()</p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; maven { url "https://maven.google.com" }</p>
<p>&nbsp;&nbsp;&nbsp; }</p>
<h1>Usage</h1>
<h2>1.&nbsp;&nbsp; Layout</h2>
<p>You can add WLinkButton to your xml layout</p>
<p><span style="background-color: #ccffff;">&lt;payment.worldlink.com.wlinkpayment.widgets.WLinkButton</span><br /><span style="background-color: #ccffff;"> &nbsp;&nbsp;&nbsp; android:id="@+id/wlink_button"</span><br /><span style="background-color: #ccffff;"> &nbsp;&nbsp;&nbsp; android:layout_width="match_parent"</span><br /><span style="background-color: #ccffff;"> &nbsp;&nbsp;&nbsp; android:layout_height="wrap_content"</span><br /><span style="background-color: #ccffff;"> &nbsp;&nbsp;&nbsp; android:layout_margin="5dp"</span><br /><span style="background-color: #ccffff;"> &nbsp;&nbsp;&nbsp; app:wlink_button_style="Rounded_Corners" /&gt;</span></p>
<p>&nbsp;</p>
<p>And, Locate your xml WLink Button in your Java</p>
<p><span style="background-color: #ccffff;">WLinkButton wlinkButton = (WLinkButton) findViewById(R.id. wlink_button);</span></p>
<p>Or, use it in Java</p>
<p><span style="background-color: #ccffff;">WLinkButton wlinkButton = new WLinkButton();</span></p>
<p>And, add this java WLinkButton into your layout container.</p>
<h2>2.&nbsp;&nbsp; Configure</h2>
<p>You will need to provide a configuration file to WLinkButton.</p>
<p>When creating a configuration file, you will need to instantiate WlinkConfig Class by passing &lsquo;Merchant Name&rsquo;, &lsquo;Merchant App Id&rsquo;, and a new instance of OnDetailsFetchedListener.&nbsp;</p>
<p>&nbsp;</p>
<p><span style="background-color: #ccffff;">WlinkConfig wlinkConfig=<strong>new </strong>WlinkConfig(&ldquo;Merchant Name&rdquo;, &ldquo;Merchant App Id&rdquo;, <strong>new </strong>Interface.OnDetailsFetchedListener() {</span><br /><span style="background-color: #ccffff;"> &nbsp;&nbsp;&nbsp; @Override</span><br /><span style="background-color: #ccffff;"> &nbsp;&nbsp;&nbsp; <strong>public void </strong>onDetailsFetched(WlinkDetails wlinkDetails) {</span><br /><span style="background-color: #ccffff;"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;//Returns all the details about the user when user proceeds for payment.&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; // Create a payment transaction with the amount and username returned from wlinkDetails.&nbsp;&nbsp;&nbsp;&nbsp; //After Successful payment, pass the transaction id from the payment and the wlinkDetails to WLinkButton (Step 4)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; }</span><br /> <br /><span style="background-color: #ccffff;"> &nbsp; &nbsp; &nbsp;&nbsp;@Override</span><br /><span style="background-color: #ccffff;"> &nbsp;&nbsp;&nbsp; <strong>public void </strong>onError(String Error) {</span><br /><span style="background-color: #ccffff;"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; //returns error </span><br /><span style="background-color: #ccffff;"> &nbsp;&nbsp;&nbsp; }</span><br /><span style="background-color: #ccffff;"> });</span></p>
<p>&nbsp;</p>
<p>&nbsp;<strong>Note:</strong>&nbsp;WlinkDetails class consists of:<strong>&nbsp;</strong>&nbsp;1.&nbsp;&nbsp;&nbsp;&nbsp; Username (String): User name of the World Link Customer.&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 2.&nbsp;&nbsp;&nbsp;&nbsp; Amount to be Paid (Long): Total Amount of the pending/advance payment of the &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Customer.&nbsp;3.&nbsp;&nbsp;&nbsp;&nbsp; Has Advance Payment (Boolean): Determines the World Link Customer can pay in advance.&nbsp;4.&nbsp;&nbsp;&nbsp;&nbsp; Has Due&nbsp; (Boolean): Identifies the world link user have due left to be paid.&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 5. VolumeRechargeId (Integer): Give the selected volume id of the user if user have volume based subscription.&nbsp;<strong><br /> </strong></p>
<p><strong>&nbsp;</strong></p>
<h2>3.&nbsp;&nbsp; Set Configuration to WLinkButton.</h2>
<p>Finally set your config in your WLinkButton.</p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; wLinkButton.setConfig(wlinkConfig);</p>
<h2>&nbsp;</h2>
<h2>4.&nbsp;&nbsp; Generate Transaction.</h2>
<p>After the WlinkDetails are generated and a payment is successfully made, pass the transaction id of the payment and WlinkDetails to WLinkButton to generate a transaction.</p>
<p><strong>wLinkButton</strong>.GenerateTransaction(wlinkDetails, <strong>&ldquo;transactionId&rdquo;</strong>, <strong>new </strong>Interface.OnInvoiceGenerateListener() {<br /> &nbsp;&nbsp;&nbsp; @Override<br /> &nbsp;&nbsp;&nbsp; <strong>public void </strong>onInvoiceGenerated(String InvoiceNumber) {<br /> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; // This will send request to WorldLink&rsquo;s transaction to activate customer&rsquo;s account.<strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; // On Successful transaction, it will return the invoice number</strong> which needs to be saved on merchant&rsquo;s database along with the transaction details for further validation.&nbsp;&nbsp;&nbsp; }<br /> <br /> &nbsp;&nbsp;&nbsp; @Override<br /> &nbsp;&nbsp;&nbsp; <strong>public void </strong>onFailure(String Error) {<br /> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; //Returns error on process failure. <br /> <br /> &nbsp;&nbsp;&nbsp; }<br /> });</p>
<p>&nbsp;</p>
<h2>&nbsp;</h2>
<p>&nbsp;&nbsp;&nbsp;</p>
<p>&nbsp;</p>
<p>&nbsp;</p>
