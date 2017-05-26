Usage:
* Run `bash generate_dummy_classes.sh 1000`
 in order to generate 1000 dummy classes
* Run `mvn package`
* Run `mvn exec:exec`

In order to switch JavassistTransformer/AsmTransformer,
modify `instrumentation.InstrumentationAgent` class.

Results of instrumentation time comparison 
(for 1000 test classes, 10 runs):

* Javassist: ~2000ms
* ASM: ~900ms