# test-pay

### What does this Application does?
Test-pay is a simple app that simulates a payment flow using the Paystack Plaform.

### Technologies Used
Paystack Android SDK
Java
Kotlin
XML
Retrofit

### How should this be manually tested?
Clone this repository Open the project in android studio, while you are connected to the internet
gradle will automatically attempt to download the appropriate gradle wrapper version, there after
it will download all necessary dependencies. you can then connect your device and intall the app by
clicking the green play icon run button

### Use the following as Card details, an actual card maybe declined.
- Card Number : ``5060666666666666666`` no spacing.
- CVC Number : ``123``
- Expiry Year : ``21`` any future year will do but ``two`` digits
- Expiry Month : ``11`` any future month will do but ``two`` digits
- Email : you can use your email just to be sure transaction was successful, you will get a mail
automatically.
- Amount : can be any amount

### Note
- The amount is usually divided by 100 as this is a test app, original implementation should use
actual amount(This is done both locally and on the server).

- You might also need the internet for a gradle sync.