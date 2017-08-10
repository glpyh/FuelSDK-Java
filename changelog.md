## Version 1.2.0 - 08/08/2017 ##
* Project tree structure 
    * Source Packages       : SDK package (src/main/java/com/exacttarget/fuelsdk/)
    * Test Packages         : JUnit test package (src/test/java/com/exacttarget/fuelsdk/)
    * Annotation Packages   : The annotation package (src/main/java/com/exacttarget/fuelsdk/annotations/)
    * Documentation		: SDK API HTML documentation (docs/)

* New addition to the source packages
    - Added ETProfileAttribute.java to create new Subcriber
    - Added SendClassification on ETTriggeredEmail to create new Triggered Send Definition

* JUnit test case : This covers basic happy path testing. All the test cases use “ET” classes. Advanced and more comprehensive test cases will be added in future releases. Added new JUnit test cases:
     - ETCampaignTest.java
     - ETCampaignAssetTest.java
     - ETEmailTest.java
     - ETListTest.java
     - ETSubscriberListTest.java
     - ETSubscriberTest.java
     - ETTriggeredTest.java

* API docs : added API documentation using doxygen documentation framework. (under docs/ directory)
